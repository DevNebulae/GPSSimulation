package com.rekeningrijden.simulation.entities

/**
 * Routes have subroutes in different countries. All subroutes combined form a
 * single route and the format in which the subroutes are saved is GPX.
 *
 * The format the GPX files are included is as follows:
 * * Route 1: contains a route in Italy and one in Germany.
 * * Route 2: conatins a route in the Netherlands.
 * * Route 3: contains a route in Italy, Germany, the Netherlands and Sweden.
 * * Et cetera...
 */
data class Route(
    private val routeNumber: String,

    val subRoutes: List<SubRoute>
) {
    var isRouteDriven: Boolean = false

    fun setAllSubRoutesToFalse() {
        subRoutes.forEach { it.isSubRouteDriven = false }
    }
}
