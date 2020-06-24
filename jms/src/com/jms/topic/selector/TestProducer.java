package com.jms.topic.selector;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestProducer {
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		/*
		 * permissão para acessar o tipoco loja
		 */
		Connection connection = factory.createConnection("user","password");
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		MessageProducer producer = (MessageProducer) session.createProducer(topico);
		// o body não pode ser usado para filtrar mensagens com o selector
		Message messager =  session.createTextMessage("<pedido><id>123</id></pedido>");
		/*
		 * setando uma property para ser filtrada no selector do consumer
		 * 
		 */
//		messager.setBooleanProperty("ebook", false);
		
		producer.send(messager);
		
//		messager.setBooleanProperty("ebook", true);
//		
//		producer.send(messager);
		
		session.close();
		connection.close();
		context.close();
	}

}
