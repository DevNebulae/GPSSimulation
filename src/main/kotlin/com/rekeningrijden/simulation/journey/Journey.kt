package com.rekeningrijden.simulation.journey

import com.rekeningrijden.europe.dtos.TransLocationDto
import com.rekeningrijden.simulation.car.Car
import com.rekeningrijden.simulation.route.Route
import com.rekeningrijden.simulation.route.SubRoute
import com.rekeningrijden.simulation.simulation.CarSimulator
import com.rekeningrijden.simulation.simulation.MessageProducer
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
    private lateinit var carSimulator: CarSimulator

    @Autowired
    private lateinit var messageProducer: MessageProducer

    private lateinit var car: Car
    private lateinit var route: Route

    fun initialize(car: Car, route: Route) {
        this.car = car
        this.route = route
    }

    override fun run() {
        while (!route.isRouteDriven) {
            val sr = findSubRouteThatIsNotDrivenYet()
            val iterator = sr.coordinates.iterator()

            while (iterator.hasNext()) {
                val coor = iterator.next()

                val dto = TransLocationDto(
                    coor.latitude.toString(),
                    coor.longitude.toString(),
                    Instant.now().toString(),
                    car.id.toString(),
                    car.country
                )
                messageProducer.sendTransLocation(sr.countryCode, dto)
                logger.debug("Lat: " + coor.latitude + " - Lon: " + coor.longitude)

                Thread.sleep(1000)
            }

            if (route.isRouteDriven) {
                logger.debug("Thread sleeping for 15 minutes")
                TimeUnit.MINUTES.sleep(15)
                this.route = carSimulator.newRoute
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
