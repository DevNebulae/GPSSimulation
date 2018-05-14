package com.rekeningrijden.simulation.entities

import org.junit.Test

import org.junit.Assert.*

class RouteTest {

    private val route = Route("TEST", listOf(SubRoute(countryCode = "FI", isSubRouteDriven = false), SubRoute(countryCode = "IT", isSubRouteDriven = false)))

    @Test
    fun setAllSubRoutesToFalse() {
        this.route.setAllSubRoutesToFalse()
        assertTrue(this.route.subRoutes.filter { it.isSubRouteDriven }.count() == 0)
    }
}
