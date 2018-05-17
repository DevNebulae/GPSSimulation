package com.rekeningrijden.simulation.simulation

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.AMQP
import com.rekeningrijden.europe.dtos.TransLocationDto
import org.apache.log4j.Logger
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

class MessageProducer {

    private var SimulationToItaly: Gateway? = null
    private var SimulationToGermany: Gateway? = null
    private var SimulationToTheNetherlands: Gateway? = null
    private var SimulationToBelgium: Gateway? = null
    private var SimulationToFinland: Gateway? = null

    private val mapper = ObjectMapper()

    init {
        startSimulationToBelgium()
        startSimulationToFinland()
        startSimulationToGermany()
        startSimulationToItaly()
        startSimulationToTheNetherlands()
    }

    @Throws(Exception::class)
    fun sendTransLocation(countryCode: String, dto: TransLocationDto) {
        val payload = mapper.writeValueAsString(dto)
        val bytePayload = convertPayLoadToBytes(payload)
        val props = AMQP.BasicProperties.Builder().contentType("application/x-java-serialized-object").build()

        when (countryCode) {
            "IT" -> SimulationToItaly!!.channel?.basicPublish("", "SimulationToItaly", props, (bytePayload))
            "DE" -> SimulationToItaly!!.channel?.basicPublish("", "SimulationToGermany", props, (bytePayload))
            "NL" -> SimulationToItaly!!.channel?.basicPublish("", "SimulationToTheNetherlands", props, (bytePayload))
            "BE" -> SimulationToItaly!!.channel?.basicPublish("", "SimulationToBelgium", props, (bytePayload))
            "FI" -> SimulationToItaly!!.channel?.basicPublish("", "SimulationToFinland", props, (bytePayload))
            else -> throw Exception()
        }
        logger.debug("($countryCode) Payload has been sent to the queue")
    }

    @Throws(IOException::class)
    private fun convertPayLoadToBytes(payload: String): ByteArray {
        val baos = ByteArrayOutputStream()
        val writter = ObjectOutputStream(baos)
        writter.writeObject(payload)
        return baos.toByteArray()
    }

    private fun startSimulationToItaly() {
        try {
            SimulationToItaly = Gateway()
            SimulationToItaly!!.connect()
            SimulationToItaly!!.channel?.queueDeclare("SimulationToItaly", false, false, false, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun startSimulationToGermany() {
        try {
            SimulationToGermany = Gateway()
            SimulationToGermany!!.connect()
            SimulationToGermany!!.channel?.queueDeclare("SimulationToGermany", false, false, false, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun startSimulationToTheNetherlands() {
        try {
            SimulationToTheNetherlands = Gateway()
            SimulationToTheNetherlands!!.connect()
            SimulationToTheNetherlands!!.channel?.queueDeclare("SimulationToTheNetherlands", false, false, false, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun startSimulationToBelgium() {
        try {
            SimulationToBelgium = Gateway()
            SimulationToBelgium!!.connect()
            SimulationToBelgium!!.channel?.queueDeclare("SimulationToBelgium", false, false, false, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun startSimulationToFinland() {
        try {
            SimulationToFinland = Gateway()
            SimulationToFinland!!.connect()
            SimulationToFinland!!.channel?.queueDeclare("SimulationToFinland", false, false, false, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val logger = Logger.getLogger(MessageProducer::class.java)
    }
}