package com.rekeningrijden.simulation.route

import com.rekeningrijden.simulation.coordinate.Coordinate
import java.util.LinkedList

data class SubRouteImpl(
    override val countryCode: String,
    override val coordinates: LinkedList<Coordinate> = LinkedList()
) : SubRoute {
    override var isSubRouteDriven = false
}
