package com.rekeningrijden.simulation.simulation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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

class CarSimulator {
    private val cars = mutableListOf<Car>()
    private var routes = mutableListOf<Route>()
    private val journeys = mutableListOf<Journey>()
    private val rndm = Random()
    private val messageProducer = MessageProducer()

    val newRoute: Route
        get() {
            val rndmRouteIndex = rndm.nextInt(routes.size)
            val route = routes[rndmRouteIndex]
            route.isRouteDriven = false
            route.setAllSubRoutesToFalse()
            return route
        }

    init {
        loadCarsFromJson()
        loadRoutesFromGPX()
        createJourneys()
    }

    private fun loadCarsFromJson() {
        val readValue = ObjectMapper().readValue<List<Car>>(resolver.getResource("classpath:trackers.json").inputStream)
        cars.addAll(readValue)

        logger.debug("${cars.size} cars have been initialized.")
    }

    private fun loadRoutesFromGPX() {
        val intermediateRoutes = HashMap<String, MutableList<SubRoute>>()

        val routesRoot = resolver.getResource(routesFolder)
        val routesRootPath = routesRoot.uri
        val subrouteResources = resolver.getResources("classpath*:/routes/**/*.gpx")

        Arrays.stream(subrouteResources)
            /**
             * Walk through all files found (recursively) in the routes resource
             * folder. Relativize the file name so that the route name can be
             * determined together with the the part of the route.
             */
            .forEach { subrouteResource ->
                /**
                 * Retrieve the country code by regex from the file path. An
                 * example of such file path is as follows:
                 * `parma-udine/A-IT.gpx`.
                 *
                 * Capture groups:
                 * 0: complete string
                 * 1: route name
                 * 2: subroute counter (alphabetical)
                 * 3: country code via the ISO specification, two
                 * capitalized alphabetical characters.
                 */
                val pattern = Pattern.compile("([a-zA-Z-]+)/([A-Z]+)-([A-Z]{2})")
                val matcher = pattern.matcher(routesRootPath.relativize(subrouteResource.uri).toString())

                if (!matcher.find()) {
                    logger.warn("The convention determined by the regex could not be found for the file with the location $subrouteResource")
                }

                val routeName = matcher.group(1)
                val countryCode = matcher.group(3)
                val subroute = SubRoute(countryCode)

                /**
                 * Read the GPX resource and map it to a coordinate.
                 */
                val tracks = GPX.read(subrouteResource.inputStream)
                val coordinates = tracks
                    .tracks()
                    .flatMap(Track::segments)
                    .flatMap(TrackSegment::points)
                    .map { Coordinate(it.latitude.toDouble(), it.longitude.toDouble()) }
                    .collect(Collectors.toList())

                subroute.coordinates.addAll(coordinates)

                /**
                 * If the route does not exist yet in the map, create it with a
                 * blank list of subroutes.
                 */
                if (!intermediateRoutes.containsKey(routeName))
                    intermediateRoutes.put(routeName, mutableListOf())

                /**
                 * Add the subroutes to the map.
                 */
                intermediateRoutes.getValue(routeName).add(subroute)
            }

        this.routes = intermediateRoutes
            .entries
            .map { entrySet -> Route(entrySet.key, entrySet.value) }
            .toMutableList()
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
