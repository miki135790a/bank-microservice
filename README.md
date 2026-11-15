# 🏦 Bank Microservice

Банковский микросервис с **Event-Driven Architecture** на Spring Boot и Apache Kafka.  
Поддерживает полный CRUD для клиентов и счетов, а также **асинхронные переводы** между счетами через **Transactional Outbox Pattern**.

---

## 📋 Содержание

- [Стек технологий](#-стек-технологий)
- [Архитектура](#-архитектура)
- [Ключевые особенности](#-ключевые-особенности)
- [Быстрый старт](#-быстрый-старт)
- [API Эндпоинты](#-api-эндпоинты)
- [Сценарий: Полный цикл перевода](#-сценарий-полный-цикл-перевода)


---

## 🛠 Стек технологий

- Java 21
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- PostgreSQL
- Flyway
- Apache Kafka
- Docker & Docker Compose
- Lombok
- Jackson
- Bean Validation

---


## ✨ Ключевые особенности

- ✅ **Transactional Outbox Pattern** — гарантия доставки событий в Kafka.
- ✅ **Пессимистичные блокировки** (`SELECT FOR UPDATE`) — защита от race condition.
- ✅ **Deadlock Prevention** — блокировка счетов по возрастанию `id`.
- ✅ **Идемпотентность Consumer'а** — проверка `status == PENDING` перед обработкой.
- ✅ **Dead Letter Queue (DLQ)** — защита от бесконечных ретраев.
- ✅ **Rich Domain Model** — бизнес-логика в сущностях (`debit`, `credit`, `close`).
- ✅ **Auditing** — `createdAt`/`updatedAt` через Spring Data JPA Auditing.
- ✅ **Global Exception Handler** — единый формат ошибок (`{code, message, fields}`).
- ✅ **Кастомная валидация** — `@ValidCurrency` через `ConstraintValidator`.
- ✅ **Flyway** — миграции БД (V1–V6).
- ✅ **Sequence** (не `BIGSERIAL`) — прод-стиль для PostgreSQL.
- ✅ **Docker Compose** — Postgres + Kafka + BankApp одной командой.

---

## 🚀 Запуск

### 1. Клонировать репозиторий

```bash
git clone https://github.com/your-username/bank.git
cd bank
```

### 2. Создать `.env` (в корне проекта)

```env
DB_USERNAME=your_username
DB_PASSWORD=your_password
```


### 3. Запустить всё одной командой

```bash
docker-compose up -d --build
```





### 4. Проверить, что API отвечает

```bash
curl http://localhost:8080/api/clients
```

---

## 📚 API Эндпоинты

### 👤 Клиенты (`/api/clients`)

| Метод | Эндпоинт | Описание |
| :--- | :--- | :--- |
| `POST` | `/api/clients` | Создать клиента |
| `GET` | `/api/clients/{id}` | Получить клиента по ID |
| `GET` | `/api/clients` | Получить всех клиентов |
| `PATCH` | `/api/clients/{id}` | Частично обновить клиента |
| `DELETE` | `/api/clients/{id}` | Удалить клиента |

**Пример: Создать клиента**
```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Иван Иванов","email":"ivan@example.com"}'
```

**Ответ:**
```json
{
  "id": 1,
  "fullName": "Иван Иванов",
  "email": "ivan@example.com",
  "createdAt": "2026-11-15T10:00:00",
  "updatedAt": null
}
```

---

### 💳 Счета (`/api/clients/{clientId}/accounts`)

| Метод | Эндпоинт | Описание |
| :--- | :--- | :--- |
| `POST` | `/api/clients/{clientId}/accounts` | Открыть счет |
| `GET` | `/api/clients/{clientId}/accounts` | Получить счета клиента |
| `POST` | `/api/clients/{clientId}/accounts/{accountId}/close` | Закрыть счет |

**Пример: Открыть счет**
```bash
curl -X POST http://localhost:8080/api/clients/1/accounts \
  -H "Content-Type: application/json" \
  -d '{"number":"12345678901234567890","currency":"RUB"}'
```

**Ответ:**
```json
{
  "id": 1,
  "clientId": 1,
  "number": "12345678901234567890",
  "balance": 0.00,
  "currency": "RUB",
  "status": "ACTIVE",
  "createdAt": "2026-11-15T10:00:00",
  "updatedAt": null,
  "closedAt": null
}
```

---

### 💸 Переводы (`/api/transfers`, `/api/operations`)

| Метод | Эндпоинт | Описание |
| :--- | :--- | :--- |
| `POST` | `/api/transfers` | Инициировать перевод (асинхронно) |
| `GET` | `/api/transfers/{id}` | Проверить статус перевода |

**Пример: Инициировать перевод**
```bash
curl -X POST http://localhost:8080/api/transfers \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":300.00}'
```

**Ответ (`202 Accepted`):**
```json
{
  "operationId": 1,
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 300.00,
  "status": "PENDING",
  "createdAt": "2026-11-15T10:00:00"
}
```


```bash
curl http://localhost:8080/api/transfers/1
```

**Ответ:**
```json
{
  "id": 1,
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 300.00,
  "type": "TRANSFER",
  "status": "COMPLETED",
  "failureReason": null,
  "createdAt": "2026-11-15T10:00:00"
  
}
```

---

## 🧪 Сценарий: Полный цикл перевода

```bash
# 1. Создать клиента
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Иван","email":"ivan@example.com"}'

# 2. Открыть два счета
curl -X POST http://localhost:8080/api/clients/1/accounts \
  -H "Content-Type: application/json" \
  -d '{"number":"12345678901234567890","currency":"RUB"}'

curl -X POST http://localhost:8080/api/clients/1/accounts \
  -H "Content-Type: application/json" \
  -d '{"number":"12345678901234567891","currency":"RUB"}'

# 3. Пополнить баланс (через Docker)
docker exec -it bank-postgres psql -U your_username -d bank \
  -c "UPDATE accounts SET balance = 1000.00 WHERE id = 1;"

# 4. Инициировать перевод
curl -X POST http://localhost:8080/api/transfers \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":300.00}'

# 5. Проверить статус (через 2-5 сек)
curl http://localhost:8080/api/transfers/1

# 6. Проверить балансы
docker exec -it bank-postgres psql -U your_username -d bank \
  -c "SELECT id, balance FROM accounts;"
# Ожидаемо: id=1 → 700.00, id=2 → 300.00
```



