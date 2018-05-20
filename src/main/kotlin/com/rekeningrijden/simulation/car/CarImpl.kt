package com.rekeningrijden.simulation.car

data class CarImpl(
    override var id: String,
    override var country: String
) : Car
