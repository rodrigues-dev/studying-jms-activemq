package com.jms.queue.acknowledge;

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
		/* A 'connection.createSession()' define 4 formas de confirmação do recebimento da msg.
		 * 1 - (false, Session.AUTO_ACKNOWLEDGE): confirma o recebimento automaticamente.
		 * 2 - (false, Session.CLIENT_ACKNOWLEDGE): o client precisa informar o recebimento da msg (message.acknowledge())
		 * 3 - (true, Session.SESSION_TRANSACTED): possui um comportamento transacional, onde confirmamos o
		 * 		recebimento da mensagem (session.commit()) e se algo der errado podemos desconfirmar (session.rollback()).
		 *      Também é usado para, na mesma transação, salvar em um banco de dados a msg.
		 * 		Obs: esse é o método de confirmação mais usado.
		 * 
		 * 4 - (true, Session.DUPS_OK_ACKNOWLEDGE): 
		 */
		Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
		Destination fila = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(fila);
		consumer.setMessageListener( message -> {
			TextMessage textMessage = (TextMessage) message;
			try {
				if ("<pedido><id>123</id></pedido>".equals(textMessage.getText()))
					//exemplo de desconfirmação.
					session.rollback();
				else
					/*
					 * Confirmando o recebimento da mensagem
					 */
					session.commit();
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
