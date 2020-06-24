package com.jms.topic.sendObject;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.jms.modelo.Pedido;

public class TestConsumerEstoque {
	
	public static void main(String[] args) throws JMSException, NamingException {

		InitialContext context = new InitialContext();
		ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory");
		/*
		 * Mecanismo de configuração para evitar codigo malicioso usanod o ObjectMessage.
		 * A partir da versão 2.12 do ActiveMQ foi introduzido o método: factory.setTrustedPackages(trustedPackages) o qual
		 * informamos quais os pacotes de classes são confiáveis para deserializar.
		 * E o factory.setTrustAllPackages(true) que informa que todos os pacotes são seguros.
		 */
		factory.setTrustAllPackages(true);
		// Ou usando o System:
//		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,br.com.caelum.modelo");
		
		Connection connection = factory.createConnection("user","password");
		connection.setClientID("estoque");
		
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");
		
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector", "ebook is null OR ebook=false", false);
		
		consumer.setMessageListener( message -> {
			/*
			 * recebe os dados do objeto serializado
			 */
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				// deserializa os dados para um objeto Pedido
				Pedido pedido = (Pedido) objectMessage.getObject();
				// testando a desserialização
				System.out.println("Codigo do pedido: " + pedido.getCodigo());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		
		new Scanner(System.in).nextLine();
		
		connection.close();
		context.close();
	}

}
