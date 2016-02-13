package com.mamewo.storm.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.Properties;

public class AMQProducer {
    public static void main(String argv[])
	throws Exception
    {
	Context context = null;
	Connection connection = null;
        try {
	    //write config to map?
	    Properties prop = new Properties();
	    prop.setProperty("java.naming.factory.initial", "org.apache.qpid.jndi.PropertiesFileInitialContextFactory");
	    prop.setProperty("connectionfactory.qpidConnectionfactory", "amqp://guest:guest@clientid/?brokerlist='tcp://localhost:5672'");
	    prop.setProperty("destination.topicExchange", "amq.topic");
	    context = new InitialContext(prop);
	    ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionfactory");
	    connection = connectionFactory.createConnection();
	    connection.start();
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    Destination destination = (Destination) context.lookup("topicExchange");

            MessageProducer messageProducer = session.createProducer(destination);

	    int i = 0;
	    while(true){
		String text = "hgeohgeo ghaoehg" + (i++);

		TextMessage message = session.createTextMessage(text);
		messageProducer.send(message);
	    }
        } catch (Exception e) {
            e.printStackTrace();
        }
	finally {
	    connection.close();
	    context.close();
	}
    }
}
