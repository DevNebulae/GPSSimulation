package com.rekeningrijden.simulation.journey

import com.google.common.collect.Iterators
import com.rekeningrijden.europe.dtos.TransLocationDto
import com.rekeningrijden.simulation.car.Car
import com.rekeningrijden.simulation.math.distance
import com.rekeningrijden.simulation.route.Route
import com.rekeningrijden.simulation.route.SubRoute
import com.rekeningrijden.simulation.services.SimulationService
import com.rekeningrijden.simulation.services.MessageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.TimeUnit

/**
 * TODO("Separate Journey and threading implementation into separate classes.")
 */
@Component
@Scope("prototype")
class Journey : Thread() {
    @Autowired
    private lateinit var simulationService: SimulationService

    @Autowired
    private lateinit var messageService: MessageService

    private lateinit var car: Car
    private lateinit var route: Route

    fun initialize(car: Car, route: Route) {
        this.car = car
        this.route = route
    }

    override fun run() {
        while (!route.isRouteDriven) {
            val sr = findSubRouteThatIsNotDrivenYet()
            val iterator = Iterators.peekingIterator(sr.coordinates.iterator())

            while (iterator.hasNext()) {
                val coor = iterator.next()

                val dto = TransLocationDto(
                    coor.latitude.toString(),
                    coor.longitude.toString(),
                    Instant.now().toString(),
                    car.id.toString(),
                    car.country
                )
                messageService.sendTransLocation(sr.countryCode, dto)
                logger.debug("Lat: ${coor.latitude} - Lon: ${coor.longitude}")

                /**
                 * Determine the delay until the next coordinate is generated.
                 */
                if (iterator.hasNext())
                    iterator.peek().let {
                        val distance = distance(coor.latitude, coor.longitude, it.latitude, it.longitude)
                        /**
                         * Calculate the sleep time in _milliseconds_. Both the
                         * distance and speed of the car are in _hours_ and
                         * _kilometers_, so the result of the division is in
                         * _hours_.
                         */
                        val delay = distance / car.speed * 3600000
                        Thread.sleep(delay.toLong())
                    }
            }

            if (route.isRouteDriven) {
                logger.debug("Thread sleeping for 15 minutes")
                TimeUnit.MINUTES.sleep(15)
                this.route = simulationService.newRoute
            }
        }
    }

    private fun findSubRouteThatIsNotDrivenYet(): SubRoute {
        val srs = route.subRoutes
        for (sr in srs) {
            if (!sr.isSubRouteDriven) {
                return sr
            }
        }
        route.isRouteDriven = true
        return srs[srs.size - 1]
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Journey::class.java)
    }
}
