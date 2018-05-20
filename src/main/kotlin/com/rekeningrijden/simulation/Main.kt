package com.rekeningrijden.simulation

import com.rekeningrijden.simulation.simulation.CarSimulator
import org.apache.log4j.BasicConfigurator

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        BasicConfigurator.configure()
        println("Starting simulation...")

        val simulator = CarSimulator()
        simulator.startSimulation()
    }
}