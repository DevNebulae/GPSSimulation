package com.rekeningrijden.simulation.services

import com.rekeningrijden.simulation.car.Car
import org.springframework.stereotype.Service

@Service
class CarService {
    val cars = mutableSetOf<Car>()

    fun save(cars: Set<Car>) {
        this.cars.addAll(cars)
    }
}
