# Critérios de Aceitação

## **1. Autenticação**

| ID | Requisito | Critério de Aceitação |
| --- | --- | --- |
| CA01 | Registro de usuário | O usuário deve poder registrar uma conta válida com e-mail único e senha conforme as regras de segurança. |
| CA02 | Login de usuário | O usuário deve poder autenticar-se e receber um token JWT válido, com expiração de 24h. |
| CA03 | Token expirado | Caso o token expire, o sistema deve negar acesso a endpoints protegidos com erro `401 Unauthorized`. |

---

## **2. Conversão de Arquivos**

| ID | Requisito | Critério de Aceitação |
| --- | --- | --- |
| CA05 | Upload de arquivo | O endpoint `/convert` deve aceitar apenas formatos suportados (DOCX, XLSX, PPTX, JPG, PNG, HTML). Arquivos inválidos devem retornar `415 Unsupported Media Type`. |
| CA06 | Armazenamento temporário | Arquivos enviados devem ser armazenados no diretório `/tmp/uploads/` e excluídos automaticamente após 24h. |
| CA07 | Conversão bem-sucedida | Quando a conversão for concluída, o arquivo PDF deve estar disponível para download em `/convert/download/{id}`. |
| CA08 | Conversão com erro | Caso a conversão falhe, o sistema deve registrar o erro e retornar `500 Internal Server Error`, sem gerar arquivo PDF. |
| CA09 | Nome do arquivo | O arquivo convertido deve seguir o padrão `<nome_original>_converted.pdf`. |

---

## **3. Histórico de Conversões**

| ID | Requisito | Critério de Aceitação |
| --- | --- | --- |
| CA10 | Registro no histórico | Após conversão bem-sucedida, o histórico do usuário deve exibir o item com status `success`. |
| CA11 | Registro de falhas | Conversões que falharem devem aparecer no histórico com status `failed`. |
| CA12 | Limite de histórico | O histórico deve conter no máximo 50 registros por usuário (as mais recentes primeiro). |
| CA13 | Remoção de histórico | O usuário deve poder excluir um item específico (`DELETE /history/{id}`) ou todo o histórico (`DELETE /history`). |

---

## **4. Segurança e Validação**

| ID | Requisito | Critério de Aceitação |
| --- | --- | --- |
| CA14 | CORS configurado | Somente domínios autorizados podem acessar a API (verificação via CORS). |
| CA15 | Sanitização de entrada | Nenhum dado recebido deve permitir execução de código malicioso (injeção de script, SQL, etc.). |
| CA16 | Logs de auditoria | Toda conversão, sucesso ou falha, deve ser registrada com timestamp e ID de usuário. |
| CA17 | Erros genéricos | Nenhum erro deve expor detalhes do servidor (stack trace ou mensagens sensíveis). |

---

## **5. Manutenção e Retenção**

| ID | Requisito | Critério de Aceitação |
| --- | --- | --- |
| CA18 | Retenção de logs | Logs devem ser mantidos por 30 dias antes de exclusão automática. |
| CA19 | Limpeza automática | O agendador de limpeza (`TempFileCleaner`) deve remover arquivos expirados a cada 6h. |
| CA20 | Integridade dos arquivos | Nenhum arquivo deve permanecer armazenado após o prazo de expiração. |
