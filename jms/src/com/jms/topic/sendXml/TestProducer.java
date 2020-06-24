package com.jms.topic.sendXml;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;

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
		 * Cria o objeto devidamente anotado com o JAXB para fazer a marshal e unmarshal
		 */
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		// Escreve em memoria o xml
		StringWriter writer = new StringWriter();
		//converte o objeto em uma String em memoria com o xml baseado no objeto
		JAXB.marshal(pedido, writer);
		//Armazena o xml em uma variavel
		String xml = writer.toString();
		
		System.out.println(xml);
		Message messager =  session.createTextMessage(xml);
		
		producer.send(messager);
		
		session.close();
		connection.close();
		context.close();
	}

}
