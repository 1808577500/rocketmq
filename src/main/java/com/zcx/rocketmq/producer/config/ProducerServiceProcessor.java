package com.zcx.rocketmq.producer.config;


import com.zcx.rocket.base.MQSendResult;
import com.zcx.rocket.base.TopicEnum;
import com.zcx.rocket.error.RocketMQErrorEnum;
import com.zcx.rocket.exception.RocketMQException;
import com.zcx.rocketmq.producer.service.TransactionExecuterimpl;

import java.util.List;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 
 * @ClassName:  ProducerServiceProcessor   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zcx
 * @date:   2019年3月22日 下午10:51:23   
 *
 */
@Service
public class ProducerServiceProcessor implements ProducerService {

	Logger LOGGER = LoggerFactory.getLogger(ProducerServiceProcessor.class);
    @Autowired
    @Qualifier("defaultMQProducer")
    DefaultMQProducer defaultMQProducer;
    @Autowired
    @Qualifier("transactionMQProducer")
    TransactionMQProducer transactionMQProducer;

    /**
     * 发送消息的延时等级（默认为1：不延时）
     * RcoketMQ的延时等级为：1s，5s，10s，30s，1m，2m，3m，4m，5m，6m，7m，8m，9m，10m，20m，30m，1h，2h。level=0，表示不延时
     */
    private final Integer delayTimeLevel = 1;


    @Override
    public void validateSendMsg(TopicEnum topic, String tag, String msg) {
        if (null == topic || null == tag || null == msg) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL);
        }
    }

    @Override
    public MQSendResult send(TopicEnum topic, String tag, String keys, String msg) {
        return send(topic, tag, keys, msg, null);
    }


    @Override
    public MQSendResult send(TopicEnum topic, String tag, String keys, String msg, Integer delayTimeLevel) {
        if (null == delayTimeLevel) {
            delayTimeLevel = this.delayTimeLevel;
        }
        MQSendResult mqSendResult = null;
        try {
            this.validateSendMsg(topic, tag, msg);
            Message sendMsg = new Message(topic.getCode(), tag, keys, msg.getBytes());
            sendMsg.setDelayTimeLevel(delayTimeLevel);
            SendResult sendResult = defaultMQProducer.send(sendMsg);
            LOGGER.info(sendResult.toString());
            mqSendResult = new MQSendResult(sendResult);
        } catch (RocketMQException e) {
        	LOGGER.error(e.getMessage(), e);
            mqSendResult = new MQSendResult(e.getErrMsg(), e);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
        	LOGGER.error(e.getMessage(), e);
            mqSendResult = new MQSendResult("消息发送失败{}", e);
        }
        return mqSendResult == null ? new MQSendResult() : mqSendResult;
    }

	/**   
	 * <p>Title: send</p>   
	 * <p>Description: </p>   
	 * @param topic
	 * @param tag
	 * @param keys
	 * @param msg
	 * @param selecter
	 * @param orderId
	 * @return   
	 * @see com.zcx.rocketmq.producer.config.ProducerService#send(com.zcx.rocket.base.TopicEnum, java.lang.String, java.lang.String, java.lang.String, org.apache.rocketmq.client.producer.MessageQueueSelector, java.lang.String)   
	 */  
	@Override
	public MQSendResult sendByOrder(TopicEnum topic, String tag, String keys, String msg,
			Long orderId) {
		MQSendResult mqSendResult = null;
        try {
            this.validateSendMsg(topic, tag, msg);
            Message sendMsg = new Message(topic.getCode(), tag, keys, msg.getBytes());
            sendMsg.setDelayTimeLevel(delayTimeLevel);
            SendResult sendResult = defaultMQProducer.send(sendMsg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long id = (Long) arg;
                    long index = id % mqs.size();
                    return mqs.get((int)index);
                }
            }, orderId);//);
            LOGGER.info(sendResult.toString());
            mqSendResult = new MQSendResult(sendResult);
        } catch (RocketMQException e) {
        	LOGGER.error(e.getMessage(), e);
            mqSendResult = new MQSendResult(e.getErrMsg(), e);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
        	LOGGER.error(e.getMessage(), e);
            mqSendResult = new MQSendResult("消息发送失败{}", e);
        }
        return mqSendResult == null ? new MQSendResult() : mqSendResult;
	}

	/**   
	 * <p>Title: sendByTransaction</p>   
	 * <p>Description: </p>   
	 * @param topic
	 * @param tag
	 * @param keys
	 * @param msg
	 * @param orderId
	 * @return   
	 * @see com.zcx.rocketmq.producer.config.ProducerService#sendByTransaction(com.zcx.rocket.base.TopicEnum, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)   
	 */  
	@Override
	public MQSendResult sendByTransaction(TopicEnum topic, String tag, String keys, String msg, Long orderId) {

		MQSendResult mqSendResult = null;
        try {
            this.validateSendMsg(topic, tag, msg);
            Message sendMsg = new Message(topic.getCode(), tag, keys, msg.getBytes());
            sendMsg.setDelayTimeLevel(delayTimeLevel);
            TransactionExecuterimpl TransactionExecuterimpl = new TransactionExecuterimpl();
            SendResult sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, orderId);
            LOGGER.info(sendResult.toString());
            mqSendResult = new MQSendResult(sendResult);
        } catch (RocketMQException e) {
        	LOGGER.error(e.getMessage(), e);
            mqSendResult = new MQSendResult(e.getErrMsg(), e);
        } catch (MQClientException e) {
        	LOGGER.error(e.getMessage(), e);
            mqSendResult = new MQSendResult("消息发送失败{}", e);
        }
        return mqSendResult == null ? new MQSendResult() : mqSendResult;
	}

    
}
