package com.rekeningrijden.simulation.entities

data class SubRoute(
    /**
     * The two-letter ISO code indicating what country the car is currently
     * driving in.
     */
    val countryCode: String
) {
    val coordinates = mutableListOf<Coordinate>()
    var isSubRouteDriven: Boolean = false

    fun getNextCoordinateAtIndex(index: Int): Coordinate? {
        if (index >= coordinates.size) {
            this.isSubRouteDriven = true
            return null
        }
        return coordinates[index]
    }
}
