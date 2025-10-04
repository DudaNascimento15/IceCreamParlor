# IceCreamParlor

Link para repositório: https://github.com/DudaNascimento15/IceCreamParlor.git


## Dependencias
Spring Web
Spring AMQP
Spring Data JPA
Lombok (facilita muito com getters/setters)

## Banco de dados 
PostgreSQL Driver

### 
CloudRabbitMq: https://api.cloudamqp.com/console/2be18ec4-ba36-4edd-9b18-7766e25f7c3b/config


# COMO RODAR
1. Clonar o repositório
2. Criar o usuário e banco de dados no PostgreSQL
   CREATE ROLE icecream_user LOGIN PASSWORD 'admin';
   ALTER DATABASE sorveteria OWNER TO icecream_user;
   GRANT ALL PRIVILEGES ON DATABASE sorveteria TO icecream_user;
3. Na raiz do projeto executar o comando:
   .\mvnw clean package
.\mvnw spring-boot:run


# Passo a passo para rodar o IceCreamParlor com RabbitMQ
## 1️⃣ Pré-requisitos

Java 17+ (o projeto está em Spring Boot 3.x, que requer Java 17 ou superior).

Maven (ou use ./mvnw que já vem no projeto).

Conta no CloudAMQP (plano free “Little Lemur” já é suficiente).
👉 https://www.cloudamqp.com/

## #️⃣ Criar instância RabbitMQ no CloudAMQP

Acesse o CloudAMQP
.
Crie uma instância (plano gratuito).

Você vai receber uma URL de conexão parecida com essa:

amqps://<username>:<password>@<host>/<vhost>

## 3️⃣ Configurar o projeto

Edite o arquivo application.yml ou application.properties e adicione suas credenciais do CloudAMQP:

spring:
rabbitmq:
addresses: amqps://<username>:<password>@<host>/<vhost>
virtual-host: <vhost>
username: <username>
password: <password>
ssl:
enabled: true

## ⚠️ Se rodar RabbitMQ localmente, basta instalar com Docker:

docker run -d --hostname rabbit --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management


Painel: http://localhost:15672

Usuário padrão: guest / guest
