package com.rekeningrijden.simulation.runners

import com.rekeningrijden.simulation.coordinate.CoordinateImpl
import com.rekeningrijden.simulation.route.SubRouteImpl
import com.rekeningrijden.simulation.services.RouteService
import io.jenetics.jpx.GPX
import io.jenetics.jpx.Track
import io.jenetics.jpx.TrackSegment
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

@Component
@Order(2)
class RouteCommandLineRunner : CommandLineRunner {
    @Autowired
    private lateinit var routeService: RouteService

    override fun run(vararg args: String?) {
        val routesRoot = resolver.getResource(routesFolder)
        val routesRootPath = routesRoot.uri
        val subrouteResources = resolver.getResources("classpath*:/$routesFolder**/*.gpx")

        Arrays.stream(subrouteResources)
            /**
             * Walk through all files found (recursively) in the subroutes resource
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
                val subroute = SubRouteImpl(countryCode)

                /**
                 * Read the GPX resource and map it to a coordinate.
                 */
                val tracks = GPX.read(subrouteResource.inputStream)
                val coordinates = tracks
                    .tracks()
                    .flatMap(Track::segments)
                    .flatMap(TrackSegment::points)
                    .map { CoordinateImpl(it.latitude.toDouble(), it.longitude.toDouble()) }
                    .collect(Collectors.toList())

                subroute.coordinates.addAll(coordinates)

                routeService.save(routeName, subroute)
            }

        logger.info("${routeService.subroutes.values.map { it.size }.sum()} subroutes have been initialized")
        logger.info("${routeService.routes.size} routes have been initialized")
    }

    companion object {
        private val cl = this::class.java.classLoader
        private val logger = LoggerFactory.getLogger(RouteCommandLineRunner::class.java)
        private val resolver = PathMatchingResourcePatternResolver(cl)
        const val routesFolder = "routes/"
    }
}
