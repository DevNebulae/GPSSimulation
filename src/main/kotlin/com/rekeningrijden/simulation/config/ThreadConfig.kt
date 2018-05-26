package com.rekeningrijden.simulation.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class ThreadConfig {
    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()

        executor.corePoolSize = Int.MAX_VALUE
        executor.threadNamePrefix = "Journey-Thread-"
        executor.initialize()

        return executor
    }
}
