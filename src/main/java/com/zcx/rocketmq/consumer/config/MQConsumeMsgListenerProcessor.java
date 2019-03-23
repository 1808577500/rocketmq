package com.zcx.rocketmq.consumer.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.zcx.rocket.base.MQConsumeResult;
import com.zcx.rocket.error.RocketMQErrorEnum;
import com.zcx.rocket.exception.RocketMQException;

/**
 * 
 * @ClassName:  MQConsumeMsgListenerProcessor   
 * @Description:消费配置 
 * @author: zcx
 * @date:   2019年3月23日 下午8:27:49   
 *
 */
@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently{

    private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);

    @Autowired
    private Map<String, MQMsgProcessor> mQMsgProcessorMqp;
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(msgs)){
            logger.info("接收到的消息为空，不做任何处理");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
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
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return consumeConcurrentlyStatus;
    }
    

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
}
