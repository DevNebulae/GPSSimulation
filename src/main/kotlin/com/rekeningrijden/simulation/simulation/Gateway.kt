package com.rekeningrijden.simulation.simulation

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.slf4j.LoggerFactory

class Gateway {
    private val factory: ConnectionFactory
    var connection: Connection? = null
    var channel: Channel? = null

    constructor(host: String) {
        factory = ConnectionFactory()
        factory.host = host
    }

    fun connect() {
        try {
            connection = factory.newConnection()
            channel = connection?.createChannel()
        } catch (e: Exception) {
            logger.error("Connection with the message queue could not be established.")
            logger.error(e.toString())
            System.exit(1)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Gateway::class.java)
    }
}
