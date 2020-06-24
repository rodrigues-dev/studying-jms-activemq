package com.jms.queue.acknowledge;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestProducer {

	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection("user", "password");
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("financeiro");
		
		MessageProducer producer = (MessageProducer) session.createProducer(fila);
		/*
		 * mandando uma mensagem para testar a confirmação do recebimento (acknowledge)
		 */
		Message messagers =  session.createTextMessage("<pedido><id>123</id></pedido>");
		producer.send(messagers);
		
		session.close();
		connection.close();
		context.close();
	}

}
