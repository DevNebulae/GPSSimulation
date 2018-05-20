package com.rekeningrijden.simulation.services

import com.rekeningrijden.simulation.entities.Route
import com.rekeningrijden.simulation.entities.SubRoute
import org.springframework.stereotype.Service

@Service
class RouteService {
    val subroutes = mutableMapOf<String, MutableList<SubRoute>>()
    val routes
        get() =
            subroutes
                .entries
                .map { entrySet -> Route(entrySet.key, entrySet.value) }
                .toList()

    fun save(routeName: String, subroute: SubRoute) {
        if (!subroutes.containsKey(routeName))
            subroutes[routeName] = mutableListOf()

        subroutes[routeName]!!.add(subroute)
    }
}
