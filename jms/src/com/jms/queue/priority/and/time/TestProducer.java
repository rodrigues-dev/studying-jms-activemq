package com.jms.queue.priority.and.time;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
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
		Destination fila = (Destination) context.lookup("LOG");
		
		MessageProducer producer = (MessageProducer) session.createProducer(fila);

		Message messagers =  session.createTextMessage("ERROR | PListStore:[/home/victor/eclipse/apache-activemq-5.15.13/data/localhost/tmp_storage] started");
		/* Definimos algumas propriedades no envia da msg:
		 * 1 - A mensagem.
		 * 2 - DeliveryMode.PERSISTENT: salva a mensagem no BD até entregar a mensagem. Obs.: caso o 
		 * 		MOM "caia", ao voltar ele consegue recuperar a msg.
		 * 3 - Prioridade (de 0 até 9): 0 menor prioridade e 9 maior prioridade.
		 * 4 - Tempo de vida (em milisegundos): define o tempo de vida para a mensagem ser entregue, se não 
		 * 		for consumida ela é descartada.
		 */
		producer.send(messagers, DeliveryMode.NON_PERSISTENT, 9, 500000);
		/*
		 * Atenção! para PRIORIDADE funcionar precisamos adicionar a configuração
		 * "<policyEntry queue=">" prioritizedMessages="true"/>" no conf/activemq.xml
		 */
		
		
		session.close();
		connection.close();
		context.close();
	}

}
