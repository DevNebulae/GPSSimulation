package com.rekeningrijden.simulation.simulation

import com.rekeningrijden.simulation.car.CarImpl
import com.rekeningrijden.simulation.entities.Journey
import com.rekeningrijden.simulation.entities.Route
import java.util.*
import kotlin.properties.Delegates

class CarSimulator {
    private var cars by Delegates.notNull<Set<CarImpl>>()
    private var journeys by Delegates.notNull<List<Journey>>()
    private var routes by Delegates.notNull<List<Route>>()
    private val rndm = Random()
    private val messageProducer = MessageProducer()

    fun initialize(carImpls: Set<CarImpl>, routes: List<Route>) {
        this.cars = carImpls
        this.routes = routes
        this.journeys = carImpls.map { Journey(this, messageProducer, it, routes[rndm.nextInt(routes.size)]) }
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
