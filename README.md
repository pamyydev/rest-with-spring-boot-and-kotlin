# 📋 Task Manager API (Em desenvolvimento)

API REST para gerenciamento de tarefas desenvolvida com **Spring Boot 3** + **Kotlin** para demonstração em entrevista técnica.

## 🚀 Tecnologias Utilizadas

### Backend
- **Kotlin** - Linguagem moderna e concisa para JVM
- **Spring Boot 3.2** - Framework para desenvolvimento rápido
- **Spring Data JPA** - Camada de persistência com Hibernate
- **Gradle** - Gerenciador de dependências e build

### DevOps & Deploy
- **Docker** - Containerização da aplicação
- **Docker Compose** - Ambiente de desenvolvimento local
- **AWS** - Deploy em nuvem (ECS, RDS, etc.)

### Ferramentas
- **Postman** - Testes da API
- **Spring Boot Actuator** - Monitoramento e health checks

## 🏗️ Arquitetura

```
📦 com.interview.taskmanager
├── 📂 controller/          # Camada REST (Controllers)
├── 📂 service/            # Lógica de negócio (Services)
├── 📂 repository/         # Acesso a dados (Repositories)
├── 📂 model/              # Entidades JPA
└── 📂 TaskManagerApplication.kt
```

### Padrões Utilizados
- **MVC** - Model-View-Controller
- **Repository Pattern** - Abstração do acesso a dados
- **Dependency Injection** - Inversão de controle
- **DTO Pattern** - Data Transfer Objects para API

## 🔧 Como Executar

### Pré-requisitos
- Java 17+
- Docker (opcional, para ambiente completo)

### Opção 1: Execução Local
```bash
# Clone o repositório
git clone <url-do-repositorio>
cd task-manager-api

# Execute com Gradle
./gradlew bootRun

# Ou compile e execute o JAR
./gradlew build
java -jar build/libs/task-manager-1.0.0.jar
```

### Opção 2: Docker Compose (Recomendado)
```bash
# Sobe aplicação + PostgreSQL
docker-compose up --build -d

# Ver logs
docker-compose logs -f app

# Parar tudo
docker-compose down
```

### Opção 3: Só a aplicação com Docker
```bash
# Build da imagem
docker build -t task-manager-api .

# Execução (usando H2 em memória)
docker run -p 8080:8080 task-manager-api
```

## 📡 Endpoints da API

### Base URL
```
http://localhost:8080/api
```

### 📋 Gerenciamento de Tarefas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/tasks` | Lista todas as tarefas |
| `GET` | `/tasks/{id}` | Busca tarefa por ID |
| `POST` | `/tasks` | Cria nova tarefa |
| `PUT` | `/tasks/{id}` | Atualiza tarefa |
| `PATCH` | `/tasks/{id}/toggle` | Alterna status da tarefa |
| `DELETE` | `/tasks/{id}` | Remove tarefa |

### 🔍 Consultas e Filtros
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/tasks/status/{completed}` | Filtra por status |
| `GET` | `/tasks/search?q={termo}` | Busca por título |
| `GET` | `/tasks/stats` | Estatísticas das tarefas |

### 🏥 Monitoramento
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/actuator/health` | Health check |
| `GET` | `/actuator/info` | Info da aplicação |

## 🧪 Testando com Postman

1. Importe a coleção `TaskManager.postman_collection.json`
2. Configure a variável `baseUrl` para `http://localhost:8080/api`
3. Execute os requests na ordem sugerida

### Exemplos de Payload

**Criar tarefa:**
```json
{
  "title": "Estudar Spring Boot",
  "description": "Aprender conceitos fundamentais do framework"
}
```

**Atualizar tarefa:**
```json
{
  "title": "Estudar Spring Boot e Kotlin",
  "description": "Aprender conceitos fundamentais e boas práticas",
  "completed": true
}
```

### Produção (PostgreSQL)
Configurado via variáveis de ambiente no `docker-compose.yml` AWS.

## ☁️ Deploy na AWS

1. **AWS ECS + RDS**
   - Container registry: ECR
   - Orquestração: ECS com Fargate

2. **AWS Elastic Beanstalk**
   - Deploy mais simples
   - Configuração automática de load balancer

3. **AWS Lambda + API Gateway**
   - Serverless com Spring Cloud Function

### Configuração para Produção

```bash
# Variáveis de ambiente necessárias
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://rds-endpoint:5432/taskmanager
DATABASE_USERNAME=admin
DATABASE_PASSWORD=sua-senha-segura
```

## 🧠 Conceitos Demonstrados

### Spring Boot
- **Auto-configuração** - Configuração automática baseada em dependências
- **Starter POMs** - Dependências pré-configuradas
- **Embedded Server** - Tomcat embarcado
- **Profiles** - Configurações por ambiente

### Spring Data JPA
- **Repository Pattern** - Abstração de acesso a dados
- **Query Methods** - Queries baseadas em nome de método
- **Custom Queries** - JPQL e SQL nativo
- **Entity Relationships** - Mapeamento objeto-relacional

### Kotlin
- **Data Classes** - Classes com métodos automáticos
- **Null Safety** - Prevenção de NullPointerException
- **Extension Functions** - Extensão de classes existentes
- **Coroutines** - Programação assíncrona (não usado aqui, mas mencionável)

### REST API Design
- **HTTP Methods** semânticos (GET, POST, PUT, PATCH, DELETE)
- **Status Codes** apropriados (200, 201, 404, 400, etc.)
- **Resource-based URLs** - `/tasks/{id}`
- **Content Negotiation** - JSON como formato padrão

### DevOps
- **Containerização** - Docker multi-stage builds
- **Infrastructure as Code** - docker-compose.yml
- **Health Checks** - Monitoramento de aplicação
- **Logging** - Configuração de logs estruturados

## 📝 Possíveis Melhorias

### Funcionalidades
- [ ] Autenticação com JWT
- [ ] Categorização de tarefas
- [ ] Prioridades e prazos
- [ ] Notificações
- [ ] API de upload de arquivos

### Técnicas
- [ ] Cache com Redis
- [ ] Mensageria com RabbitMQ
- [ ] Testes automatizados
- [ ] CI/CD com GitHub Actions
- [ ] Monitoramento com Prometheus

### Frontend
- [ ] React/Vue.js SPA
- [ ] Mobile com React Native
- [ ] PWA (Progressive Web App)

## 👨‍💻 Sobre a Desenvolvedora
Este projeto foi desenvolvido como demonstração técnica para entrevistas, showcasing:
- Conhecimento em Spring Boot ecosystem
- Proficiência em Kotlin
- Boas práticas de desenvolvimento
- Experiência com containerização
- Conhecimento em cloud computing (AWS)

**Contato**: pamelamiranda.o87@gmail.com
