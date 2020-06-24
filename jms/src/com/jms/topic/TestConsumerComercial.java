package com.jms.topic;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestConsumerComercial {
	/*
	 * Um topico não guarda a mensagem para sistemas que não estão online.
	 * Ele manda para todos os sistemas que estão online, ou guarda apenas para os 
	 * sistemas que estão registrados para receberem mesmo se não estiverem online no momento.
	 */
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		//registra um cliente que informa ao topico que esse consumer deve receber as mensagens mesmo que não esteja online no momento.
		connection.setClientID("comercial");
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		//assinatura do cliente duravel para poder receber as msg
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
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
