package com.zcx.rocketmq.producer.config;


import com.zcx.rocket.base.MQSendResult;
import com.zcx.rocket.base.TopicEnum;

/**
 * 
 * @ClassName:  ProducerService   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zcx
 * @date:   2019年3月22日 下午10:51:50   
 *
 */
public interface ProducerService {

    /**
     * 校验发送消息内容
     *
     * @param topic
     * @param tag
     * @param msg
     */
    void validateSendMsg(TopicEnum topic, String tag, String msg);

    /**
     * 发送消息
     *
     * @param topic
     * @param tag
     * @param keys
     * @param msg
     * @return
     */
    MQSendResult send(TopicEnum topic, String tag, String keys, String msg);

    /**
     * 发送延时的消息
     *
     * @param topic
     * @param tag
     * @param keys
     * @param msg
     * @param delayTimeLevel
     * @return
     */
    MQSendResult send(TopicEnum topic, String tag, String keys, String msg, Integer delayTimeLevel);
    
    /**
     * 有序发送消息
     *
     * @param topic
     * @param tag
     * @param msg
     */
    MQSendResult sendByOrder(TopicEnum topic, String tag, String keys, String msg, Long orderId);
    
    /**
     * 发送事务消息
     *
     * @param topic
     * @param tag
     * @param msg
     */
    MQSendResult sendByTransaction(TopicEnum topic, String tag, String keys, String msg, Long orderId);
}
