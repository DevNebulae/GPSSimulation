package com.rekeningrijden.simulation.car

import java.io.Serializable
import java.util.UUID
import kotlin.properties.Delegates

class CarImpl : Car, Serializable {
    override var id by Delegates.notNull<UUID>()
    override var country by Delegates.notNull<String>()
    override val speed = 80.0
}
