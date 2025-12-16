# PDFily API

Java • Spring Boot • PostgreSQL • Docker

PDFily API é uma aplicação backend responsável por **upload, conversão e download de arquivos em PDF**, com controle de usuários, autenticação JWT e histórico de downloads.

O sistema permite que usuários autenticados façam upload de arquivos, convertam para PDF, realizem o download dos arquivos convertidos e acompanhem todo o histórico de conversões e downloads de forma segura.

---

## 📑 Sumário

* Visão Geral
* Arquitetura do Projeto
* Tecnologias e Ferramentas
* Camadas do Sistema
* Fluxo da Aplicação
* Banco de Dados
* Variáveis de Ambiente
* Execução do Projeto
* Documentação da API (Swagger / OpenAPI)
* Endpoints Principais
* Boas Práticas e Padrões
* Testes
* Contribuição
* Licença
* Contato

---

## 🔎 Visão Geral

O **PDFily API** foi desenvolvido para resolver o processo completo de conversão de arquivos para PDF, oferecendo:

* Autenticação e autorização com JWT
* Upload seguro de arquivos
* Conversão de arquivos para PDF
* Download de arquivos convertidos
* Histórico completo de downloads por usuário
* Validações e tratamento de erros personalizados

Principais funcionalidades:

* 📤 Upload de arquivos
* 🔄 Conversão para PDF
* 📥 Download de arquivos convertidos
* 📜 Histórico de downloads
* 🔐 Autenticação e segurança baseada em JWT

---

## 🏗 Arquitetura do Projeto

A aplicação segue o padrão de **arquitetura em camadas**, garantindo separação clara de responsabilidades e facilitando manutenção, testes e evolução do sistema.

```text
PDFily/
├── src/
│   ├── main/
│   │   ├── java/com/tomazbr9/pdfily/
│   │   │   ├── auth/
│   │   │   ├── controller/
│   │   │   ├── conversion/
│   │   │   ├── downloadhistory/
│   │   │   ├── fileupload/
│   │   │   ├── security/
│   │   │   ├── user/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │
│   └── test/
│       └── ...
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🧰 Tecnologias e Ferramentas

* Java 17
* Spring Boot 3.x
* Spring Web
* Spring Security
* JWT (JSON Web Token)
* Spring Data JPA
* PostgreSQL
* Lombok
* Docker & Docker Compose
* JUnit 5
* Mockito
* Swagger / OpenAPI
* Spring Boot Actuator
* Prometheus
* Grafana

---

## 🧱 Camadas do Sistema

| Camada     | Responsabilidade                                             |
| ---------- | ------------------------------------------------------------ |
| Controller | Exposição dos endpoints REST e controle das requisições HTTP |
| Service    | Regras de negócio, validações e orquestração de fluxos       |
| Repository | Acesso e persistência de dados via Spring Data JPA           |
| Model      | Entidades mapeadas no banco de dados                         |
| DTO        | Transferência de dados entre camadas e respostas da API      |
| Exception  | Tratamento centralizado de erros                             |
| Security   | Autenticação, autorização, filtros e JWT                     |

---

## 🔄 Fluxo da Aplicação

1. Usuário realiza autenticação
2. Recebe um token JWT
3. Envia arquivos para upload
4. Solicita conversão para PDF
5. Realiza download do arquivo convertido
6. Sistema registra histórico de downloads

---

## 🗄 Banco de Dados

O projeto utiliza **PostgreSQL** como banco de dados principal.

Principais entidades:

### Usuário

* id (UUID)
* username
* password
* roles

### Upload de Arquivo

* id (UUID)
* originalName
* filePath
* size
* user

### Conversão

* id (UUID)
* convertedFileName
* outputPath
* status
* size
* user

### Histórico de Download

* id (UUID)
* conversion
* user
* downloadedAt

---

## ⚙️ Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# Banco de Dados
POSTGRES_DB=pdfily
POSTGRES_USER=postgres
POSTGRES_PASSWORD=senha123

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/pdfily
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=senha123
```

---

## ▶️ Execução do Projeto

### Pré-requisitos

* Java 17
* Maven
* Docker e Docker Compose

### Rodar com Maven

