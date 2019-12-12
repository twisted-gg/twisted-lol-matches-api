package com.twisted.lolmatches

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class LolMatchesApplication

fun main(args: Array<String>) {
  runApplication<LolMatchesApplication>(*args)
}
