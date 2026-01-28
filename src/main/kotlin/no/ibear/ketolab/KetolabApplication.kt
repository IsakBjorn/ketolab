package no.ibear.ketolab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KetolabApplication

fun main(args: Array<String>) {
    runApplication<KetolabApplication>(*args)
}
