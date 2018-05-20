package com.rekeningrijden.simulation.entities

import java.util.Timer

class Car(val id: String? = null, val country: String? = null) {
    private val timer: Timer = Timer()
}
