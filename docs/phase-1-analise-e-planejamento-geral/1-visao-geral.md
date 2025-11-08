# Visão geral do Sistema

### 1. Nome do Projeto

**DocToPDF API**

### 2. Descrição

A **DocToPDF API** é uma aplicação back-end desenvolvida em **Java + Spring Boot** que tem como objetivo converter diferentes tipos de documentos (Word, Excel, PowerPoint, imagens, HTML, etc.) para o formato **PDF**, oferecendo uma interface REST para integração com sistemas externos.

O projeto tem foco em **boas práticas de engenharia de software**, **qualidade**, **segurança** e **escalabilidade**, sendo ideal para demonstrar habilidades de desenvolvimento e arquitetura em um portfólio profissional.

---

### 3. Objetivo Geral

Fornecer uma API REST segura e eficiente que permita a conversão de arquivos para PDF, facilitando a integração com aplicações web e mobile.

---

### 4. Objetivos Específicos

- Permitir upload de arquivos em múltiplos formatos.
- Realizar a conversão para PDF de forma rápida e segura.
- Retornar o arquivo convertido ou um link de download temporário.
- Proteger rotas com autenticação JWT.
- Documentar toda a API via Swagger.
- Implementar testes automatizados.

---

### 5. Escopo

O projeto abrangerá:

- Backend em **Spring Boot**.
- Conversão de arquivos utilizando bibliotecas Java (ex: Apache POI, iText, Aspose, etc.).
- Upload e armazenamento temporário dos arquivos.
- Documentação técnica e de API.
- Testes unitários e de integração.

**Não incluído no escopo inicial:**

- Interface web (frontend).
- Conversão de vídeo/áudio.
- Integração com provedores de nuvem (ex: S3, GCP Storage).

---

### 6. Stakeholders

| Tipo | Nome/Responsabilidade |
| --- | --- |
| Desenvolvedor | Bruno Tomaz (responsável por todo o desenvolvimento e documentação). |
| Usuário final | Qualquer sistema ou aplicação que utilize a API para converter arquivos em PDF. |
| Administrador  | Bruno Tomaz (Responsável por visualizar logs e monitorar uso da API). |

---

### 7. Tecnologias previstas

| Categoria | Ferramenta |
| --- | --- |
| Linguagem | Java 17+ |
| Framework | Spring Boot |
| Autenticação | JWT |
| Conversão de arquivos | Apache POI / iText / LibreOffice API |
| Banco de Dados | PostgreSQL |
| Testes | JUnit + Mockito |
| Documentação | Swagger / OpenAPI |
| Build | Maven ou Gradle |
| Versionamento | Git + GitHub |

---

### 8. Motivação

Projetos de conversão de documentos são amplamente usados em SaaS, sistemas corporativos e serviços de automação. Criar essa API demonstra domínio de **arquitetura REST**, **tratamento de arquivos**, **autenticação**, **boas práticas de código** e **qualidade de software**, o que é excelente para um portfólio.