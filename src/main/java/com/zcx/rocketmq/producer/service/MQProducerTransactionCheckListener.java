package com.zcx.rocketmq.producer.service;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MQProducerTransactionCheckListener implements TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(MQProducerTransactionCheckListener.class);
    
    private ConcurrentHashMap<String, Integer> countHashMap = new ConcurrentHashMap<>();
	
	private final static int MAX_COUNT = 5;
	/**   
	 * <p>Title: checkLocalTransaction</p>   
	 * <p>Description: </p>   
	 * @param arg0
	 * @return   
	 * @see org.apache.rocketmq.client.producer.TransactionListener#checkLocalTransaction(org.apache.rocketmq.common.message.MessageExt)   
	 */  
	@Override
	public LocalTransactionState checkLocalTransaction(MessageExt arg0) {
		Integer status = 0;
        // 从数据库查查询t_message_transaction表，如果该表中存在记录，则提交，
        String bizUniNo = arg0.getUserProperty("bizUniNo"); // 从消息中获取业务唯一ID。
        // 然后t_message_transaction 表，是否存在bizUniNo，如果存在，则返回COMMIT_MESSAGE，
        // 不存在，则记录查询次数，未超过次数，返回UNKNOW，超过次数，返回ROLLBACK_MESSAGE
        if(query(bizUniNo) > 0 ) {
        	return LocalTransactionState.COMMIT_MESSAGE;
        }
        return rollBackOrUnown(bizUniNo);
	}
	
	public int query(String bizUniNo) {
    	return 1; //select count(1) from t_message_transaction a where a.biz_uni_no=#{bizUniNo}
    }
    
    public LocalTransactionState rollBackOrUnown(String bizUniNo) {
    	Integer num = countHashMap.get(bizUniNo);
    	
    	if(num != null &&  ++num > MAX_COUNT) {
    		countHashMap.remove(bizUniNo);
    		return LocalTransactionState.ROLLBACK_MESSAGE;
    	}
    	
    	if(num == null) {
    		num = new Integer(1);
    	}
    	
    	countHashMap.put(bizUniNo, num);
    	return LocalTransactionState.UNKNOW;
    	
    }

	/**   
	 * <p>Title: executeLocalTransaction</p>   
	 * <p>Description: </p>   
	 * @param arg0
	 * @param arg1
	 * @return   
	 * @see org.apache.rocketmq.client.producer.TransactionListener#executeLocalTransaction(org.apache.rocketmq.common.message.Message, java.lang.Object)   
	 */  
	@Override
	public LocalTransactionState executeLocalTransaction(Message arg0, Object arg1) {
		 // 
    	String bizUniNo = arg0.getUserProperty("bizUniNo"); // 从消息中获取业务唯一ID。
    	// 将bizUniNo入库，表名：t_message_transaction,表结构  bizUniNo(主键),业务类型。
        return LocalTransactionState.COMMIT_MESSAGE;
	}
}
