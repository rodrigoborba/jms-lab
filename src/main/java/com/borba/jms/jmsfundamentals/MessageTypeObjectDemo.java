package com.borba.jms.jmsfundamentals;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypeObjectDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			JMSProducer producer = jmsContext.createProducer();
			
			Patient patient = new Patient();
			patient.setId(1);
			patient.setName("Borba");
			
			ObjectMessage message = jmsContext.createObjectMessage();
			message.setObject(patient);
			
			producer.send(queue, message);
			
			ObjectMessage messageReceivedTemp = (ObjectMessage) jmsContext.createConsumer(queue).receive(5000);
			Patient patientReceivedTemp = (Patient) messageReceivedTemp.getObject();
			System.out.println(patientReceivedTemp.getId());
			System.out.println(patientReceivedTemp.getName());
			
			/**
			 * another style
			 */
			
			TemporaryQueue queueTemp = jmsContext.createTemporaryQueue();
			
			producer.send(queueTemp, patient);
			
			Patient patientReceived = (Patient) jmsContext.createConsumer(queueTemp).receiveBody(Patient.class, 5000);
			System.out.println(patientReceived.getId());
			System.out.println(patientReceived.getName());

		}

	}

}
