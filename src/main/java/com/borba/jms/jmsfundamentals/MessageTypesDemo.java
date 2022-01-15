package com.borba.jms.jmsfundamentals;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {


		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			TemporaryQueue bytesQueueTemp = jmsContext.createTemporaryQueue();
			
			JMSProducer producer = jmsContext.createProducer();
			
			BytesMessage bytesMessage = jmsContext.createBytesMessage();
			bytesMessage.writeUTF("John");
			bytesMessage.writeLong(123l);
			
			producer.send(bytesQueueTemp, bytesMessage);
			
			BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(bytesQueueTemp).receive(5000);
			System.err.println("bytes message");
			System.out.println(messageReceived.readUTF());
			System.out.println(messageReceived.readLong());
			
			TemporaryQueue streamsQueueTemp = jmsContext.createTemporaryQueue();
			
			JMSProducer streamProducer = jmsContext.createProducer();
			
			StreamMessage streamMessage = jmsContext.createStreamMessage();
			streamMessage.writeBoolean(true);
			streamMessage.writeFloat(2.5f);
			
			streamProducer.send(streamsQueueTemp, streamMessage);
			
			StreamMessage streamMessageReceived = (StreamMessage) jmsContext.createConsumer(streamsQueueTemp).receive(5000);
			System.out.println("stream messages");
			System.out.println(streamMessageReceived.readBoolean());
			System.out.println(streamMessageReceived.readFloat());
			
			MapMessage mapMessage = jmsContext.createMapMessage();
			mapMessage.setBoolean("isCreditAvailable", true);
			
			TemporaryQueue mapQueueTemp = jmsContext.createTemporaryQueue();
			
			JMSProducer mapProducer = jmsContext.createProducer();
			
			mapProducer.send(mapQueueTemp, mapMessage);
			
			MapMessage mapMessageReceived = (MapMessage) jmsContext.createConsumer(mapQueueTemp).receive(5000);
			System.out.println(mapMessageReceived.getBoolean("isCreditAvailable"));


		}

	}

}
