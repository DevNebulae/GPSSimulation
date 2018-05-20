package com.rekeningrijden.simulation.entities

import com.rekeningrijden.simulation.coordinate.CoordinateImpl

data class SubRoute(
    /**
     * The two-letter ISO code indicating what country the car is currently
     * driving in.
     */
    val countryCode: String
) {
    val coordinates = mutableListOf<CoordinateImpl>()
    var isSubRouteDriven: Boolean = false

    fun getNextCoordinateAtIndex(index: Int): CoordinateImpl? {
        if (index >= coordinates.size) {
            this.isSubRouteDriven = true
            return null
        }
        return coordinates[index]
    }
}
