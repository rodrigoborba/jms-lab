package com.borba.jms.jmsfundamentals;

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
public class MessagePropertiesDemo {
	
	
	public static void main(String[] args) throws NamingException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			
			JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("test sending message");
			message.setBooleanProperty("loggedIn", true);
			message.setStringProperty("userToken", "abc123");
			
			producer.send(queue, message);
			
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			
			System.out.println(messageReceived.getText());
			System.out.println(messageReceived.getBooleanProperty("loggedIn"));
			System.out.println(messageReceived.getStringProperty("userToken"));
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
