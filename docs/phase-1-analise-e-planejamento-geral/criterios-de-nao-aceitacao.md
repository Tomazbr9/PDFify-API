# Critérios de não aceitação

## **1. Conversão e Upload**

| ID | Situação | Descrição |
| --- | --- | --- |
| CNA01 | Formatos não suportados | Qualquer tentativa de conversão de formatos não especificados (ex: `.zip`, `.exe`, `.mp4`) não deve ser processada. O sistema deve retornar `415 Unsupported Media Type`. |
| CNA02 | Tamanho excessivo | Arquivos acima do limite configurado (por exemplo, 20 MB) não devem ser aceitos. |
| CNA03 | Conversão parcial | Se o PDF gerado estiver corrompido ou incompleto, o processo **não será considerado concluído com sucesso**. |
| CNA04 | Upload sem autenticação | Enviar arquivos sem token JWT válido deve resultar em `401 Unauthorized`. |
| CNA05 | Reenvio repetido | O sistema não deve sobrescrever conversões anteriores com o mesmo nome de arquivo; deve gerar um novo ID. |

---

## **2. Download e Armazenamento**

| ID | Situação | Descrição |
| --- | --- | --- |
| CNA06 | Arquivos expirados | Arquivos que ultrapassaram o prazo de 24h não devem estar disponíveis para download. |
| CNA07 | Download direto via caminho | O download direto do arquivo físico (sem usar o endpoint `/convert/download/{id}`) **não é permitido**. |
| CNA08 | Download de arquivo inexistente | Solicitações para IDs inválidos ou já removidos devem retornar `404 Not Found`. |

---

## **3. Histórico e Logs**

| ID | Situação | Descrição |
| --- | --- | --- |
| CNA09 | Histórico compartilhado | Um usuário não deve ter acesso ao histórico de outro usuário. |
| CNA10 | Logs incompletos | Conversões sem registro de log (data, usuário e resultado) **não serão consideradas válidas**. |
| CNA11 | Histórico ilimitado | Exibir mais de 50 registros no histórico não será aceito como funcionalidade pronta. |

---

## **4. Segurança**

| ID | Situação | Descrição |
| --- | --- | --- |
| CNA12 | Tokens sem expiração | Tokens JWT sem data de expiração **não serão aceitos**. |
| CNA13 | CORS liberado para todos | Configurações genéricas de CORS (`*`) não serão permitidas. |
| CNA14 | Dados sensíveis em logs | Logs contendo informações como senhas, tokens ou caminhos internos do servidor serão considerados falhas graves. |
| CNA15 | Upload com script embutido | O sistema não deve aceitar arquivos contendo tags de script (ex: `<script>`) sem sanitização prévia. |

---

## **5. Manutenção e Infraestrutura**

| ID | Situação | Descrição |
| --- | --- | --- |
| CNA16 | Falha na limpeza automática | Caso o agendador não remova arquivos expirados dentro do intervalo configurado, o requisito **não será considerado cumprido**. |
| CNA17 | Armazenamento persistente | O uso de diretórios permanentes para guardar arquivos (fora de `/tmp/uploads/` ou bucket temporário) está **fora do escopo do MVP**. |
| CNA18 | Backup automático | O MVP não incluirá sistema de backup ou recuperação de arquivos. |

---

## **6. Escopo Futuro (fora do MVP)**

| ID | Situação | Descrição |
| --- | --- | --- |
| CNA19 | Conversão em lote | O MVP não suportará upload múltiplo de arquivos em uma única requisição. |
| CNA20 | Conversão assíncrona com fila | O processamento assíncrono via fila (RabbitMQ, Kafka, etc.) será avaliado em versões futuras. |
| CNA21 | Interface web | A primeira versão incluirá apenas a API REST; o painel web será desenvolvido após o MVP. |

---

