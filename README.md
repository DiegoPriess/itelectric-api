# Projeto IT-Electric API

## Sobre o Projeto
O IT-Electric é um sistema de gerenciamento desenvolvido especificamente para profissionais autônomos do ramo de elétrica. Este sistema facilita o controle de orçamentos, gerenciamento financeiro e de clientes, através de uma interface web moderna e responsiva. O projeto foi desenvolvido como parte do trabalho de conclusão de curso em Engenharia de Software pelo Centro Universitário - Católica de Santa Catarina.

## Objetivo
Este software tem como objetivo desenvolver uma ferramenta que auxilie eletricistas na gestão de seus projetos, proporcionando os seguintes benefícios:
- Aumento da produtividade: Automatização de tarefas repetitivas e redução do tempo gasto na elaboração de orçamentos.
- Melhora na organização: Centralização de informações e fácil acesso aos dados de cada projeto.
- Redução de erros: Padronização de processos e minimização de riscos de erros manuais.
- Satisfação do cliente: Agilidade na entrega de orçamentos e acompanhamento transparente do projeto através de envio de emails.

## Boas práticas de desenvolvimento aplicadas
- **Handler de Erros**: Implementação de um handler de erros para tratar todas as exceções geradas pela API e retornar mensagens amigáveis e padronizadas. Isso melhora a experiência do usuário e facilita a identificação e resolução de problemas.
- **BaseModel**: Criação de uma `BaseModel` reutilizável por todas as models, fornecendo um padrão consistente na criação de tabelas no banco de dados. Isso melhora a escalabilidade e a organização do sistema.
- **Testes Unitários com Mockito e JUnit**: Testes unitários implementados com Mockito e JUnit, garantindo a qualidade e confiabilidade do código. Essa abordagem permite isolar componentes e simular comportamentos para testar diferentes cenários de forma eficiente.
- **Envio de E-mails Assíncrono**: Envio de e-mails configurado de forma assíncrona, garantindo que a operação de envio não impacte o desempenho do sistema, especialmente em processos que envolvem múltiplos destinatários ou grande volume de mensagens.
- **Autenticação com Token JWT**: Implementação de autenticação utilizando token JWT para garantir que apenas usuários autenticados possam acessar as funcionalidades protegidas da aplicação. O sistema valida e gerencia os tokens de forma segura para cada requisição.
- **Permissões de Rotas com Base nas Credenciais de Usuário**: As permissões de acesso às rotas são controladas com base nas credenciais do usuário, permitindo que apenas usuários com as permissões adequadas possam acessar determinadas funcionalidades do sistema. As rotas são protegidas e adaptadas de acordo com o perfil do usuário (ex: eletricista, cliente).

