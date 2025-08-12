package com.interview.taskmanager.model

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * Entidade que representa uma tarefa no banco de dados
 *
 * @Entity: diz pro JPA que essa classe vai virar uma tabela no banco
 * @Table: especifica o nome da tabela (opcional, por padrão seria "task")
 *
 * Em Kotlin, data class já nos dá equals(), hashCode(), toString() e copy() de graça
 */
@Entity
@Table(name = "tasks")
data class Task(

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    val id: Long? = null, // Nullable porque quando criamos ainda não tem ID

    @Column(nullable = false, length = 200) // Coluna obrigatória com tamanho máximo
    val title: String,

    @Column(length = 1000) // Descrição opcional e maior
    val description: String? = null,

    @Column(nullable = false)
    val completed: Boolean = false, // Por padrão, tarefa não está completa

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(), // Timestamp de criação

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
) {
    /**
     * Construtor secundário para facilitar a criação de tarefas
     * Kotlin permite múltiplos construtores, bem útil! 🎯
     */
    constructor(title: String, description: String? = null) : this(
        title = title,
        description = description,
        completed = false,
        createdAt = LocalDateTime.now()
    )
}

