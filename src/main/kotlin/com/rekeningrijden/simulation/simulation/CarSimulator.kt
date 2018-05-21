package com.rekeningrijden.simulation.simulation

import com.rekeningrijden.simulation.car.Car
import com.rekeningrijden.simulation.journey.Journey
import com.rekeningrijden.simulation.route.Route
import com.rekeningrijden.simulation.services.CarService
import com.rekeningrijden.simulation.services.RouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Service
import sun.plugin2.message.Message
import java.util.Random
import kotlin.properties.Delegates

@Service
class CarSimulator {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var carService: CarService

    @Autowired
    private lateinit var routeService: RouteService

    @Autowired
    private lateinit var taskExecutor: TaskExecutor

    private var cars by Delegates.notNull<Set<Car>>()
    private var routes by Delegates.notNull<List<Route>>()
    private val rndm = Random()


    fun initialize() {
        this.cars = carService.cars
        this.routes = routeService.routes
    }

    val newRoute: Route
        get() {
            val rndmRouteIndex = rndm.nextInt(routes.size)
            val route = routes[rndmRouteIndex]
            route.isRouteDriven = false
            route.setAllSubRoutesToFalse()
            return route
        }

    fun startSimulation() {
        cars.forEach {
            val journey = applicationContext.getBean(Journey::class.java)

            journey.initialize(it, routes[rndm.nextInt(routes.size)])
            taskExecutor.execute(journey)

            Thread.sleep(((rndm.nextInt(10) + 1) * 1000).toLong())
        }
    }
}
