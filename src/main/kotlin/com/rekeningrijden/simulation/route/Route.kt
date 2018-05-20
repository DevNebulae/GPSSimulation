package com.rekeningrijden.simulation.route

interface Route {
    val routeName: String
    val subRoutes: List<SubRoute>
    var isRouteDriven: Boolean

    fun setAllSubRoutesToFalse() {
        subRoutes.forEach { it.isSubRouteDriven = false }
    }
}
