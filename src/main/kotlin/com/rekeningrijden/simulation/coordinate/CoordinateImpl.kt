package com.rekeningrijden.simulation.coordinate

data class CoordinateImpl(
    override var latitude: Double,
    override var longitude: Double
) : Coordinate
