package com.zcx.rocketmq.consumer.service;


import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.zcx.rocket.base.MQConsumeResult;
import com.zcx.rocket.base.TopicEnum;
import com.zcx.rocketmq.consumer.config.ConsumerAnnotation;
import com.zcx.rocketmq.consumer.config.MQConsumerPrcessor;

/**
 * 消费1
 * @ClassName:  Consumer1   
 * @Description:消费   
 * @author: zcx
 * @date:   2019年3月22日 下午10:43:41   
 *
 */
@ConsumerAnnotation(topic = TopicEnum.Topic1, tag= {"tag1","tag2"})
@Component
public class Consumer1 extends MQConsumerPrcessor{
	
	Logger Logger = LoggerFactory.getLogger(Consumer1.class);
	
	/**   
	 * <p>Title: consumer</p>   
	 * <p>Description: </p>   
	 * @param topic
	 * @param tag
	 * @param messageExt
	 * @return   
	 * @see com.zcx.rocketmq.consumer.config.MQConsumerPrcessor#consumer(java.lang.String, java.lang.String, com.alibaba.rocketmq.common.message.MessageExt)   
	 */  
	@Override
	public MQConsumeResult consumer(String topic, String tag, MessageExt messageExt) {
		Logger.info("message " + new String(messageExt.getBody()));
		//测试rollback
		if ("订单1 发货".equals(new String(messageExt.getBody()))) {
			return new MQConsumeResult(false, false, false, "", "", null);
		}
		return new MQConsumeResult(true, false, false, "", "", null);
	}
}
