package com.rekeningrijden.simulation.entities

import org.junit.Test

import org.junit.Assert.*

class SubRouteTest {

    private val testCoordinate1 = Coordinate(20.0, 20.0)
    private val testCoordinate2 = Coordinate(30.0, 30.0)

    private val subroute: SubRoute

    init {
        this.subroute = SubRoute(countryCode = "FI", coordinates = mutableListOf(testCoordinate1, testCoordinate2))
    }

    @Test
    fun getNextCoordinateAtIndex() {
        assertEquals(this.testCoordinate1, this.subroute.getNextCoordinateAtIndex(0))
        assertEquals(this.testCoordinate2, this.subroute.getNextCoordinateAtIndex(1))
        assertFalse(this.subroute.isSubRouteDriven)
        assertNull(null, this.subroute.getNextCoordinateAtIndex(2))
        assertTrue(this.subroute.isSubRouteDriven)
    }

}
