package com.rekeningrijden.simulation.route

import com.rekeningrijden.simulation.coordinate.CoordinateImpl

data class SubRouteImpl(
    /**
     * The two-letter ISO code indicating what country the car is currently
     * driving in.
     */
    override val countryCode: String
) : SubRoute {
    override val coordinates = mutableListOf<CoordinateImpl>()
    override var isSubRouteDriven = false
}
