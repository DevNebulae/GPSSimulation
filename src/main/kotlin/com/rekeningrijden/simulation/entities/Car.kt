package com.rekeningrijden.simulation.entities

import com.rekeningrijden.europe.dtos.TransLocationDto
import java.util.*

class Car(val id: String? = null, val country: String? = null) {
    private val timer: Timer = Timer()

    fun generateTransLocation(): TransLocationDto? {
        //This method uses libraries to calculate the lat and lon from the route.
        //The timestamp comes from the timer in this object.
        //A subroute has a countrycode, a subroute always is in only 1 country.
        //A multinational route needs multiple subroutes to be international.
        //The serial number comes from the serialnumber propertie of this class.
        return null
    }
}