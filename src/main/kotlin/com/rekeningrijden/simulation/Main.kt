package com.rekeningrijden.simulation

import com.rekeningrijden.simulation.simulation.CarSimulator

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting simulation...")
        val simulator = CarSimulator()
        simulator.startSimulation()
    }
}