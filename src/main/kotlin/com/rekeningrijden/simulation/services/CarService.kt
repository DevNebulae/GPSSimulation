package com.rekeningrijden.simulation.services

import com.rekeningrijden.simulation.car.Car
import com.rekeningrijden.simulation.car.CarImpl
import org.springframework.stereotype.Service

@Service
class CarService {
    val cars = mutableSetOf<Car>()

    fun save(carImpls: Set<Car>) {
        this.cars.addAll(carImpls)
    }
}
