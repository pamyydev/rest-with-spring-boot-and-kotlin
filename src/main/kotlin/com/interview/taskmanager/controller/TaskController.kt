package com.interview.taskmanager.controller

import com.interview.taskmanager.model.Task
import com.interview.taskmanager.service.TaskService
import com.interview.taskmanager.service.TaskStats
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller REST - nossa API HTTP
 * 
 * @RestController combina @Controller + @ResponseBody
 * Significa que todos os métodos retornam dados (JSON) ao invés de views
 * 
 * @RequestMapping define o prefixo de todas as rotas deste controller
 * @CrossOrigin permite requisições de outros domínios (importante pro frontend!)
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = ["*"]) // Em produção, especifique os domínios permitidos!
class TaskController(
    private val taskService: TaskService // Injeção de dependência via construtor
) {
    // Buscar todas as tarefas
    @GetMapping
    fun getAllTasks(): ResponseEntity<List<Task>> {
        val tasks = taskService.getAllTasks()
        return ResponseEntity.ok(tasks)
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<Task> {
        val task = taskService.getTaskById(id)
        return task?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    // Criar uma nova tarefa
    @PostMapping
    fun createTask(@RequestBody request: CreateTaskRequest): ResponseEntity<Task> {
        return try {
            val task = taskService.createTask(request.title, request.description)
            ResponseEntity.status(HttpStatus.CREATED).body(task)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    // Atualizar tarefa existente
    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @RequestBody request: UpdateTaskRequest
    ): ResponseEntity<Task> {
        val updatedTask = taskService.updateTask(
            id = id,
            title = request.title,
            description = request.description,
            completed = request.completed
        )
        return updatedTask?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    // Alterar status de conclusão da tarefa
    @PatchMapping("/{id}/toggle")
    fun toggleTaskCompletion(@PathVariable id: Long): ResponseEntity<Task> {
        val task = taskService.toggleTaskCompletion(id)
        return task?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    // Deletar tarefa
    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Unit> {
        val deleted = taskService.deleteTask(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // Buscar tarefas por status (concluídas/não concluídas)
    @GetMapping("/status/{completed}")
    fun getTasksByStatus(@PathVariable completed: Boolean): ResponseEntity<List<Task>> {
        val tasks = taskService.getTasksByStatus(completed)
        return ResponseEntity.ok(tasks)
    }

    // Buscar tarefas pelo título (filtro de texto)
    @GetMapping("/search")
    fun searchTasks(@RequestParam(value = "q", defaultValue = "") searchTerm: String): ResponseEntity<List<Task>> {
        val tasks = if (searchTerm.isNotBlank()) {
            taskService.searchTasksByTitle(searchTerm)
        } else {
            taskService.getAllTasks()
        }
        return ResponseEntity.ok(tasks)
    }

    // Obter estatísticas das tarefas
    @GetMapping("/stats")
    fun getTaskStats(): ResponseEntity<TaskStats> {
        val stats = taskService.getTaskStats()
        return ResponseEntity.ok(stats)
    }
}

/**
 * DTOs (Data Transfer Objects) para as requisições
 * 
 * Separar os DTOs da entidade JPA é uma boa prática:
 * - Controle sobre o que aceitar na API
 * - Validações específicas
 * - Evita expor detalhes internos
 */
data class CreateTaskRequest(
    val title: String,
    val description: String? = null
)

data class UpdateTaskRequest(
    val title: String? = null,
    val description: String? = null,
    val completed: Boolean? = null
)
