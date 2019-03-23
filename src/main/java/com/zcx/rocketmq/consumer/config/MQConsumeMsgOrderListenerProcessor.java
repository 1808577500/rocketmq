package com.zcx.rocketmq.consumer.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.zcx.rocket.base.MQConsumeResult;
import com.zcx.rocket.error.RocketMQErrorEnum;
import com.zcx.rocket.exception.RocketMQException;

@Component
public class MQConsumeMsgOrderListenerProcessor implements MessageListenerOrderly{

    private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgOrderListenerProcessor.class);

    @Autowired
    private Map<String, MQMsgProcessor> mQMsgProcessorMqp;
    

    /**
     * 消费msg
     *
     * @param topic
     * @param tag
     * @param value
     */
    private void consumerMsg(String topic, String tag, MessageExt value) {
        try {
            MQMsgProcessor mqMsgProcessor = selectConsumerService(topic, tag);
            if (mqMsgProcessor == null) {
                throw new RocketMQException(RocketMQErrorEnum.NOT_FOUND_CONSUMESERVICE);
            }
            //调用该类的方法,处理消息
            MQConsumeResult mqConsumeResult = mqMsgProcessor.hadleMsg(topic, tag, value);
            if (mqConsumeResult == null) {
                throw new RocketMQException(RocketMQErrorEnum.HANDLE_RESULT_NULL);
            }
            if (mqConsumeResult.isSuccess()) {
            	logger.info("消费消息成功");
            } else {
            	// ②如果重试了三次就返回成功
                if (value.getReconsumeTimes() < 3) {
                    throw new RocketMQException(RocketMQErrorEnum.CONSUME_FAIL);
                } else {
                	logger.info("重试3次失败");
                }
            }
        } catch (Exception e) {
            if (e instanceof RocketMQException) {
                RocketMQException rocketMQException = (RocketMQException) e;
                throw rocketMQException;
            } else {
                throw e;
            }
        }

    }

    /**
     * 反射得到consumerService
     *
     * @param topic
     * @param tag
     * @return
     */
    private MQMsgProcessor selectConsumerService(String topic, String tag) {
        MQMsgProcessor mqMsgProcessor = null;
        for (Map.Entry<String, MQMsgProcessor> mqMsgProcessorEntry : mQMsgProcessorMqp.entrySet()) {
            ConsumerAnnotation mqConsumeService = mqMsgProcessorEntry.getValue().getClass().getAnnotation(ConsumerAnnotation.class);
            if (mqConsumeService == null) {
                continue;
            }
            //拿到订阅的topic
            String topicAnnotation = mqConsumeService.topic().getCode();
            if (!topic.equals(topicAnnotation)) {
                continue;
            }
            String[] tagsAnnotation = mqConsumeService.tag();
            if (Arrays.stream(tagsAnnotation).anyMatch(t -> "*".equals(t) || t.equals(tag))) {
                mqMsgProcessor = mqMsgProcessorEntry.getValue();
                break;
            }
        }
        return mqMsgProcessor;
    }


	/**   
	 * <p>Title: consumeMessage</p>   
	 * <p>Description: </p>   
	 * @param arg0
	 * @param arg1
	 * @return   
	 * @see org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly#consumeMessage(java.util.List, org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext)   
	 */  
	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
		context.setAutoCommit(true);
		if(CollectionUtils.isEmpty(msgs)){
            logger.info("接收到的消息为空，不做任何处理");
            return ConsumeOrderlyStatus.SUCCESS;
        }
		ConsumeOrderlyStatus consumeConcurrentlyStatus = ConsumeOrderlyStatus.SUCCESS;
        try {
            // 根据topic 分组
            Map<String, List<MessageExt>> mapTopics = msgs.stream().collect(Collectors.groupingBy(MessageExt::getTopic));
            for (Map.Entry<String, List<MessageExt>> extEntity : mapTopics.entrySet()) {
                String topic = extEntity.getKey();
                //根据tag分组
                Map<String, List<MessageExt>> mapTags = extEntity.getValue().stream().collect(Collectors.groupingBy(MessageExt::getTags));
                for (Map.Entry<String, List<MessageExt>> entry : mapTags.entrySet()) {
                    String tag = entry.getKey();
                    for (int i = 0; i < entry.getValue().size(); i++) {
                    	logger.info("topic " + topic + " || tag:" + tag + " || message:" + new String(entry.getValue().get(i).getBody()));
                    	consumerMsg(topic, tag, entry.getValue().get(i));
					}
                }
            }
        } catch (Exception e) {
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return consumeConcurrentlyStatus;
	}
}
