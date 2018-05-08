package com.rekeningrijden.simulation.Simulation;

import com.rekeningrijden.europe.dtos.TransLocationDto;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageProducer {
    private final static Logger logger = Logger.getLogger(MessageProducer.class);

    private Gateway SimulationToItaly;
    private Gateway SimulationToGermany;
    private Gateway SimulationToTheNetherlands;
    private Gateway SimulationToBelgium;
    private Gateway SimulationToFinland;

    public MessageProducer(){
        startSimulationToBelgium();
        startSimulationToFinland();
        startSimulationToGermany();
        startSimulationToItaly();
        startSimulationToTheNetherlands();
    }

    public void sendTransLocation(String countryCode, TransLocationDto dto) throws Exception {
        JSONObject jsonObj = new JSONObject(dto);

        switch (countryCode){
            case "IT":
                SimulationToItaly.channel.basicPublish("", "SimulationToItaly", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "DE":
                SimulationToItaly.channel.basicPublish("", "SimulationToGermany", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "NL":
                SimulationToItaly.channel.basicPublish("", "SimulationToTheNetherlands", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "BE":
                SimulationToItaly.channel.basicPublish("", "SimulationToBelgium", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            case "FI":
                SimulationToItaly.channel.basicPublish("", "SimulationToFinland", null, convertPayLoadToBytes(jsonObj.toString()));
                break;
            default:
                throw new Exception();
        }
        logger.debug("(" + countryCode + ") Payload has been sent to the queue");
    }

    private byte[] convertPayLoadToBytes(String payload) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream writter = new ObjectOutputStream(baos);
        writter.writeObject(payload);
        return baos.toByteArray();
    }

    private void startSimulationToItaly(){
        try {
            SimulationToItaly = new Gateway();
            SimulationToItaly.connect();
            SimulationToItaly.channel.queueDeclare("SimulationToItaly", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToGermany(){
        try {
            SimulationToGermany = new Gateway();
            SimulationToGermany.connect();
            SimulationToGermany.channel.queueDeclare("SimulationToGermany", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToTheNetherlands(){
        try {
            SimulationToTheNetherlands = new Gateway();
            SimulationToTheNetherlands.connect();
            SimulationToTheNetherlands.channel.queueDeclare("SimulationToTheNetherlands", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToBelgium(){
        try {
            SimulationToBelgium = new Gateway();
            SimulationToBelgium.connect();
            SimulationToBelgium.channel.queueDeclare("SimulationToBelgium", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSimulationToFinland(){
        try {
            SimulationToFinland = new Gateway();
            SimulationToFinland.connect();
            SimulationToFinland.channel.queueDeclare("SimulationToFinland", false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
