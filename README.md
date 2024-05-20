# Projeto IT-Eletric API

## Sobre o Projeto
O IT-Eletric é um sistema de gerenciamento desenvolvido especificamente para profissionais autônomos do ramo de elétrica. Este sistema facilita o controle de orçamentos, gerenciamento financeiro e de clientes, através de uma interface web moderna e responsiva. O projeto foi desenvolvido como parte do trabalho de conclusão de curso em Engenharia de Software pelo Centro Universitário - Católica de Santa Catarina.

Essa é a API do projeto, que também conta com um front-end, disponível no repositório:

## Tecnologias Utilizadas
- **Linguagem:** Java 21 com Spring Boot
- **Banco de Dados:** PostgreSQL (db_iteletric)
- **Autenticação:** JWT - Bearer Token
- **Testes:** JUnit
- **Deploy:** Heroku

## Funcionalidades Principais
- Gestão de orçamentos com detalhamento de materiais, horas trabalhadas e clientes.
- Controle financeiro com visualização de lucro, faturamento e gastos por períodos.
- Gerenciamento de clientes, incluindo criação de usuários para acesso aos orçamentos.
- Configuração e manutenção de uma Landing Page personalizada.

## Pré-requisitos
Para rodar o projeto, é necessário ter instalado:
- Java JDK 21
- PostgreSQL
- Maven

## Configuração do Banco de Dados
1. Instale o PostgreSQL e crie uma base de dados chamada `db_iteletric`.
2. Configure as credenciais de acesso ao banco no arquivo `application.properties` do Spring Boot:
- spring.datasource.url=jdbc:postgresql://localhost:5432/db_iteletric
- spring.datasource.username=seu_usuario
- spring.datasource.password=sua_senha

3. O Spring Boot está configurado para criar as tabelas automaticamente ao iniciar a aplicação.

## Como Executar o Projeto
1. Navegue até a pasta do projeto.
2. Execute o comando para instalar as dependências do Maven: *mvn clean install*
3. Inicie a aplicação Spring Boot: *mvn spring-boot:run*

## Documentação Adicional
- A documentação completa do API está disponível em Swagger, acessível em `http://localhost:8080/swagger-ui.html` quando o servidor backend está em execução.
- Testes podem ser executados no backend com JUnit usando o comando Maven: *mvn test*


## Postman para teste


## Contato
- Autor: Diego Priess
  - Email: diegopriess.dev@gmail.com
  - Linkedin: https://www.linkedin.com/in/diego-priess-457aab188/

