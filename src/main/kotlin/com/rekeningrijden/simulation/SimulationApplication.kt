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
    fun start(carService: CarService, carSimulator: CarSimulator, routeService: RouteService) = CommandLineRunner {
        carSimulator.initialize(carService.cars, routeService.routes)
        carSimulator.startSimulation()
    }
}

fun main(args: Array<String>) {
    runApplication<SimulationApplication>(*args)
}
