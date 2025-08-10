package com.interview.taskmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Classe principal da aplicação
 *
 * @SpringBootApplication é uma anotação "guarda-chuva" que combina:
 * - @Configuration: permite definir beans
 * - @EnableAutoConfiguration: configura automaticamente o Spring baseado nas dependências
 * - @ComponentScan: escaneia por componentes no pacote atual e subpacotes
 *
 * Basicamente, essa anotação faz toda a mágica do Spring Boot acontecer! 🎩✨
 */
@SpringBootApplication
@EnableJpaRepositories // Habilita os repositórios JPA (nossa camada de dados)
class TaskManagerApplication

fun main(args: Array<String>) {
    // runApplication é uma função Kotlin que inicia a aplicação Spring Boot
    runApplication<TaskManagerApplication>(*args)
}

