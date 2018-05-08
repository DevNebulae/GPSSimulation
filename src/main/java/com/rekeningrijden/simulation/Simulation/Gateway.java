package com.rekeningrijden.simulation.Simulation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;

public class Gateway {
    private final static Logger logger = Logger.getLogger(Gateway.class);

    private ConnectionFactory factory;
    public Connection connection;
    public Channel channel;

    public Gateway() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void connect() {
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        }
        catch (Exception e) {
            logger.fatal("Connection with the message queue could not be established.");
            System.exit(1);
        }
    }
}
