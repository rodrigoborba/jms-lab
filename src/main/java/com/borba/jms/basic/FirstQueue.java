package com.borba.jms.basic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {
	
	public static void main(String[] args) {
		
		InitialContext initialContext = null;
		Connection connection = null;
		
		try {
			initialContext = new InitialContext();

			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession();
			Queue queue = (Queue) initialContext.lookup("queue/myQueue");
			
			MessageProducer producer = session.createProducer(queue);
			
			TextMessage message = session.createTextMessage("Jesus is the Lord");
			
			producer.send(message);
			System.out.println("Message sent: " + message.getText());
			
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage messageReceived = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: " + messageReceived.getText());
			
		} catch (NamingException | JMSException e) {
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
