package com.rekeningrijden.simulation.route

import com.rekeningrijden.simulation.coordinate.Coordinate
import java.util.LinkedList

interface SubRoute {
    /**
     * A set amount of coordinates which are bound by a country's borders. A
     * [Route][com.rekeningrijden.simulation.route.Route] is composed of
     * multiple sub-routes. Only countries which have registered with the
     * rekeningrijden program will have routes registered.
     */
    val coordinates: LinkedList<Coordinate>

    /**
     * The
     * [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
     * country code which indicates which country the car originates from.
     */
    val countryCode: String
}
