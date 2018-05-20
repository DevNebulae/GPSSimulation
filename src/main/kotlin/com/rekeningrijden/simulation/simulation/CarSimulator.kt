package com.rekeningrijden.simulation.simulation

import com.rekeningrijden.simulation.entities.Car
import com.rekeningrijden.simulation.entities.Coordinate
import com.rekeningrijden.simulation.entities.Journey
import com.rekeningrijden.simulation.entities.Route
import com.rekeningrijden.simulation.entities.SubRoute
import io.jenetics.jpx.GPX
import io.jenetics.jpx.Track
import io.jenetics.jpx.TrackSegment
import org.slf4j.LoggerFactory
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.Arrays
import java.util.Random
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.properties.Delegates

class CarSimulator {
    private var cars by Delegates.notNull<Set<Car>>()
    private var routes by Delegates.notNull<List<Route>>()
    private val journeys = mutableListOf<Journey>()
    private val rndm = Random()
    private val messageProducer = MessageProducer()

    fun initialize(cars: Set<Car>, routes: List<Route>) {
        this.cars = cars
        this.routes = routes
        createJourneys()
    }

    val newRoute: Route
        get() {
            val rndmRouteIndex = rndm.nextInt(routes.size)
            val route = routes[rndmRouteIndex]
            route.isRouteDriven = false
            route.setAllSubRoutesToFalse()
            return route
        }

    private fun createJourneys() {
        journeys.addAll(cars.map { Journey(this, messageProducer, it, routes[rndm.nextInt(routes.size)]) })
    }

    fun startSimulation() {
        for (t in journeys) {
            t.start()
            Thread.sleep(((rndm.nextInt(10) + 1) * 1000).toLong())
        }
    }

    companion object {
        private val cl = this::class.java.classLoader
        private val logger = LoggerFactory.getLogger(CarSimulator::class.java)
        private val resolver = PathMatchingResourcePatternResolver(cl)
        private const val routesFolder = "routes/"
    }
}
