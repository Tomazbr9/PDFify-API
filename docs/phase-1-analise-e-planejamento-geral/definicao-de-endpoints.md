# Definição dos endpoints da API

## **1. Autenticação**

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/auth/register` | Cria um novo usuário no sistema. |
| `POST` | `/auth/login` | Autentica um usuário e retorna um token JWT. |

---

## **2. Conversão de Arquivos**

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/convert` | Realiza o upload e converte o arquivo para PDF. |
| `GET` | `/convert/status/{id}` | Retorna o status da conversão (em processamento, concluído ou falhou). |
| `GET` | `/convert/download/{id}` | Retorna o arquivo PDF convertido (caso disponível). |

---

## **3. Histórico de Conversões**

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `GET` | `/history` | Retorna o histórico de conversões do usuário autenticado (máx. 50 registros). |
| `DELETE` | `/history/{id}` | Remove um item específico do histórico. |
| `DELETE` | `/history` | Limpa todo o histórico do usuário autenticado. |

---

## **4. Administração**

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `GET` | `/admin/logs` | Retorna logs do sistema (apenas para administradores). |
| `GET` | `/admin/users` | Lista usuários cadastrados. |
| `DELETE` | `/admin/users/{id}` | Remove um usuário do sistema. |

---