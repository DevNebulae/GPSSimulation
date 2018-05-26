package com.rekeningrijden.simulation.route

interface Route {
    /**
     * The unique name this route has. It is mainly used for identification
     * during debugging. The format is as follows: `<start>-<end>`.
     */
    val routeName: String
    val subRoutes: List<SubRoute>
}
