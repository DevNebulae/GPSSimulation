package com.rekeningrijden.simulation.car

import java.io.Serializable
import java.util.UUID
import kotlin.properties.Delegates

class CarImpl : Car, Serializable {
    override val id: UUID by Delegates.notNull<UUID>()
    override val country by Delegates.notNull<String>()
    override var speed: Double = 80.0
}
