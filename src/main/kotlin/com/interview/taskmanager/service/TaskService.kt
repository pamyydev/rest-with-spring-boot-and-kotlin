package com.interview.taskmanager.service

import com.interview.taskmanager.model.Task
import com.interview.taskmanager.repository.TaskRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Camada de serviço - aqui fica a lógica de negócio
 *
 * @Service marca essa classe como um componente de serviço do Spring
 * Ela será automaticamente descoberta pelo @ComponentScan
 *
 * Injeção de dependência via construtor - mais limpo que @Autowired! 💉
 */
@Service
class TaskService(
    private val taskRepository: TaskRepository // Spring injeta automaticamente
) {

    /**
     * Lista todas as tarefas
     * Método simples, só delega pro repository
     */
    fun getAllTasks(): List<Task> {
        return taskRepository.findAll()
    }

    /**
     * Busca tarefa por ID
     *
     * findByIdOrNull é uma extensão do Kotlin que retorna null ao invés de Optional
     * Mais idiomático em Kotlin! 🎯
     */
    fun getTaskById(id: Long): Task? {
        return taskRepository.findByIdOrNull(id)
    }

    /**
     * Cria uma nova tarefa
     *
     * Aqui poderia ter validações de negócio:
     * - Título não pode estar vazio
     * - Não permitir tarefas duplicadas
     * - etc.
     */
    fun createTask(title: String, description: String?): Task {
        // Validação simples
        require(title.isNotBlank()) { "Título não pode estar vazio!" }

        val task = Task(title = title.trim(), description = description?.trim())
        return taskRepository.save(task)
    }

    /**
     * Atualiza uma tarefa existente
     *
     * copy() é uma funcionalidade das data classes do Kotlin
     * Cria uma nova instância alterando apenas os campos especificados
     */
    fun updateTask(id: Long, title: String?, description: String?, completed: Boolean?): Task? {
        val existingTask = taskRepository.findByIdOrNull(id) ?: return null

        // Kotlin tem null-safety built-in, então usamos ?: para valores padrão
        val updatedTask = existingTask.copy(
            title = title?.trim() ?: existingTask.title,
            description = description?.trim() ?: existingTask.description,
            completed = completed ?: existingTask.completed,
            updatedAt = LocalDateTime.now() // Sempre atualiza o timestamp
        )

        return taskRepository.save(updatedTask)
    }

    /**
     * Marca/desmarca tarefa como completa
     * Método de conveniência comum em TODOs
     */
    fun toggleTaskCompletion(id: Long): Task? {
        val task = getTaskById(id) ?: return null
        return updateTask(id, null, null, !task.completed)
    }

    /**
     * Deleta uma tarefa
     *
     * Em aplicações reais, frequentemente fazemos "soft delete"
     * (marcar como deletado ao invés de remover do banco)
     */
    fun deleteTask(id: Long): Boolean {
        return if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    /**
     * Métodos que usam as queries customizadas do repository
     */
    fun getTasksByStatus(completed: Boolean): List<Task> {
        return taskRepository.findByCompleted(completed)
    }

    fun searchTasksByTitle(searchTerm: String): List<Task> {
        return taskRepository.findByTitleContainingIgnoreCase(searchTerm)
    }

    /**
     * Método que demonstra lógica de negócio
     * Retorna estatísticas das tarefas
     */
    fun getTaskStats(): TaskStats {
        val total = taskRepository.count()
        val completed = taskRepository.countByCompletedStatus(true)
        val pending = total - completed

        return TaskStats(
            total = total,
            completed = completed,
            pending = pending,
            completionRate = if (total > 0) (completed.toDouble() / total.toDouble()) * 100 else 0.0
        )
    }
}

/**
 * Data class para retornar estatísticas
 * Mostra como criar DTOs (Data Transfer Objects) em Kotlin
 */
data class TaskStats(
    val total: Long,
    val completed: Long,
    val pending: Long,
    val completionRate: Double // Percentual de conclusão
)

