package com.borba.jms.claimmanagement;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ClaimManagementFilterObject {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/claimQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();

			JMSConsumer consumer = jmsContext.createConsumer(requestQueue, " doctorType IN ('neuro','psych') "
					+ " OR JMSPriority BETWEEN 4 AND 9");

			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			objectMessage.setStringProperty("doctorType", "gyna");
			Claim claim = new Claim();
			claim.setHospitalId(1);
			claim.setClaimAmount(1000);
			claim.setDoctorName("John");
			claim.setDoctorType("neuro");
			claim.setInsuranceProvider("blue cross");
			objectMessage.setObject(claim);

			producer.send(requestQueue, objectMessage);

			Claim receiveBody = consumer.receiveBody(Claim.class, 5000);
			System.out.println(receiveBody.getClaimAmount());
		}
	}

}