```bash
./mvnw spring-boot:run
```

### Rodar com Docker

```bash
docker-compose up --build
```

### Rodar Testes

```bash
./mvnw test
```

---

## 📘 Documentação da API (Swagger / OpenAPI)

A documentação da API está disponível via Swagger.

### Acessar:

```
http://localhost:8080/swagger-ui/index.html
```

Através do Swagger é possível:

* Visualizar todos os endpoints
* Testar requisições
* Ver exemplos de payloads e respostas

---

## 🔌 Endpoints Principais

| Método | Endpoint                        | Descrição                       |
| ------ | ------------------------------- | ------------------------------- |
| POST   | /api/auth/login                 | Autenticação do usuário         |
| POST   | /api/auth/register              | Registro de usuário             |
| POST   | /api/v1/files/upload            | Upload de arquivo               |
| POST   | /api/v1/convert                 | Conversão para PDF              |
| GET    | /api/v1/download/{conversionId} | Download do arquivo convertido  |
| GET    | /api/v1/download                | Histórico de downloads          |
| DELETE | /api/v1/download/{downloadId}   | Remoção de registro de download |

---

## ✅ Boas Práticas e Padrões

* Arquitetura em camadas
* DTOs para isolamento do domínio
* Tratamento global de exceções com `@ControllerAdvice`
* Princípios SOLID
* Logs com SLF4J
* Segurança com JWT
* Testes unitários e de controller

---

## 📊 Observabilidade e Métricas

O **PDFily API** conta com um sistema de **observabilidade** baseado em **Spring Boot Actuator + Prometheus + Grafana**, permitindo monitorar a saúde e o desempenho da aplicação em tempo real.

### Métricas coletadas

* Uso de CPU e memória
* Tempo de resposta dos endpoints
* Contagem de requisições HTTP
* Status HTTP (2xx, 4xx, 5xx)
* Métricas da JVM (Heap, GC, Threads)

### Prometheus

O Prometheus é responsável por coletar as métricas expostas pelo Actuator.

Endpoint de métricas:

```text
/actuator/prometheus
```

### Grafana

O Grafana é utilizado para visualizar as métricas através de dashboards interativos, possibilitando:

* Identificação de gargalos de performance
* Monitoramento de disponibilidade
* Análise de comportamento da aplicação em produção

### Benefícios

* Observabilidade em tempo real
* Base sólida para ambientes produtivos
* Facilidade para troubleshooting e análise de falhas

---

## ⏱ Limpeza Automática de Arquivos Temporários (Scheduler)

O **PDFily API** utiliza o **Spring Scheduler** para realizar a **limpeza automática de arquivos temporários** em intervalos regulares.

### Como funciona

* Um job agendado é executado **a cada 5 minutos**
* Arquivos temporários antigos são removidos do sistema de arquivos
* Evita acúmulo de arquivos desnecessários em disco
* Melhora performance e uso de armazenamento

### Benefícios

* 🧹 Gerenciamento automático de arquivos temporários
* 📉 Redução de uso de espaço em disco
* ⚙️ Processo totalmente automatizado
* 🚀 Aplicação preparada para uso contínuo em produção

Essa abordagem garante que o sistema mantenha apenas os arquivos necessários, sem intervenção manual.

---

## 🧪 Testes

O projeto possui testes utilizando:

* JUnit 5
* Mockito

Tipos de testes:

* Testes de Service (regras de negócio)
* Testes de Controller (status HTTP e contratos)

Para executar:

```bash
./mvnw test
```

---

## 🤝 Contribuição

Contribuições são bem-vindas!

1. Faça um fork do projeto
2. Crie uma branch para sua feature

```bash
git checkout -b minha-feature
```

3. Commit suas alterações
4. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## 📬 Contato

👨‍💻 Autor: **Bruno Tomaz**
📧 Email: [brunotomaaz@yahoo.com](mailto:brunotomaaz@yahoo.com)
🔗 LinkedIn: [https://www.linkedin.com/in/bruno-tomaz-5232451b2/](https://www.linkedin.com/in/bruno-tomaz-5232451b2/)
🐙 GitHub: [https://github.com/seuusuario](https://github.com/Tomazbr9)
