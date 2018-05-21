package com.rekeningrijden.simulation.simulation

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.AMQP
import com.rekeningrijden.europe.dtos.TransLocationDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

@Service
class MessageProducer {
    private val mapper = ObjectMapper()
    private val gateways = mutableMapOf<String, Gateway>()

    init {
        val countries = arrayOf("BE", "DE", "FI", "IT", "NL")

        countries.forEach {
            val host = System.getProperty(createHostProperty(it))

            if (host == null)
                logger.error("Could not connect with the $it queue. If this is intended, please ignore this message. Otherwise, add the queue via a command line property:\n    -D${createQueueName(it)}=<ip-address>")
            else
                gateways[it] = createGateway(it, host)
        }

        if (gateways.count() == 0) {
            System.exit(1)
            logger.error("There are no RabbitMQ instances to be connected to. Please supply at least one valid URL.")
        }
    }

    fun sendTransLocation(countryCode: String, dto: TransLocationDto) {
        val payload = mapper.writeValueAsString(dto)
        val bytePayload = convertPayLoadToBytes(payload)

        /**
         * The message will be marked with delivery mode 2, this means that the
         * message will be persisted on disk by RabbitMQ so that it will not be
         * lost.
         */
        val props = AMQP.BasicProperties
            .Builder()
            .contentType("application/x-java-serialized-object")
            .deliveryMode(2)
            .build()

        gateways.get(countryCode)?.channel?.basicPublish("", createQueueName(countryCode), props, bytePayload)
    }

    private fun convertPayLoadToBytes(payload: String): ByteArray {
        val baos = ByteArrayOutputStream()
        val writter = ObjectOutputStream(baos)
        writter.writeObject(payload)
        return baos.toByteArray()
    }

    private fun createGateway(countryCode: String, host: String): Gateway {
        val gateway = Gateway(host)

        /**
         * The queue will be marked as durable to ensure that it is not lost
         * when the queue is temporarily taken down for maintenance. To delete
         * a queue created by the simulation system, please remove it by hand.
         */
        gateway.connect()
        gateway.channel?.queueDeclare(createQueueName(countryCode), true, false, false, null)

        return gateway
    }

    fun createHostProperty(countryCode: String): String = "simulation.rabbitmq.${countryCode.toLowerCase()}"

    fun createQueueName(countryCode: String): String = "rekeningrijden.simulation.${countryCode.toLowerCase()}"

    companion object {
        private val logger = LoggerFactory.getLogger(MessageProducer::class.java)
    }
}
