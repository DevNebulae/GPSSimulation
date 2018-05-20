package com.rekeningrijden.simulation.route

import com.rekeningrijden.simulation.coordinate.Coordinate

interface SubRoute {
    val coordinates: List<Coordinate>
    val countryCode: String
    var isSubRouteDriven: Boolean

    fun getNextCoordinateAtIndex(index: Int): Coordinate? {
        if (index >= coordinates.size) {
            this.isSubRouteDriven = true
            return null
        }
        return coordinates[index]
    }
}
