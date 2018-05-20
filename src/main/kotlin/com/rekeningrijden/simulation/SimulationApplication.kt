package com.rekeningrijden.simulation

import com.rekeningrijden.simulation.services.CarService
import com.rekeningrijden.simulation.services.RouteService
import com.rekeningrijden.simulation.simulation.CarSimulator
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SimulationApplication {
    @Bean
    fun start(carService: CarService, routeService: RouteService) = CommandLineRunner {
        println("Starting simulation...")

        val simulator = CarSimulator()
        simulator.initialize(carService.cars, routeService.routes)
        simulator.startSimulation()
    }
}

fun main(args: Array<String>) {
    runApplication<SimulationApplication>(*args)
}
