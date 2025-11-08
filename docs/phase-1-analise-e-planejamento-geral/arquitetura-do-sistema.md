# Definição da Arquitetura do Sistema

---

## **1. Objetivo**

Este documento descreve a **arquitetura de software** da aplicação **API Conversora de Documentos para PDF**, estabelecendo sua estrutura de camadas, componentes principais, comunicação entre módulos, padrões adotados e tecnologias envolvidas.

O objetivo é garantir uma base sólida e escalável, alinhada às boas práticas de **engenharia de software**, **qualidade**, e **manutenibilidade**.

---

## **2. Visão Geral da Arquitetura**

A aplicação seguirá o padrão **Arquitetura em Camadas (Layered Architecture)**, adotando uma **estrutura monolítica modular** baseada no **Spring Boot**.

Essa abordagem favorece a organização do código, a testabilidade e a separação clara de responsabilidades, permitindo evolução futura para uma arquitetura distribuída (como microsserviços, se necessário).

---

## **3. Camadas da Arquitetura**

A arquitetura será composta pelas seguintes camadas:

### **3.1 Controller Layer (Interface REST)**

- Responsável por receber requisições HTTP e retornar respostas adequadas (JSON).
- Faz a ponte entre o cliente e a camada de serviço.
- Implementa tratamento de erros e autenticação.

**Principais responsabilidades:**

- Expor endpoints REST.
- Validar entradas do usuário.
- Invocar os métodos de serviço.
- Retornar respostas padronizadas (ResponseEntity).

---

### **3.2 Service Layer (Regras de Negócio)**

- Contém a lógica de negócio e as regras de conversão.
- Interage com os repositórios e classes utilitárias.
- Aplica validações, conversões e tratamento de exceções específicas.

**Principais responsabilidades:**

- Implementar a lógica de conversão de arquivos.
- Gerenciar histórico e status das conversões.
- Aplicar políticas de segurança e controle de acesso.

---

### **3.3 Repository Layer (Acesso a Dados)**

- Interface direta com o banco de dados via **Spring Data JPA**.
- Responsável por operações CRUD e consultas personalizadas.

**Principais responsabilidades:**

- Mapear entidades JPA.
- Gerenciar persistência (PostgreSQL).
- Garantir integridade e consistência dos dados.

---

### **3.4 Configuration Layer**

- Contém as configurações globais do sistema.
- Inclui segurança (JWT, CORS), logs, cache, e variáveis de ambiente.

---

### **3.5 Infra Layer (Infraestrutura)**

- Responsável por lidar com recursos externos, como:
    - Armazenamento de arquivos temporários (`/tmp/uploads`).
    - Integração com serviços cloud (AWS S3, no futuro).
    - Agendamento de tarefas (limpeza de arquivos antigos).

---

## **4. Tecnologias Utilizadas**

| Componente | Tecnologia | Finalidade |
| --- | --- | --- |
| **Linguagem** | Java 17+ | Backend principal |
| **Framework Web** | Spring Boot | Criação da API REST |
| **Banco de Dados** | PostgreSQL | Persistência de dados |
| **ORM** | Spring Data JPA | Mapeamento objeto-relacional |
| **Autenticação** | JWT (JSON Web Token) | Segurança e autenticação de usuários |
| **Logs** | SLF4J + Logback | Monitoramento e rastreamento |
| **Build Tool** | Maven | Gerenciamento de dependências |
| **Containerização** | Docker | Padronização de ambiente |

---

## **5. Padrões de Projeto Adotados**

| Padrão | Aplicação |
| --- | --- |
| **DTO (Data Transfer Object)** | Transferência de dados entre camadas sem expor entidades diretamente |
| **Factory Pattern** | Criação de objetos complexos (ex: manipuladores de conversão) |
| **Strategy Pattern** | Suporte a diferentes tipos de conversão (HTML, DOCX, XLSX, etc.) |
| **Singleton Pattern** | Gerenciamento centralizado de configurações globais |
| **Exception Handler Global** | Captura e tratamento uniforme de erros na API |

---

## **6. Diagrama de Arquitetura (Mermaid)**

```mermaid
flowchart TB
    A[Cliente / Frontend] -->|HTTP (JSON)| B[Controller Layer]
    B --> C[Service Layer]
    C --> D[Repository Layer]
    D --> E[(Banco de Dados PostgreSQL)]
    C --> F[/Infra Layer - File System/]

    subgraph API [API Spring Boot]
    B
    C
    D
    F
    end

    E -.->|persistência| D

```

---

## **7. Comunicação entre Componentes**

| Origem | Destino | Protocolo | Descrição |
| --- | --- | --- | --- |
| Cliente | Controller | HTTP/REST | Envio de requisições à API |
| Controller | Service | Interno (Java) | Chamada de métodos de negócio |
| Service | Repository | JPA / SQL | Acesso e persistência de dados |
| Service | File System | I/O | Armazenamento temporário de arquivos |
| Service | JWT / Security | Interno | Validação de autenticação e autorização |

---

## **8. Considerações Finais**

A arquitetura foi desenhada para:

- **Facilitar manutenção e evolução futura;**
- **Separar claramente as responsabilidades;**
- **Permitir escalabilidade horizontal com Docker e PostgreSQL externo;**
- **Aderir às boas práticas de Clean Architecture e SOLID.**

Essa estrutura servirá de base para a **Fase 2 — Setup do Projeto e Infraestrutura**, onde todo o ambiente será configurado de acordo com as decisões documentadas aqui.

---

Deseja que eu gere também o **diagrama de componentes da arquitetura** (mais detalhado que o atual, mostrando cada camada com seus módulos internos — ex: controller, service, repository, config, etc.) em **Mermaid**?

Esse seria o complemento ideal antes de fechar a Fase 1.