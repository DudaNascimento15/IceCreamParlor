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

