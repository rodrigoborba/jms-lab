package com.borba.jms.basic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {
	
	public static void main(String[] args) {
			
			InitialContext initialContext = null;
			Connection connection = null;
			
			try {
				
				initialContext = new InitialContext();
				Topic topic = (Topic) initialContext.lookup("topic/myTopic");
				
				ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
				connection = cf.createConnection();
				Session session = connection.createSession();
				
				MessageProducer producer = session.createProducer(topic);
				
				MessageConsumer consumer1 = session.createConsumer(topic);
				MessageConsumer consumer2 = session.createConsumer(topic);
				
				TextMessage message = session.createTextMessage("Test message");
				
				producer.send(message);
				
				connection.start();
				
				TextMessage message1 = (TextMessage) consumer1.receive();
				
				System.out.println("Consumer 1 message received: " + message1.getText());
				
				TextMessage message2 = (TextMessage) consumer2.receive();
				
				System.out.println("Consumer 2 message received: " + message2.getText());
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				
				if(null != initialContext) {
					try {
						initialContext.close();
					} catch (NamingException e2) {
						e2.printStackTrace();
					}
				}
				
				if(null != connection) {
					try {
						connection.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
	}

}
