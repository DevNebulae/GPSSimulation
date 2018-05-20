package com.rekeningrijden.simulation.runners

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rekeningrijden.simulation.entities.Car
import com.rekeningrijden.simulation.services.CarService
import com.rekeningrijden.simulation.simulation.CarSimulator
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
@Order(1)
class CarCommandLineRunner : CommandLineRunner {
    @Autowired
    private lateinit var carService: CarService

    override fun run(vararg args: String?) {
        carService.save(ObjectMapper().readValue<Set<Car>>(ClassPathResource("trackers.json").file))

        logger.info("${carService.cars.size} cars have been initialized")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CarCommandLineRunner::class.java)
    }
}
