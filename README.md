# 👶 Carioquinha Rio

Sistema de gerenciamento de **bebês e maternidades**, com registro e auditoria de dados, desenvolvido em **Java + Quarkus**.

---

## 📌 Sobre o Projeto

O **Carioquinha Rio** é uma API REST responsável por:

* 👶 Cadastro de bebês
* 🏥 Consulta de maternidades
* 📝 Registro de logs de auditoria
* ⚙️ Tratamento global de exceções
* 🧪 Testes automatizados

O projeto segue uma arquitetura organizada em **Controller → Service → Repository → Domain**, utilizando boas práticas de separação de responsabilidades.

---

## 🛠️ Tecnologias Utilizadas

* ☕ Java 17+
* 🚀 Quarkus
* 📦 Maven
* 🗄️ JPA / Hibernate
* 🧪 JUnit 5
* 🐳 Docker (configuração preparada)

---

## 📂 Estrutura do Projeto

```
src/
 ├── main/
 │   ├── java/
 │   │   ├── controller/
 │   │   ├── service/
 │   │   ├── repository/
 │   │   ├── domain/
 │   │   ├── dto/
 │   │   ├── error/
 │   │   └── Main.java
 │   └── resources/
 │       └── application.properties
 └── test/
     └── java/
```

### 🔹 Camadas

* **Controller** → Endpoints da API
* **Service** → Regras de negócio
* **Repository** → Persistência de dados
* **Domain** → Entidades do sistema
* **DTO** → Objetos de transferência
* **Error** → Tratamento global de exceções

---

## 🚀 Como Executar o Projeto

### 🔧 Pré-requisitos

* Java 17+
* Maven 3.8+
* Docker (opcional)

---

### ▶️ Rodando em modo desenvolvimento

```bash
./mvnw quarkus:dev
```

Ou no Windows:

```bash
mvnw.cmd quarkus:dev
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

---

### 📦 Gerar o pacote

```bash
./mvnw clean package
```

Executar o JAR:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## 📡 Endpoints Principais

### 👶 Bebês

* `POST /bebes` → Cadastrar bebê
* `GET /bebes` → Listar bebês
* `GET /bebes/{id}` → Buscar por ID

### 🏥 Maternidades

* `GET /maternidades` → Listar maternidades

---

## 🧪 Executar Testes

```bash
./mvnw test
```

Os testes incluem:

* ✔️ Testes de Service
* ✔️ Testes de Integração
* ✔️ Testes de Recursos REST

---

## ⚠️ Tratamento de Erros

A aplicação possui um **GlobalExceptionMapper** responsável por:

* Padronizar respostas de erro
* Retornar mensagens claras
* Garantir consistência na API

Exemplo de resposta de erro:

```json
{
  "status": 400,
  "message": "Dados inválidos",
  "timestamp": "2026-02-25T10:00:00"
}
```


## 📖 Boas Práticas Aplicadas

* ✔️ Separação de responsabilidades
* ✔️ Uso de DTOs
* ✔️ Tratamento global de exceções
* ✔️ Testes automatizados
* ✔️ Estrutura limpa e organizada

---

## 👨‍💻 Autor

Projeto desenvolvido para fins acadêmicos e de prática em arquitetura REST com Quarkus.
