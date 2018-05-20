package com.rekeningrijden.simulation.services

import com.rekeningrijden.simulation.car.CarImpl
import org.springframework.stereotype.Service

@Service
class CarService {
    val cars = mutableSetOf<CarImpl>()

    fun save(carImpls: Set<CarImpl>) {
        this.cars.addAll(carImpls)
    }
}
