package com.zcx.rocketmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zcx.rocket.base.TopicEnum;
import com.zcx.rocketmq.producer.config.ProducerService;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {
	@Autowired 
	ProducerService producerService;
	
	/**
	 * 顺序消费
	 * @Title: testOrder   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	@Test
	public void testOrder() {
		/*producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单1 创建", 1L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单2 创建", 2L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单3 创建", 3L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单4 创建", 4L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单5 创建", 5L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单1 付款", 1L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单2 付款", 2L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单3 付款", 3L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单4 付款", 4L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单5 付款", 5L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单1 发货", 1L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单2 发货", 2L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单3 发货", 3L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单4 发货", 4L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单5 发货", 5L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单1 完成", 1L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单2 完成", 2L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单3 完成", 3L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单4 完成", 4L);
		producerService.sendByOrder(TopicEnum.Topic1, "tag1", "tag1", "订单5 完成", 5L);*/
	}
	
	
	/**
	 * 顺序消费
	 * @Title: testOrder   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	@Test
	public void testTransaction() {
		producerService.sendByTransaction(TopicEnum.Topic2, "tag1", "tag1", "订单1 创建", 1L);
		/*producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单2 创建", 2L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单3 创建", 3L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单4 创建", 4L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单5 创建", 5L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单1 付款", 1L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单2 付款", 2L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单3 付款", 3L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单4 付款", 4L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单5 付款", 5L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单1 发货", 1L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单2 发货", 2L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单3 发货", 3L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单4 发货", 4L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单5 发货", 5L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单1 完成", 1L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单2 完成", 2L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单3 完成", 3L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单4 完成", 4L);
		producerService.sendByTransaction(TopicEnum.Topic1, "tag1", "tag1", "订单5 完成", 5L);*/
	}
	
	/**
	 * 
	 * 随机消费
	 * @Title: test   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	@Test
	public void test() {
		/*producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单1 创建");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单2 创建");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单3 创建");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单4 创建");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单5 创建");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单1 付款");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单2 付款");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单3 付款");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单4 付款");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单5 付款");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单1 发货");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单2 发货");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单3 发货");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单4 发货");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单5 发货");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单1 完成");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单2 完成");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单3 完成");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单4 完成");
		producerService.send(TopicEnum.Topic1, "tag1", "tag1", "订单5 完成");*/
	}
}