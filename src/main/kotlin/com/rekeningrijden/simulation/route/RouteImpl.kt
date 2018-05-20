package com.rekeningrijden.simulation.route

/**
 * Routes have subroutes in different countries. All subroutes combined form a
 * single route and the format in which the subroutes are saved is GPX.
 *
 * The format the GPX files are included is as follows:
 * * RouteImpl 1: contains a route in Italy and one in Germany.
 * * RouteImpl 2: conatins a route in the Netherlands.
 * * RouteImpl 3: contains a route in Italy, Germany, the Netherlands and Sweden.
 * * Et cetera...
 */
data class RouteImpl(
    override val routeName: String,

    override val subRoutes: List<SubRoute>
) : Route {
    override var isRouteDriven: Boolean = false
}
