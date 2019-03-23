package com.zcx.rocketmq.consumer.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.zcx.rocket.base.TopicEnum;

/**
 * 消费者 topic tag
 * @author zcxpc
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)  
public @interface ConsumerAnnotation {
	TopicEnum topic();
	
	String[] tag();
}
