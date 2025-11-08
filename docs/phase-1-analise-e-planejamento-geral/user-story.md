# User Stories

## Objetivo

Registrar as **histórias de usuário** que representam as principais interações entre o usuário e o sistema de conversão de documentos para PDF, descrevendo funcionalidades sob a ótica do valor entregue.

---

## User Story 01 — Registro de Usuário

**Como** um novo usuário,

**quero** criar uma conta na plataforma,

**para** poder acessar a API e realizar conversões de documentos.

**Critérios de Aceitação**

- Deve ser possível registrar nome, e-mail e senha.
- E-mail já existente deve gerar erro `409 (Conflict)`.
- A senha deve conter no mínimo 8 caracteres, com letras e números.
- Após o registro, o usuário pode autenticar-se normalmente.

**Prioridade:** Alta

**Status:** Planejado

---

## User Story 02 — Autenticação e Token JWT

**Como** um usuário registrado,

**quero** autenticar-me com e-mail e senha,

**para** receber um token JWT e usar a API com segurança.

**Critérios de Aceitação**

- O sistema deve validar credenciais e retornar token JWT válido por 24 horas.
- Tokens expirados devem gerar erro `401 (Unauthorized)`.
- As respostas não devem conter informações sensíveis.

**Prioridade:** Alta

**Status:** Planejado

---

## User Story 03 — Conversão de Arquivo para PDF

**Como** um usuário autenticado,

**quero** enviar um documento em diversos formatos,

**para** convertê-lo em PDF e fazer o download.

**Critérios de Aceitação**

- A API deve aceitar formatos `.docx`, `.pptx`, `.xlsx`, `.html`, `.jpg`, `.png`.
- Arquivos inválidos devem retornar erro `415 (Unsupported Media Type)`.
- Deve ser retornado um link temporário para download do PDF.
- O histórico deve registrar a operação com status `success` ou `failed`.

**Prioridade:** Muito Alta

**Status:** Planejado

---

## User Story 04 — Consultar Histórico de Conversões

**Como** um usuário autenticado,

**quero** visualizar o histórico das minhas conversões,

**para** acompanhar o que já foi processado.

**Critérios de Aceitação**

- O sistema deve listar até 50 conversões mais recentes.
- Cada registro deve conter: nome original, data, status e link (quando válido).
- Registros de erro devem exibir `status = failed`.
- Caso não existam registros, o retorno deve ser uma lista vazia.

**Prioridade:** Média

**Status:** Planejado

---

## User Story 05 — Download de PDF Convertido

**Como** um usuário autenticado,

**quero** baixar o arquivo PDF gerado,

**para** poder utilizá-lo localmente.

**Critérios de Aceitação**

- O link de download deve expirar após tempo definido.
- Caso o arquivo tenha expirado, deve retornar `404 (Not Found)`.
- Apenas o dono do arquivo pode baixá-lo.

**Prioridade:** Média

**Status:** Planejado

---

## User Story 06 — Exclusão de Histórico

**Como** um usuário autenticado,

**quero** excluir meus registros antigos de conversões,

**para** manter o histórico limpo e organizado.

**Critérios de Aceitação**

- Deve ser possível remover registros individualmente ou em lote.
- A operação deve retornar confirmação de exclusão.
- Logs devem registrar a ação do usuário.

**Prioridade:** Baixa

**Status:** Planejado

---

## User Story 07 — Limpeza Automática de Arquivos

**Como** administrador do sistema,

**quero** que arquivos e logs expirados sejam removidos automaticamente,

**para** evitar acúmulo de dados e reduzir custos de armazenamento.

**Critérios de Aceitação**

- Um job deve ser executado periodicamente (configurável).
- Arquivos expirados e logs antigos devem ser removidos.
- A execução deve gerar logs de auditoria.

**Prioridade:** Média

**Status:** Planejado

---

## User Story 08 — Registro de Falhas de Conversão

**Como** o sistema,

**quero** registrar automaticamente falhas de conversão,

**para** que o usuário e o administrador possam identificar problemas.

**Critérios de Aceitação**

- Toda falha deve ser gravada no histórico com status `"failed"`.
- O log deve conter mensagem técnica e timestamp.
- O usuário deve receber uma resposta genérica e segura.

**Prioridade:** Alta

**Status:** Planejado

---

## User Story 09 — Política de Segurança e CORS

**Como** desenvolvedor responsável pela API,

**quero** restringir o acesso a origens autorizadas,

**para** garantir segurança e evitar uso indevido da aplicação.

**Critérios de Aceitação**

- O CORS deve permitir apenas domínios definidos em configuração.
- Requisições não autorizadas devem ser bloqueadas.
- Todas as respostas devem seguir a política de segurança padrão da aplicação.

**Prioridade:** Alta

**Status:** Planejado