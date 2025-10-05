# Ice Cream Parlor - Sistema de Gerenciamento de Pedidos

Sistema distribuído baseado em arquitetura orientada a eventos (EDA) utilizando RabbitMQ para orquestração de pedidos de uma sorveteria.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando o Projeto](#executando-o-projeto)
- [Testando a Aplicação](#testando-a-aplicação)
- [Endpoints da API](#endpoints-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

O Ice Cream Parlor é um sistema de gerenciamento de pedidos que utiliza mensageria assíncrona para coordenar diferentes serviços:

- **Workflow**: Orquestração do fluxo de pedidos
- **Caixa**: Processamento de pagamentos
- **Estoque**: Gestão de reserva de produtos
- **Produção**: Controle de fabricação dos pedidos
- **Entregas**: Gerenciamento de entregas
- **Cliente**: Notificações ao cliente
- **Relatório**: Auditoria e registro de eventos

---

## 🏗️ Arquitetura

O sistema segue uma arquitetura de **Saga Coreografada** onde cada serviço reage a eventos publicados no RabbitMQ.

### Fluxo de um Pedido

```
1. Cliente cria pedido
2. Workflow inicia processamento
3. Caixa processa pagamento (70% aprovação)
4. Estoque reserva itens (80% sucesso)
5. Produção fabrica o pedido
6. Entregas despacha e entrega
7. Cliente recebe notificações em cada etapa
8. Relatório registra todos os eventos
```

### Padrões de Mensageria

- **Exchange Principal**: `sorv.ex` (Topic)
- **Exchange DLX**: `sorv.dlx` (Direct)
- **Retry com TTL**: 15 segundos
- **Dead Letter Queue**: Uma por domínio

---

## 📦 Pré-requisitos

### Software Necessário

- **Java 21** ou superior
   - Verificar: `java -version`
   - Download: https://adoptium.net/

- **Maven 3.9+**
   - Verificar: `mvn -version`
   - Download: https://maven.apache.org/download.cgi

- **PostgreSQL 13+**
   - Verificar: `psql --version`
   - Download: https://www.postgresql.org/download/

- **Git**
   - Verificar: `git --version`
   - Download: https://git-scm.com/downloads

### Serviços em Nuvem

- **CloudAMQP** (RabbitMQ como serviço)
   - Criar conta gratuita em: https://www.cloudamqp.com/
   - Plano gratuito suficiente para desenvolvimento

---

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/ice-cream-parlor.git
cd ice-cream-parlor
```

### 2. Configurar PostgreSQL

#### Linux/Mac:
```bash
# Instalar PostgreSQL
sudo apt-get install postgresql postgresql-contrib  # Ubuntu/Debian
brew install postgresql@13                          # Mac

# Iniciar serviço
sudo service postgresql start    # Linux
brew services start postgresql   # Mac

# Acessar PostgreSQL
sudo -u postgres psql
```

#### Windows:
1. Baixe o instalador do PostgreSQL
2. Execute e siga o wizard de instalação
3. Abra o pgAdmin ou cmd e execute:

```sql
-- Criar banco de dados
CREATE DATABASE sorveteria;

-- Criar usuário
CREATE USER icecream_user WITH PASSWORD 'admin';

-- Conceder permissões
GRANT ALL PRIVILEGES ON DATABASE sorveteria TO icecream_user;

-- Conectar ao banco
\c sorveteria

-- Conceder permissões no schema
GRANT ALL ON SCHEMA public TO icecream_user;

GRANT ALL PRIVILEGES ON DATABASE sorveteria TO icecream_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO icecream_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO icecream_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO icecream_user;

-- Garantir permissões futuras
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO icecream_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO icecream_user
```

### 3. Configurar CloudAMQP

1. Acesse https://customer.cloudamqp.com/login
2. Crie uma conta gratuita
3. Crie uma nova instância:
   - Nome: `ice-cream-parlor`
   - Plan: `Little Lemur (Free)`
   - Region: Escolha a mais próxima
4. Após criar, acesse a instância e copie:
   - **AMQP URL**: `amqps://usuario:senha@servidor.rmq.cloudamqp.com/vhost`
   - Host: Ex: `jackal.rmq.cloudamqp.com`
   - Username: Ex: `kmskovrf`
   - Password: Ex: `Ei9fF3bR2B6P_7_R_vaxARNfCXkKqj1d`
   - Virtual Host: Ex: `kmskovrf`

---

## ⚙️ Configuração

### Arquivo application.yaml

Localize o arquivo `src/main/resources/application.yaml` e configure:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sorveteria
    username: icecream_user
    password: admin
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        jdbc:
          time_zone: UTC

  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true

  docker:
    compose:
      enabled: false

  rabbitmq:
    addresses: [SEU_HOST].rmq.cloudamqp.com:5671
    username: [SEU_USERNAME]
    password: [SUA_SENHA]
    virtual-host: [SEU_VHOST]
    ssl:
      enabled: true
    connection-timeout: 30000
    requested-heartbeat: 30
    template:
      retry:
        enabled: true
        initial-interval: 2000
        max-attempts: 3
        multiplier: 2
    listener:
      simple:
        retry:
          enabled: true
          initial-interval: 2000
          max-attempts: 3
          multiplier: 2
        acknowledge-mode: auto
        prefetch: 1
```

**Substitua:**
- `[SEU_HOST]` pelo host do CloudAMQP (ex: jackal)
- `[SEU_USERNAME]` pelo username
- `[SUA_SENHA]` pela senha
- `[SEU_VHOST]` pelo virtual host

---

## ▶️ Executando o Projeto

### 1. Compilar o Projeto

```bash
mvn clean install
```

**Saída esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

### 2. Executar a Aplicação

```bash
mvn spring-boot:run
```

**Logs de sucesso esperados:**
```
INFO - Created new connection: rabbitConnectionFactory#...
INFO - Tomcat started on port 8080 (http) with context path '/'
INFO - Started MessagingRabbitmqApplication in X.XXX seconds
```

### 3. Verificar se está Rodando

```bash
curl http://localhost:8080/actuator/health
```

**Resposta:**
```json
{"status":"UP"}
```

---

## 🧪 Testando a Aplicação

### Teste Básico - Criar um Pedido

```bash
curl -X POST "http://localhost:8080/workflow/pedidos?clienteId=maria&valorTotal=50.00" \
  -H "x-user: teste"
```

**Resposta Esperada:**
```json
{
  "pedidoId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "iniciado"
}
```

### Consultar Dados

```bash
# Todos os registros de caixa
curl http://localhost:8080/caixa

# Pedido específico no caixa
curl http://localhost:8080/caixa/por-pedido/[PEDIDO_ID]

# Todos os pedidos em produção
curl http://localhost:8080/producao

# Todas as entregas
curl http://localhost:8080/entregas

# Notificações de um cliente
curl http://localhost:8080/cliente/por-cliente/maria

# Todos os eventos registrados
curl http://localhost:8080/relatorio
```

### Verificar Filas no RabbitMQ

1. Acesse https://customer.cloudamqp.com
2. Entre na sua instância
3. Clique em "RabbitMQ Manager"
4. Vá em "Queues"
5. Verifique se as 21 filas foram criadas:
   - 7 filas principais (q.workflow, q.caixa, etc.)
   - 7 filas de retry
   - 7 DLQs

---

## 🔌 Endpoints da API

### Workflow
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/workflow/pedidos?clienteId={id}&valorTotal={valor}` | Criar novo pedido |

### Caixa
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/caixa` | Listar todos os pagamentos |
| GET | `/caixa/por-pedido/{pedidoId}` | Buscar pagamento por pedido |

### Estoque
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/estoque` | Listar todas as reservas |

### Produção
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/producao` | Listar todas as produções |
| GET | `/producao/por-pedido/{pedidoId}` | Buscar produção por pedido |

### Entregas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/entregas` | Listar todas as entregas |
| GET | `/entregas/por-pedido/{pedidoId}` | Buscar entrega por pedido |

### Cliente
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/cliente` | Listar todas as notificações |
| GET | `/cliente/por-cliente/{clienteId}` | Buscar notificações por cliente |
| GET | `/cliente/por-pedido/{pedidoId}` | Buscar notificações por pedido |

### Relatório
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/relatorio` | Listar todos os eventos |

---

## 📁 Estrutura do Projeto

```
ice-cream-parlor/
├── src/
│   ├── main/
│   │   ├── java/com/IceCreamParlor/
│   │   │   ├── consumer/          # Consumers RabbitMQ
│   │   │   ├── controller/        # Controllers REST
│   │   │   ├── dto/
│   │   │   │   ├── entities/      # Entidades JPA
│   │   │   │   ├── enums/         # Enumerações
│   │   │   │   └── events/        # DTOs de eventos
│   │   │   ├── messaging/         # Configuração RabbitMQ
│   │   │   ├── producer/          # Producers RabbitMQ
│   │   │   ├── repositories/      # Repositórios JPA
│   │   │   ├── service/           # Lógica de negócio
│   │   │   └── MessagingRabbitmqApplication.java
│   │   └── resources/
│   │       ├── application.yaml   # Configurações
│   │       └── db/migration/      # Scripts Flyway
│   └── test/                      # Testes
├── pom.xml                        # Dependências Maven
└── README.md                      # Este arquivo
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **Spring AMQP** - Integração com RabbitMQ
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Migrations do banco
- **RabbitMQ** (CloudAMQP) - Message broker
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

---

## 🐛 Troubleshooting

### Erro: "Connection refused" no RabbitMQ

**Problema:** Não consegue conectar ao CloudAMQP

**Solução:**
1. Verifique se as credenciais estão corretas no `application.yaml`
2. Confirme se a instância CloudAMQP está ativa
3. Teste a conexão com o script `RabbitMQConnectionTest.java`

### Erro: "Failed to declare queue"

**Problema:** Filas já existem com configurações diferentes

**Solução:**
1. Acesse o CloudAMQP Management Console
2. Delete todas as filas e exchanges manualmente
3. Reinicie a aplicação para recriar

### Erro: "relation does not exist"

**Problema:** Tabelas do banco não foram criadas

**Solução:**
```bash
# Verifique se o Flyway rodou
mvn flyway:info

# Force a migração
mvn flyway:migrate

# Se necessário, limpe e recrie
mvn flyway:clean
mvn flyway:migrate
```

### Pedidos não são processados

**Problema:** Mensagens não chegam aos consumers

**Solução:**
1. Verifique os logs para ver se há erros
2. Confirme se as filas estão com mensagens no RabbitMQ Management
3. Verifique se os listeners estão ativos (procure por `@RabbitListener` nos logs)

### Aplicação não inicia

**Problema:** Erros durante startup

**Solução:**
```bash
# Compile novamente
mvn clean install

# Execute com debug
mvn spring-boot:run -Dspring-boot.run.arguments=--debug

# Verifique as configurações
cat src/main/resources/application.yaml
```

---

## 📝 Notas Importantes

- O sistema simula aprovação de pagamento (70% de chance)
- O sistema simula disponibilidade de estoque (80% de chance)
- Todos os eventos são registrados na tabela de relatório
- DLQs recebem mensagens que falharam após retries
- TTL de retry é de 15 segundos

---

## 👥 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## 📧 Contato

Para dúvidas ou sugestões, abra uma issue no GitHub.