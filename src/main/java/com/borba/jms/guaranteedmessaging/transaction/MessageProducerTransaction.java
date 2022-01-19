package com.borba.jms.guaranteedmessaging.transaction;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageProducerTransaction {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

			JMSProducer producer = jmsContext.createProducer();
			producer.send(requestQueue, "Message 1");
			jmsContext.commit();
			
			producer.send(requestQueue, "Message 2");
			jmsContext.rollback();

		}

	}

}
