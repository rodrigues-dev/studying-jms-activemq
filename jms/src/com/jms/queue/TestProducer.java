package com.jms.queue;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestProducer {

	/* Revisão da autima aula para fazer:s
	 * Pesquise na documentação do JMS e dê uma olhada nas 
	 * Sub-Interfaces de javax.jms.Destination. Quais são elas?
	 * 
	 * 
	 * Segue também um artigo no blog da Caelum sobre o novo padrão JMS 2.0:

http://blog.caelum.com.br/a-nova-api-do-jms-2-0-no-java-ee-7/
	 */
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("financeiro");
		
		//criando o produtor de mensagens
		MessageProducer producer = (MessageProducer) session.createProducer(fila);
		//cria a mensagem
		Message messager =  session.createTextMessage("<pedido><id>123</id></pedido>");
		//envia a mensagem
		producer.send(messager);
		
		/*
		 * teste de estresse: mandar varias mensagens
		 */
		for (int id = 0; id < 1000; id++) {
			Message messagers =  session.createTextMessage("<pedido><id>"+id+"</id></pedido>");
			producer.send(messagers);
		}
		
		
		
//		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}

}
