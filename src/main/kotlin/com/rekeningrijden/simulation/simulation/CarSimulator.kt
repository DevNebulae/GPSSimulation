package com.rekeningrijden.simulation.simulation

import com.rekeningrijden.simulation.car.Car
import com.rekeningrijden.simulation.entities.Journey
import com.rekeningrijden.simulation.route.Route
import java.util.Random
import kotlin.properties.Delegates

class CarSimulator {
    private var cars by Delegates.notNull<Set<Car>>()
    private var journeys by Delegates.notNull<List<Journey>>()
    private var routes by Delegates.notNull<List<Route>>()
    private val rndm = Random()
    private val messageProducer = MessageProducer()

    fun initialize(cars: Set<Car>, routes: List<Route>) {
        this.cars = cars
        this.routes = routes
        this.journeys = cars.map { Journey(this, messageProducer, it, routes[rndm.nextInt(routes.size)]) }
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
        for (t in journeys) {
            t.start()
            Thread.sleep(((rndm.nextInt(10) + 1) * 1000).toLong())
        }
    }
}
