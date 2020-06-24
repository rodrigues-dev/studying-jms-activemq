package com.jms.topic.selector;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestConsumerEstoque {
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		/*
		 * Usando autenticação para acessar o ActiveMQ. O user e password foram configurados no
		 * arquivo ActiveMQ/confi/activemq.xml
		 */
		Connection connection = factory.createConnection("user","password");
		connection.setClientID("estoque");
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		/* Selectors: servem para filtrar as mensagens de um topico.
		 * Não podem verificar o corpo da mensagem. Apenas os Headers e Properties.
		 * Atenção! Por padrão o selector não recebe as mensagens que não tem a propriedade do filtro.
		 * "ebook=false": expressão usada para filtrar as mensagens.
		 * Atenção! para receber mensagens não presentes na expressão do selector
		 * ex. de expressões: "ebook=false OR ebook is null" 
		 * O ultimo parametro "noLocal=false" significa que não ira receber mensagens da proria conexão (connection.setClientID("estoque");)
		 */
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector", "ebook is null OR ebook=false", false);
		
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
