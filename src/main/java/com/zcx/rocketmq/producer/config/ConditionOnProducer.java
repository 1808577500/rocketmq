package com.zcx.rocketmq.producer.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import com.zcx.rocket.base.SwitchEnum;


/**
 * 
 * @ClassName:  ConditionOnProducer   
 * @Description:判断是否启动  
 * @author: zcx
 * @date:   2019年3月22日 下午10:49:28   
 *
 */
public class ConditionOnProducer implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //判断当前环境开关是否开启
        String isOnOff = context.getEnvironment().getProperty("rocketmq.producer.isOnOff");
        //当且仅当值为on时，返回true
        if (!StringUtils.isEmpty(isOnOff) && isOnOff.equalsIgnoreCase(SwitchEnum.on.getCode())) {
            return true;
        }
        return false;
    }
}
