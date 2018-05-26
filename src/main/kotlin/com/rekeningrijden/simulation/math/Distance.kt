package com.rekeningrijden.simulation.math

/**
 * The radius of planet Earth in _kilometers_.
 *
 * Source: [Wikipedia](https://en.wikipedia.org/wiki/Earth_radius)
 */
private const val EARTH_RADIUS = 6371

/**
 * Calculates the distance between two given coordinates in _kilometers_.
 *
 * Source: [StackOverflow](https://stackoverflow.com/a/3694416/3334862)
 */
fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return EARTH_RADIUS * c
}
