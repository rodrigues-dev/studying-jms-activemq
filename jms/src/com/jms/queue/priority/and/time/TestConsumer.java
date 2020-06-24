package com.jms.queue.priority.and.time;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestConsumer {

	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection("user", "password");
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("LOG");
		MessageConsumer consumer = session.createConsumer(fila);
		
		consumer.setMessageListener( message -> {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("Recebendo msg: " + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});
		
		
		new Scanner(System.in).nextLine();
		
		connection.close();
		context.close();
	}

}
