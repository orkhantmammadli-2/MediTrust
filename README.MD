## MediTrust AI Architecture

MediTrust AI is a healthcare feedback and appointment management platform built using a microservices architecture.

The system follows an API Gateway pattern where all client requests are routed through Spring Cloud Gateway running on port 8080. The gateway is responsible for JWT authentication, Redis-based rate limiting, and Resilience4j circuit breaker protection.

### Core Services

#### Patient Service (Port 8081)

Responsible for patient management operations, including patient registration, retrieval, updates, and deletion. Patient information is stored in PostgreSQL.

#### Appointment Service (Port 8082)

The central service of the platform. It manages appointment creation, verification, feedback collection, and healthcare analytics. This service also provides:

* GraphQL-based dynamic filtering
* WebSocket real-time updates
* Redis caching
* OpenFeign communication with other services

#### Notification Service (Port 8083)

Handles asynchronous communication such as email notifications, webhooks, chatbot integrations, and event-driven messaging using Apache Kafka.

#### AI Service (Port 8085)

Generates healthcare insights using the Gemini API. Monthly reports are produced based on patient feedback, complaint statistics, and hospital performance data.

#### File Service (Port 8088)

Responsible for secure document uploads and file validation. Files are stored in AWS S3 object storage and linked to appointments.

### Infrastructure Components

* Spring Cloud Gateway
* Spring Security with JWT Authentication
* Redis Cache & Rate Limiting
* PostgreSQL Databases
* Apache Kafka Messaging
* WebSocket (STOMP + SockJS)
* GraphQL API
* AWS S3 Storage
* Docker & Docker Compose
* JUnit & Mockito Testing
* GitHub Actions CI/CD
* Resilience4j Circuit Breaker

### System Flow

1. Users interact with the frontend application built using HTML, CSS, and JavaScript.
2. Requests are routed through the API Gateway (localhost:8080).
3. Gateway validates JWT tokens and applies rate limiting and resilience mechanisms.
4. Requests are forwarded to the appropriate microservice.
5. Services communicate using REST APIs and OpenFeign clients.
6. Redis provides caching and performance optimization.
7. Kafka enables asynchronous messaging and event-driven communication.
8. WebSocket pushes real-time appointment updates to connected clients.
9. AWS S3 stores uploaded medical documents.
10. Gemini AI generates monthly healthcare insights and analytics.

### Key Features

* JWT Authentication & Authorization
* Patient Management
* Appointment Management
* File Upload to AWS S3
* Redis Caching
* GraphQL Filtering
* WebSocket Real-Time Updates
* AI-Powered Monthly Insights
* Kafka-Based Messaging
* Microservices Architecture
