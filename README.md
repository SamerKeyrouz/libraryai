# 📚 LibraryAI

A full-stack AI-powered library management system built with **Spring Boot, PostgreSQL, Angular, Google OAuth2, and Gemini AI**, deployed on Render using Docker.

---

## 🚀 Live Application

🌐 **Deployed App:**  
https://libraryai.onrender.com  

---

## 🧠 Tech Stack

- Spring Boot 3
- PostgreSQL (Render Cloud Database)
- Angular (compiled into static bundle)
- Google OAuth2 Authentication
- Role-Based Access Control (ADMIN / USER)
- Gemini AI (Google Generative AI)
- Docker (multi-stage build)
- Render Cloud Deployment

---

## 🎯 Project Overview

LibraryAI is a cloud-deployed, production-ready web application demonstrating:

- Secure OAuth2 authentication
- Role-based authorization
- RESTful API design
- AI integration using Gemini
- PostgreSQL cloud database
- Docker-based deployment
- Environment-based configuration
- Modern UI with Angular

The system allows users to browse books, borrow and return them, and receive AI-generated book recommendations.

---

## 🔐 Authentication & Authorization

### Google OAuth2 Login

Users authenticate via Google OAuth.

### Role System

- `ROLE_USER` → can borrow / return books
- `ROLE_ADMIN` → can add / edit / delete books

Admin role is assigned via email mapping inside `SecurityConfig`.

---

## 📖 Core Features

### 📚 Book Management
- Add books (Admin only)
- Edit books (Admin only)
- Delete books (Admin only)
- Search by title or author
- Pagination & sorting
- Borrow tracking (who borrowed + timestamp)

### 🔄 Borrow System
- Only authenticated users can borrow
- Only the original borrower can return
- Borrow history stored in database

### 🤖 AI Recommendations
Users can enter natural language queries such as:

> "Suggest psychological thrillers like Gone Girl"

The backend sends the request to Gemini AI and returns structured book recommendations.

---

## 🗄 Database

Production uses:

Render PostgreSQL (Free Tier)

Environment variables are injected securely:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

Hibernate auto-creates and updates tables via:

```
spring.jpa.hibernate.ddl-auto=update
```

---

## ⚙ Environment Variables

All sensitive values are injected via environment variables:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
GEMINI_API_KEY
```

No secrets are hardcoded in the repository.

---

## 🐳 Deployment Architecture

The application is containerized using Docker.

### Key Deployment Features

- Multi-stage Docker build
- Maven build inside container
- Lightweight JRE runtime
- Exposes port 8080
- Binds to Render’s dynamic PORT:

```
server.port=${PORT:8080}
```

---

## 🏗 Project Structure

```
libraryai/
├── src/main/java/com/samer/libraryai
│   ├── config
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── exception
├── src/main/resources
│   ├── application.properties
│   └── static (Angular build output)
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🔒 Security Configuration Highlights

- CORS configured for production domain
- CSRF disabled for API usage
- Role-based endpoint protection
- OAuth2 login flow with authority mapping
- Secure logout configuration

---

## 🧠 AI Integration

The system integrates with **Google Gemini 2.5 Flash**.

Backend flow:

1. Accept user natural-language prompt
2. Send structured request to Gemini
3. Parse AI response
4. Return formatted recommendations to frontend

---

## 💻 Local Development

### 1️⃣ Clone Repository

```
git clone https://github.com/SamerKeyrouz/libraryai.git
```

### 2️⃣ Configure Environment Variables

Set:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
GEMINI_API_KEY
```

### 3️⃣ Run Application

```
./mvnw spring-boot:run
```

Application runs on:

```
http://localhost:8080
```

---

## 🌍 Production Deployment

Hosted on Render:

- Docker-based build
- Cloud PostgreSQL
- Environment variable injection
- HTTPS enabled
- Auto redeploy on Git push

---

## 📌 Design Decisions

- Email-based role bootstrap for demo simplicity
- Environment variables for security
- Docker for production-grade deployment
- PostgreSQL over H2 for realistic persistence
- Clean layered architecture (Controller → Service → Repository)

---

## 📈 What This Project Demonstrates

✔ Full-stack development  
✔ Cloud deployment  
✔ AI integration  
✔ Secure authentication  
✔ Role-based authorization  
✔ Docker containerization  
✔ Production configuration management  

---

## 👨‍💻 Author

Samer Keyrouz  
Software Engineer | Backend & AI Development  
