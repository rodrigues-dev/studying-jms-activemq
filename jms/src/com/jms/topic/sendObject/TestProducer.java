package com.jms.topic.sendObject;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jms.modelo.Pedido;
import com.jms.modelo.PedidoFactory;

public class TestProducer {
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection("user","password");
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		MessageProducer producer = (MessageProducer) session.createProducer(topico);
		
		/*
		 * criando o objeto pedido
		 */
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		/*
		 * Enviando os dados do objeto serializado em um fluxo de bits na mensagem.
		 * Assim, evita converter de xml para objeto e vice versa.
		 * Usar desta forma apenas quando a comunicação for entre sistemas java.
		 */
		Message messager =  session.createObjectMessage(pedido);
		
		producer.send(messager);
		
		session.close();
		connection.close();
		context.close();
	}

}
