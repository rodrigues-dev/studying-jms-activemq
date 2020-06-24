package com.jms.queue;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestConsumer {

	/* Revisão da autima aula para fazer:s
	 * Pesquise na documentação do JMS e dê uma olhada nas 
	 * Sub-Interfaces de javax.jms.Destination. Quais são elas?
	 * 
	 * 
	 * Segue também um artigo no blog da Caelum sobre o novo padrão JMS 2.0:

http://blog.caelum.com.br/a-nova-api-do-jms-2-0-no-java-ee-7/
	 */
	
	public static void main(String[] args) throws JMSException, NamingException {

		//Java Naming e Diretory Service (JNDI)
		InitialContext context = new InitialContext();
		//O nome do objeto que foi utilizado na hora de subir o activeMQ. Essa informação é dada pelo MOM
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		//Conecção com o sistema de mensageiria
		Connection connection = factory.createConnection();
		//inicializando a 
		connection.start();
		
		//Session: abstrai o trabalho transacional e o recebimento das mensagens
		//primeiro parametro: se eu quero ou não uma transação.
		//segundo parametro: auto confirmação das mensagens
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//pega no jndi.properties o nome da fila que iremos consumir
		Destination fila = (Destination) context.lookup("financeiro");
		//cria o consumidor para umaa fila específica dentro do MOM
		MessageConsumer consumer = session.createConsumer(fila);
		
		//recebo uma unica mensagem postadas na fila
		//Message message = consumer.receive();
		/* setMessageListener: fica "ouvindo" todas as mensagens enviadas para a fila.
		 * e segue o padrão Messaging Patterns do EIP:
		 * Enterprise Integration Pattern: https://www.enterpriseintegrationpatterns.com/patterns/messaging/ObserverJmsExample.html
		 */
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
