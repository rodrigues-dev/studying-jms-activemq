package com.jms.queue.dlq;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TestConsumerEstoque {
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory");
		factory.setTrustAllPackages(true);
		
		Connection connection = factory.createConnection("user","password");
		connection.setClientID("estoque");
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		/*
		 * consumindo mensagens com problema na entrega
		 */
		Destination filaDLQ = (Destination) context.lookup("DLQ");
		
		MessageConsumer consumer = session.createConsumer(filaDLQ);
		
		//verificando o que tem na fila DLQ
		consumer.setMessageListener( message -> {
			System.out.println(message);
		});
		
		
		new Scanner(System.in).nextLine();
		
		connection.close();
		context.close();
	}

}
