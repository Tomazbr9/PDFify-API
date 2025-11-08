# Requisitos do Sistema

### 1. Introdução

Esta seção descreve os **requisitos funcionais e não funcionais** do sistema **DocToPDF API**, com o objetivo de detalhar o comportamento esperado da aplicação e suas restrições técnicas.

---

### 2. Requisitos Funcionais (RF)

Os requisitos funcionais definem **o que o sistema deve fazer** — as funcionalidades que entregam valor ao usuário.

| ID | Nome | Descrição |
| --- | --- | --- |
| **RF01** | Upload de arquivo | A API deve permitir o envio de arquivos nos formatos `.docx`, `.xlsx`, `.pptx`, `.jpg`, `.png`, `.html` e `.txt`. |
| **RF02** | Conversão para PDF | O sistema deve converter o arquivo recebido para o formato PDF. |
| **RF03** | Download de arquivo convertido | A API deve retornar o arquivo PDF convertido diretamente ou fornecer uma URL temporária para download. |
| **RF04** | Autenticação de usuário | A API deve exigir autenticação JWT para uso dos endpoints de conversão e histórico. |
| **RF05** | Cadastro de usuário | O sistema deve permitir o registro de novos usuários com email e senha. |
| **RF06** | Histórico de conversões | O sistema deve manter um registro das conversões realizadas por cada usuário. |
| **RF07** | Exclusão automática | O sistema deve excluir arquivos convertidos após 5 minutos para garantir segurança e economia de espaço. |
| **RF08** | Limite de uso diário | Cada usuário poderá realizar até **100 conversões por dia** (para simular um modelo freemium). |
| **RF09** | Retorno de status de conversão | O sistema deve informar o status do processamento (sucesso, erro, formato inválido, tamanho excedido etc.). |
| **RF10** | Documentação da API | A API deve conter documentação interativa via **Swagger/OpenAPI** acessível publicamente. |

---

### 3. Requisitos Não Funcionais (RNF)

Os requisitos não funcionais definem **como** o sistema deve operar — aspectos de qualidade, desempenho, segurança e manutenção.

| ID | Categoria | Descrição |
| --- | --- | --- |
| **RNF01** | Desempenho | A conversão de arquivos de até 5 MB deve ocorrer em menos de 5 segundos. |
| **RNF02** | Escalabilidade | O sistema deve suportar múltiplas conversões simultâneas sem degradação perceptível. |
| **RNF03** | Segurança | Os arquivos devem ser armazenados temporariamente e excluídos após o processamento. Nenhum dado sensível deve ser persistido. |
| **RNF04** | Disponibilidade | O sistema deve garantir pelo menos **99% de uptime** em ambiente de produção. |
| **RNF05** | Documentação | Toda a API deve estar documentada via **Swagger/OpenAPI**. |
| **RNF06** | Testabilidade | O sistema deve conter testes unitários e de integração automatizados. |
| **RNF07** | Portabilidade | A aplicação deve ser executável em containers **Docker**. |
| **RNF08** | Manutenibilidade | O código deve seguir boas práticas de **Clean Code**, **SOLID** e **arquitetura em camadas**. |
| **RNF09** | Confiabilidade | O sistema deve registrar logs detalhados de erros e eventos importantes. |
| **RNF10** | Privacidade | Dados do usuário devem ser protegidos conforme a LGPD. |

---

### 4. Requisitos de Interface

| ID | Descrição |
| --- | --- |
| **RI01** | A API deve expor endpoints RESTful padronizados, com respostas em formato **JSON**. |
| **RI02** | Todos os endpoints protegidos devem exigir header `Authorization: Bearer <token>`. |
| **RI03** | O upload de arquivos será feito via multipart/form-data. |

---

### 5. Requisitos de Dados

| ID | Descrição |
| --- | --- |
| **RD01** | O banco deve armazenar informações básicas do usuário (id, email, senha hash, data de criação). |
| **RD02** | O sistema deve registrar logs de conversão (usuário, nome do arquivo, status, data/hora). |
| **RD03** | Os arquivos convertidos não devem ser salvos permanentemente no banco, apenas os metadados. |

---

### 6. Premissas

- O usuário possui conexão estável com a internet.
- O sistema possui um ambiente backend configurado com Java e dependências instaladas.
- As bibliotecas de conversão suportam os formatos informados.

---

### 7. Restrições

- Tamanho máximo de upload: **10 MB por arquivo**.
- Formatos suportados limitados aos principais tipos de documentos e imagens.
- A API não armazena arquivos permanentemente.
- O tempo máximo de resposta não deve ultrapassar 10 segundos em condições normais.

---

### 8. Critérios de Qualidade

- O código deve passar por **análise estática** (SonarLint, por exemplo).
- Deve haver testes cobrindo **pelo menos 70% das classes de negócio**.
- O Swagger deve estar sempre sincronizado com as rotas reais.
- Toda exceção deve ser tratada com mensagens claras e logs adequados

---