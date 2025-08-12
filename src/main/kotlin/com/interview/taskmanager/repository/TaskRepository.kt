package com.interview.taskmanager.repository

import com.interview.taskmanager.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository pattern - nossa camada de acesso aos dados
 *
 * JpaRepository já nos dá vários métodos prontos:
 * - findAll(), findById(), save(), delete(), etc.
 *
 * Só precisamos herdar e pronto! O Spring Data JPA faz a mágica
 * Os tipos genéricos são <Entidade, TipoDaChavePrimaria>
 */
@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    // Spring Data JPA cria automaticamente a implementação baseada no nome do método!
    // findByCompleted vira "SELECT * FROM tasks WHERE completed = ?"
    fun findByCompleted(completed: Boolean): List<Task>

    // Métodos com múltiplos critérios também funcionam
    fun findByTitleContainingIgnoreCase(title: String): List<Task>

    /**
     * Exemplo de query customizada usando @Query
     * Útil quando o nome do método ficaria muito complexo
     *
     * JPQL (Java Persistence Query Language) é como SQL mas pra objetos
     */
    @Query("SELECT t FROM Task t WHERE t.completed = false ORDER BY t.createdAt DESC")
    fun findPendingTasksOrderedByDate(): List<Task>

    /**
     * Query nativa em SQL quando JPQL não rola
     * nativeQuery = true diz que é SQL puro
     */
    @Query(
        value = "SELECT COUNT(*) FROM tasks WHERE completed = :completed",
        nativeQuery = true
    )
    fun countByCompletedStatus(completed: Boolean): Long
}

