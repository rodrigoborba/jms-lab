package com.borba.jms.jmsfundamentals;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/**
 * using JMS 2.0
 * @author rodri
 *
 */
public class RequestReplyDemoCorrelationID {
	
	
	public static void main(String[] args) throws NamingException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("test sending message");
			message.setJMSReplyTo(replyQueue);
			producer.send(queue, message);
			System.out.println("message request ID " + message.getJMSMessageID());
			
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			System.out.println(messageReceived.getText());
			
			Map<String, TextMessage> requestMessages = new HashMap<>();
			requestMessages.put(message.getJMSMessageID(), messageReceived);
			
			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("reply message");
			replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
			
			replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			
			TextMessage messageReceivedReply = (TextMessage) replyConsumer.receive();
			System.out.println("reply correlation ID " + messageReceivedReply.getJMSCorrelationID());
			
			System.out.println(requestMessages.get(replyMessage.getJMSCorrelationID()).getText());
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
