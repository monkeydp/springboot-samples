package com.monkeydp.springboot.sample.zombodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZombodbSimple

fun main(args: Array<String>) {
    runApplication<ZombodbSimple>(*args)
}
