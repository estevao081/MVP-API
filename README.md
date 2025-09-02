# NeoProjectAPI

## 📌 Sobre o projeto
O **NeoProjectAPI** é um MVP de uma **API REST** desenvolvida em **Spring Boot** para o cadastro e gerenciamento de clientes pessoa física.  
A aplicação possibilita **incluir, editar, listar e excluir** clientes, garantindo operações completas de CRUD.  

O projeto inclui:
- **Testes unitários** e **testes de integração** para validação da regra de negócio e endpoints.
- Deploy em produção via **[Render](https://render.com/)**.

---

## ⚙️ Funcionalidades
- ✅ Inclusão de novos clientes  
- ✅ Atualização de clientes existentes  
- ✅ Exclusão de clientes  
- ✅ Listagem paginada de clientes  

---

## 🛠️ Tecnologias utilizadas
- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **H2 Database (ambiente de desenvolvimento)**
- **JUnit 5 & Mockito** (testes)
- **Maven**

---

## 📂 Estrutura principal
```
src/main/java/com/example/neoprojectapi
 ┣ controllers/   -> Endpoints REST
 ┣ services/      -> Regras de negócio
 ┣ repositories/  -> Interfaces JPA
 ┗ models/        -> Entidades do sistema

src/test/java/com/example/neoprojectapi
 ┣ unit/          -> Testes unitários
 ┗ integration/   -> Testes de integração
```

---

## 🚀 Como executar o projeto

### Pré-requisitos
- **Java 21+**
- **Maven 3.8+**
- **Docker (opcional, para banco externo)**

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/estevao081/MVP-API.git
   cd neoProjectAPI
   ```

2. Compile e rode a aplicação:
   ```bash
   mvn spring-boot:run
   ```

3. Acesse no navegador:
   ```
   http://localhost:8080
   ```

---

## 🧪 Rodando os testes
Para executar todos os testes unitários e de integração:
```bash
mvn test
```

---

## 🌐 Deploy
O projeto está disponível em produção via **Render**.  
```
SWAGGER https://neo-mvp-api.onrender.com/swagger-ui/index.html
```

---

## 📄 Licença
Este projeto é de uso livre para fins de estudo e evolução profissional.
