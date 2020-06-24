package com.jms.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
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
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//configurar o topico
		Topic topico = (Topic) context.lookup("loja");
		
		MessageProducer producer = (MessageProducer) session.createProducer(topico);
		Message messager =  session.createTextMessage("<pedido><id>123</id></pedido>");
		//enviando duas msg
		producer.send(messager);
		producer.send(messager);
		
		session.close();
		connection.close();
		context.close();
	}

}
