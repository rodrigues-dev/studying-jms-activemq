package com.jms.topic.sendXml;

import java.io.StringReader;
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
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.jms.modelo.Pedido;

public class TestConsumerEstoque {
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection("user","password");
		connection.setClientID("estoque");
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector", "ebook is null OR ebook=false", false);
		
		consumer.setMessageListener( message -> {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("Recebendo msg: " + textMessage.getText());
				/*
				 * Convertendo o xml recebido no objeto
				 */
				Pedido pedido = xmlToPedido(textMessage.getText());
				//testando a conversão
				System.out.println("Objeto:\n"
						+ "Codigo: " + pedido.getCodigo()
						+ "\nValor Total: " + pedido.getValorTotal());
				
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});
		
		
		new Scanner(System.in).nextLine();
		
		connection.close();
		context.close();
	}

	private static Pedido xmlToPedido(String xml)  {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Pedido.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		try {
			Unmarshaller converter = context.createUnmarshaller();
			return (Pedido) converter.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