Esse é o BACK-END do projeto, que também conta com um front-end disponíveL no repositório: [FRONT-END](https://github.com/DiegoPriess/itelectric-app)

### Dashboard Kanban [Jira](https://diegopriessdev.atlassian.net/jira/software/projects/KAN/boards/1)

## Arquitetura C4
- [NV.1](https://drive.google.com/file/d/1cjtWxbsxG7hVC8DEFkOR6WQd6ZeGkPWz/view?usp=sharing)
- [NV.2](https://drive.google.com/file/d/1IeZX3G9YlAU1yuloMuRd6zc5fc9VUpQw/view?usp=sharing)
- [NV.3](https://drive.google.com/file/d/1vO7s7wQtwONgZgGq_Q49tUo-yFIXrOxQ/view?usp=sharing)
- [NV.4](https://drive.google.com/file/d/1lFf0mzdBJF32mfsSrSgtDDfgtEhdT7fF/view?usp=sharing)

## Requisitos funcionais
| Identificação | Requisito Funcional                                                                 | Descrição                                                                                                                                      |
|---------------|--------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| **RF.1**      | **Gerenciar Trabalhos**                                                              | Permitir o cadastro, edição e exclusão de trabalhos realizados pelos eletricistas.                                                              |
| **RF.2**      | **Gestão de Material**                                                               | Controlar os materiais utilizados nos orçamentos e serviços prestados, incluindo a quantidade e o custo.                                        |
| **RF.3**      | **Gerenciar Orçamento**                                                              | Criar, editar e excluir orçamentos, além de listar orçamentos existentes.                                                                       |
| **RF.4**      | **Aprovação e Negação de Orçamentos**                                                 | Permitir que os eletricistas aprovem ou neguem orçamentos realizados para os clientes.                                                          |
| **RF.5**      | **Login**                                                                            | Sistema de login para os eletricistas e clientes, garantindo segurança no acesso.                                                              |
| **RF.6**      | **Registrar**                                                                         | Permitir o registro de novos usuários (eletricistas e clientes) no sistema.                                                                    |
| **RF.7**      | **Geração de Credenciais para Clientes Cadastrados em Orçamentos**                   | Gerar credenciais de acesso para clientes que tiverem orçamentos cadastrados.                                                                  |
| **RF.8**      | **Envio de E-mail com Credenciais**                                                  | Enviar e-mails automáticos para os clientes com suas credenciais de acesso ao sistema.                                                         |
| **RF.9**      | **Envio de E-mail e Notificação de Novo Orçamento**                                  | Enviar e-mails e notificações aos clientes quando um novo orçamento for registrado ou atualizado.                                              |
| **RF.10**     | **Dashboard de Cliente com Informações de Todos Seus Orçamentos**                    | Fornecer um dashboard para os clientes com informações detalhadas sobre todos os seus orçamentos, mesmo quando realizados por diferentes eletricistas. |

## Requisitos não funcionais
| Identificação | Requisito Não Funcional                                                                | Descrição                                                                                                                              |
|---------------|----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| **RNF.1**     | **Autenticação com Token JWT**                                                         | O sistema deve utilizar autenticação baseada em token JWT para garantir segurança no acesso às funcionalidades.                        |
| **RNF.2**     | **Validação de Credenciais de Usuário**                                                | O sistema deve validar as credenciais de usuários (eletricistas e clientes) para garantir que apenas usuários autenticados acessem o sistema. |
| **RNF.3**     | **Experiência do Usuário**                                                            | O sistema deve oferecer uma experiência de usuário intuitiva e fluida, com interfaces amigáveis e tempos de resposta rápidos.           |
| **RNF.4**     | **Criptografia bcrypt**                                                                | As senhas dos usuários devem ser criptografadas utilizando o algoritmo bcrypt, garantindo a segurança dos dados sensíveis.              |

## Tecnologias Utilizadas
- **Linguagem:** Java 21 com Spring Boot
- **Banco de Dados:** PostgreSQL
- **Autenticação:** JWT - Bearer Token
- **Criptografia** BCrypt
- **Testes:** JUnit/Mockito
- **Deploy:** AWS
- **Qualidade:** [SonarCloud](https://sonarcloud.io/project/overview?id=DiegoPriess_iteletric-api)
- **Monitoramento** [Datadog](https://app.datadoghq.com/dashboard/ivz-3u3-cdk/api?fromUser=false&refresh_mode=sliding&from_ts=1732843123031&to_ts=1732846723031&live=true)

## Contato
- Autor: Diego Priess
  - Email: diegopriess.dev@gmail.com
  - Linkedin: https://www.linkedin.com/in/diego-priess-457aab188/

## Como Rodar o Projeto Localmente

Para rodar o backend do projeto localmente e começar a desenvolver, siga os passos abaixo:

### Pré-requisitos

Antes de começar, você precisa ter as seguintes ferramentas instaladas:

- **Java 21** ou superior
  - [Baixe e instale o JDK do Java](https://adoptopenjdk.net/)
- **Maven** para gerenciar as dependências do projeto
  - [Baixe e instale o Maven](https://maven.apache.org/)
- **PostgreSQL** para banco de dados
  - [Baixe e instale o PostgreSQL](https://www.postgresql.org/download/)
- **Postman** (opcional, para testar as APIs)
  - [Baixe o Postman](https://www.postman.com/)

### Passos para Rodar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/DiegoPriess/iteletric-api.git
   cd iteletric-api
   ```

2. **Configure o Banco de Dados Local**
   ```bash
   psql -U postgres
   CREATE DATABASE iteletric;
   ```

3. **Configuração do Banco de Dados no application.properties**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/iteletric
   spring.datasource.username=postgres
   spring.datasource.password=senha_do_postgres
   spring.jpa.hibernate.ddl-auto=update
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.properties.hibernate.format_sql=true
   spring.jpa.show-sql=true
   ```

4. **Instale as dependências**
   ```bash
   mvn clean install
   ```

5. **Configuração de E-mails**
   ```properties
   spring.mail.host=smtp.seuprovedor.com
   spring.mail.port=587
   spring.mail.username=seuemail@dominio.com
   spring.mail.password=suasenha
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   spring.mail.properties.mail.smtp.ssl.trust=smtp.seuprovedor.com
   ```

6. **Execute o backend**
- Vá até o arquivo IteletricApiApplication e execute-o, se preferir crie um runner para roda-lo
