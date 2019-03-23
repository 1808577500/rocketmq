package com.zcx.rocketmq.consumer.config;


import org.apache.rocketmq.common.message.MessageExt;

import com.zcx.rocket.base.MQConsumeResult;

/**
 * 消费者
 * @ClassName:  MQConsumerPrcessor   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zcx
 * @date:   2019年3月22日 下午10:39:57   
 *
 */
public abstract class MQConsumerPrcessor implements MQMsgProcessor{
	
	public MQConsumeResult hadleMsg(String topic, String tag, MessageExt msgs) {
		return consumer(topic,tag, msgs);
	}
	
	public abstract MQConsumeResult consumer(String topic, String tag, MessageExt messageExt);
}
