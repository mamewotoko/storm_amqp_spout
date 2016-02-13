package com.mamewo.storm.app;

import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Values;
import backtype.storm.tuple.Fields;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
	
import java.util.Properties;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AMQSpout
    extends BaseRichSpout
{
    private SpoutOutputCollector collector_;
    private MessageConsumer consumer_;
    private Context context_;
    private Connection connection_;
	
    private long receiveMsec_ = 5000;
    // private String host_;
    // private int port_;
    // private String topicname_;
	
    // public BaseRichSpout(String host,
    // 					 int port,
    // 					 String topicname)
    // {
    // 	host_ = host;
    // 	port_ = port;
    // 	topicname_ = topicname;
    // }

    public AMQSpout(){
    }
	
    //shold be non-blocking
    @Override
    public void nextTuple(){
	LOG.info("nextTuple enter");
	try{
	    //TextMessage message = (TextMessage)consumer_.receive(receiveMsec_);
	    //block
	    TextMessage message = (TextMessage)consumer_.receive();
	    if(message == null){
		return;
	    }
	    System.out.println(message.getText());
	    Values values = new Values(message.getText());
	    collector_.emit(values);
	}
	catch(Exception e){
	    //e.printStackTrace();
	    LOG.error("nextTuple", e);
	}
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
    {
	collector_ = collector;
	LOG.info("opened");
	try{
	    //write config to map?
	    Properties prop = new Properties();
	    prop.setProperty("java.naming.factory.initial", "org.apache.qpid.jndi.PropertiesFileInitialContextFactory");
	    prop.setProperty("connectionfactory.qpidConnectionfactory", "amqp://guest:guest@clientid/?brokerlist='tcp://localhost:5672'");
	    prop.setProperty("destination.topicExchange", "amq.topic");
	    context_ = new InitialContext(prop);
	    ConnectionFactory connectionFactory = (ConnectionFactory) context_.lookup("qpidConnectionfactory");
	    connection_ = connectionFactory.createConnection();
	    connection_.start();
	    Session session = connection_.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    Destination destination = (Destination) context_.lookup("topicExchange");
		
	    consumer_ = session.createConsumer(destination);
	}
	catch(Exception e){
	    //e.printStackTrace();
	    LOG.error("open", e);
	}
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer){
	declarer.declare(new Fields("text"));
    }

    @Override
    public void close()
    {
	try{
	    connection_.close();
	    context_.close();
	}
	catch(Exception e){
	    //e.printStackTrace();
	    LOG.error("close", e);
	}
    }
	
    @Override
    public void ack(Object msgid){
    }

    @Override
    public void fail(Object msgid){
    }
    
    static final private Logger LOG = LoggerFactory.getLogger(AMQSpout.class);
}
