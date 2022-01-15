package com.borba.jms.jmsfundamentals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/**
 * using JMS 2.0
 * @author rodri
 *
 */
public class RequestReplyDemo {
	
	
	public static void main(String[] args) throws NamingException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			
			jmsContext.createProducer().send(queue, "test sending message");
			
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			String messageReceived = consumer.receiveBody(String.class);
			System.out.println(messageReceived);
			
			JMSProducer replyProducer = jmsContext.createProducer();
			replyProducer.send(replyQueue, "reply message");
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			String messageReceivedReply = replyConsumer.receiveBody(String.class);
			System.out.println(messageReceivedReply);
			
		}
	}

}
