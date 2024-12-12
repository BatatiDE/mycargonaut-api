


# MyCargonaut Backend API

The backend API for MyCargonaut facilitates ride-sharing and freight-sharing services. This repository contains the core logic, authentication, database interaction, and GraphQL APIs required to power the platform.

## Table of Contents

- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Endpoints](#endpoints)
    - [Authentication](#authentication)
    - [Offers](#offers)
    - [Tracking](#tracking)
    - [Reviews](#reviews)
- [GraphQL Support](#graphql-support)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The **MyCargonaut API** provides the backend services required for users to:
- Register and authenticate securely.
- Search for rides and freight offers.
- Create and manage freight/ride requests.
- Track active rides.
- Submit and view reviews for completed rides.
- Query complex data relationships efficiently via GraphQL.

This repository is deployed independently to allow modular development and CI/CD pipelines.

---

## Technologies Used

- **Kotlin**: For concise and type-safe code.
- **Spring Boot**: A robust and scalable framework for building RESTful and GraphQL APIs.
- **GraphQL (GraphQL Java)**: For efficient and flexible querying.
- **PostgreSQL**: Database for persistent data storage.
- **Gradle**: Dependency management and build automation.
- **JWT (JSON Web Tokens)**: For secure authentication.
- **Docker**: For containerized deployments.

---

## Project Structure

```plaintext
src/
├── main/
│   ├── kotlin/com/mycargonaut/backend/
│   │   ├── controllers/   # REST and GraphQL API endpoints
│   │   ├── config/        # Application configuration (e.g., SecurityConfig.kt)
│   │   ├── entities/      # JPA entities for database models
│   │   ├── repositories/  # Interfaces for database queries
│   │   ├── services/      # Business logic and service classes
│   │   ├── dto/           # Data Transfer Objects (DTOs) for safely passing data between layers
│   └── resources/
│       ├── application.properties   # App configuration (database, JWT secrets)
│       ├── db/migration/            # Flyway scripts for database schema
│       ├── graphql/
│       │   ├── schema.graphqls      # GraphQL schema definition
│       │   ├── queries/             # GraphQL query files
│       │   ├── mutations/           # GraphQL mutation files
├── test/                            # Unit and integration tests
```

---

## Setup Instructions

### Prerequisites

- **Java 17+**
- **PostgreSQL** installed and running.
- **Gradle** installed.
- **Docker** (optional for containerized deployment).

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/BatatiDE/mycargonaut-api.git
   cd mycargonaut-api
   ```

2. Configure your environment variables in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/mycargonaut
   spring.datasource.username=<your-db-username>
   spring.datasource.password=<your-db-password>
   jwt.secret=<your-jwt-secret>
   ```

3. Build and run the application:
   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```

4. Access the API at `http://localhost:8080/api` (REST) or `http://localhost:8080/graphql` (GraphQL).

---

## Endpoints

### Authentication

#### REST:
- **POST /api/register**  
  Register a new user.  
  **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```

- **POST /api/login**  
  Authenticate and obtain a JWT.  
  **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```

---

### GraphQL Support

#### GraphQL Endpoint
- Accessible at:  
  `http://localhost:8080/graphql`

#### Example Schema Definition (`schema.graphqls`):
```graphql
type Query {
  getUser(id: ID!): User
  getOffers(filter: OfferFilter): [Offer]
}

type Mutation {
  registerUser(input: RegisterInput): User
  createOffer(input: OfferInput): Offer
}

type User {
  id: ID!
  email: String!
  name: String
}

type Offer {
  id: ID!
  start: String!
  destination: String!
  price: Float!
  date: String!
  user: User!
}

input RegisterInput {
  email: String!
  password: String!
}

input OfferInput {
  start: String!
  destination: String!
  price: Float!
  date: String!
}
```

#### Example Query:
```graphql
query GetOffers {
  getOffers(filter: { start: "Berlin", destination: "Munich" }) {
    id
    start
    destination
    price
    user {
      email
    }
  }
}
```

#### Example Mutation:
```graphql
mutation CreateOffer {
  createOffer(input: { start: "Berlin", destination: "Hamburg", price: 120, date: "2024-12-15" }) {
    id
    start
    destination
    price
  }
}
```

---

## Testing

Run unit, integration, and GraphQL tests using Gradle:

```bash
./gradlew test
```

---

## Contributing

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/new-feature`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/new-feature`).
5. Open a pull request.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
