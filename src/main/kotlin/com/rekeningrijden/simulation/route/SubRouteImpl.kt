package com.rekeningrijden.simulation.route

import com.rekeningrijden.simulation.coordinate.Coordinate
import java.util.LinkedList

data class SubRouteImpl(
    /**
     * The two-letter ISO code indicating what country the car is currently
     * driving in.
     */
    override val countryCode: String
) : SubRoute {
    override val coordinates = LinkedList<Coordinate>()
    override var isSubRouteDriven = false
}
