package com.borba.jms.jmsfundamentals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/**
 * using JMS 2.0
 * @author rodri
 *
 */
public class RequestReplyDemoTemporaryQueue {
	
	
	public static void main(String[] args) throws NamingException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			
			TemporaryQueue replyQueueTemp = jmsContext.createTemporaryQueue();
			
			TextMessage message = jmsContext.createTextMessage("test sending message");
			message.setJMSReplyTo(replyQueueTemp);
			producer.send(queue, message);
			
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			System.out.println(messageReceived.getText());
			
			/**
			 * 
			 */
			
			JMSProducer replyProducer = jmsContext.createProducer();
			replyProducer.send(messageReceived.getJMSReplyTo(), "reply message");
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueueTemp);
			
			String messageReceivedReply = replyConsumer.receiveBody(String.class);
			System.out.println(messageReceivedReply);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
