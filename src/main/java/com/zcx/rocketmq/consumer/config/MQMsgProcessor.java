package com.zcx.rocketmq.consumer.config;


import org.apache.rocketmq.common.message.MessageExt;

import com.zcx.rocket.base.MQConsumeResult;

public interface MQMsgProcessor {
	public MQConsumeResult hadleMsg(String topic, String tag, MessageExt messageExt);
}
