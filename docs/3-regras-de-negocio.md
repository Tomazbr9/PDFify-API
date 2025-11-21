# Regras de Negócio

## Introdução

As **regras de negócio** definem as políticas, restrições e condições que controlam o funcionamento da **DocToPDF API**, garantindo coerência com os objetivos do sistema e segurança no uso dos recursos.

Essas regras são responsáveis por determinar **como o sistema deve se comportar em situações específicas**, assegurando a integridade operacional da aplicação.

---

## 1. Regras Gerais

| ID | Regra | Descrição                                                                                                                                                                                      |
| --- | --- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **RN01** | Tipos de arquivos aceitos | A API deve aceitar apenas arquivos nos formatos `.docx`, `.xlsx`, `.pptx`, `.jpg`, `.jpeg`, `.png`, `.html` e `.txt`. Qualquer outro formato deve gerar erro **415 - Unsupported Media Type**. |
| **RN02** | Tamanho máximo | O tamanho máximo permitido para upload de arquivo é **10 MB**. Arquivos acima desse limite devem ser rejeitados com erro **413 - Payload Too Large**.                                          |
| **RN03** | Autenticação obrigatória | Toda solicitação de conversão deve conter um token JWT válido. Requisições sem token devem retornar erro **401 - Unauthorized**.                                                               |
| **RN04** | Tempo de expiração do arquivo | O arquivo convertido deve ser excluído automaticamente do servidor após **5 minutos**.                                                                                                         |
| **RN05** | Download temporário | Caso a conversão gere um link, o mesmo deve ser válido apenas durante o tempo de vida do arquivo (5 minutos).                                                                                  |
| **RN06** | Validação de entrada | Se o arquivo estiver corrompido, ilegível ou em formato inválido, o sistema deve retornar erro **400 - Bad Request**.                                                                          |
| **RN07** | Histórico de conversões | O sistema deve armazenar um histórico de todas as conversões realizadas por cada usuário, incluindo: nome do arquivo, formato de origem, data/hora, status e duração da conversão.             |
| **RN09** | Senhas seguras | As senhas dos usuários devem ser armazenadas com **hash seguro (BCrypt)** e nunca em texto puro.                                                                                               |
| **RN10** | Logs de auditoria | O sistema deve registrar logs de erros, falhas de autenticação e eventos de conversão bem-sucedidos, contendo data, hora e usuário.                                                            |
| **RN11** | Falha na conversão | Caso a conversão falhe (por formato incompatível ou erro interno), o sistema deve retornar **500 - Internal Server Error** com uma mensagem padronizada.                                       |
| **RN12** | Resposta padrão da API | Todas as respostas devem seguir o padrão JSON abaixo:                                                                                                                                          |

```json
{
  "status": "success" ou "error",
  "message": "descrição",
  "data": { ... }
}
```

## 2. Regras de Segurança

| ID | Regra | Descrição |
| --- | --- | --- |
| RS01 | Tokens JWT | O token JWT deve expirar em 24 horas após a autenticação. |
| RS02 | Política de CORS | A API deve permitir apenas origens autorizadas definidas em configuração. |
| RS03 | Upload seguro | Todos os uploads devem ser validados quanto ao tipo MIME real do arquivo, e não apenas pela extensão. |
| RS04 | Sanitização de entrada | Nenhum campo recebido via requisição deve ser executado ou interpretado como código (proteção contra injeção). |
| RS05 | Política de senhas | A senha mínima deve ter 8 caracteres, contendo letras e números. |
| RS06 | Erros genéricos | Mensagens de erro não devem revelar informações sensíveis sobre o servidor, stack trace ou estrutura interna. |

---

## 3. Regras de Negócio Específicas

| ID | Regra | Descrição |
| --- | --- | --- |
| RNE01 | Conversão HTML → PDF | Ao converter HTML, a API deve renderizar o conteúdo visualmente (respeitando CSS básico). |
| RNE02 | Conversão de imagens | Ao converter imagens (JPG, PNG), o sistema deve gerar um PDF com uma imagem por página. |
| RNE03 | Conversão de planilhas | Para arquivos `.xlsx`, todas as planilhas devem ser incluídas no PDF. |
| RNE04 | Conversão de apresentações | Arquivos `.pptx` devem gerar um PDF com uma página por slide. |
| RNE05 | Nome do arquivo convertido | O nome padrão do arquivo convertido deve seguir o formato: `<nome_original>_converted.pdf`. |
| RNE06 | Histórico limitado | O histórico exibido para o usuário deve conter no máximo as últimas 50 conversões (as mais recentes primeiro). |
| RNE07 | Registro de falhas | Conversões com erro também devem aparecer no histórico com o campo `status = "failed"`. |

---

## 4. Políticas de Manutenção e Retenção

| ID | Política | Descrição |
| --- | --- | --- |
| PM01 | Retenção de logs | Logs de auditoria devem ser mantidos por 30 dias. |
| PM02 | Retenção de histórico | O histórico de conversões deve ser mantido por 90 dias e depois removido. |
| PM03 | Limpeza de arquivos | Um job agendado deve executar periodicamente para remover arquivos expirados. |

---

## 5. Fluxos Importantes

### Fluxo de conversão bem-sucedida

1. Usuário envia arquivo autenticado.
2. API valida tipo e tamanho.
3. Sistema processa conversão.
4. PDF é gerado e armazenado temporariamente.
5. Link de download é retornado.
6. Histórico é atualizado.
7. Arquivo é removido após expiração.

---

### Fluxo de erro na conversão

1. Usuário envia arquivo.
2. API detecta formato não suportado.
3. Retorna erro `415`.
4. Log é registrado.
5. Nenhum histórico de sucesso é adicionado.