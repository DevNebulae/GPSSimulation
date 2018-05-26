package com.rekeningrijden.simulation.coordinate

/**
 * Generic decimal-based coordinate on planet Earth. The latitude and longitude
 * are bounded by ±90 and ±180 respectively.
 */
interface Coordinate {
    val latitude: Double
    val longitude: Double
}
