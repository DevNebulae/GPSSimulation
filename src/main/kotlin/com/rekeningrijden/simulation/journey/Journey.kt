package com.rekeningrijden.simulation.journey

import com.google.common.collect.Iterators
import com.rekeningrijden.europe.dtos.TransLocationDto
import com.rekeningrijden.simulation.car.Car
import com.rekeningrijden.simulation.math.distance
import com.rekeningrijden.simulation.route.Route
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
        while (!isInterrupted) {
            val routeIterator = route.subRoutes.iterator()

            while (routeIterator.hasNext()) {
                val subRoute = routeIterator.next()
                val coordinateIterator = Iterators.peekingIterator(subRoute.coordinates.iterator())

                while (coordinateIterator.hasNext()) {
                    val coordinate = coordinateIterator.next()

                    val dto = TransLocationDto(
                        coordinate.latitude.toString(),
                        coordinate.longitude.toString(),
                        Instant.now().toString(),
                        car.id.toString(),
                        car.country
                    )
                    messageService.sendTransLocation(subRoute.countryCode, dto)
                    logger.debug("Lat: ${coordinate.latitude} - Lon: ${coordinate.longitude}")

                    /**
                     * Determine the delay until the next coordinate is generated.
                     */
                    if (coordinateIterator.hasNext())
                        coordinateIterator.peek().let {
                            val distance = distance(coordinate.latitude, coordinate.longitude, it.latitude, it.longitude)
                            /**
                             * Calculate the sleep time in _milliseconds_. Both the
                             * distance and speed of the car are in _hours_ and
                             * _kilometers_. To avoid errors in the decimal places,
                             * the values are re-calculated to be in _meters_ and
                             * _seconds_.
                             */
                            val delay = (distance * 1000) / (car.speed / 3.6) * 1000
                            Thread.sleep(delay.toLong())
                        }
                }

                // TODO("Add variable delay")
                logger.debug("Thread sleeping for 15 minutes")
                TimeUnit.MINUTES.sleep(15)
                this.route = simulationService.newRoute
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Journey::class.java)
    }
}
