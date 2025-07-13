## 📚 API Documentation

Financial Tracker API is a secure, scalable backend built with Spring Boot designed to manage personal finances efficiently. It allows users to register, authenticate, and track both expected and real financial transactions tied to monthly budgets. The API uses JWT-based authentication for secure access and follows clean architecture principles for maintainability and scalability.
## ⚙️ Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3
- **Security:** Spring Security with JWT authentication
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **API Documentation:** Swagger / OpenAPI 3.0
- **Testing:** JUnit (future scope)
- **Other:** Lombok, Hibernate, Bean Validation (JSR-380)

---

## ✨ Features

- User registration and login with secure password hashing (BCrypt)
- JWT token-based authentication and role-based authorization
- CRUD operations for financial transactions and budget plans
- Separate tracking of expected vs real transactions
- Pagination support on transaction and budget list endpoints
- Password update and user profile retrieval
- Dashboard endpoints to summarize financial data
- Well-defined RESTful API with Swagger documentation
- CORS enabled for frontend integration

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.x
- PostgreSQL 12+
- (Optional) Docker for containerization

### Installation

1. **Clone the repo**

```bash
git clone https://github.com/LahNabil/financial_tracker_api.git
cd financial_tracker_api

### 🔐 Authentication Endpoints

| Method | Endpoint               | Description                    | Request Body                             | Response                     |
|--------|------------------------|--------------------------------|----------------------------------------|------------------------------|
| POST   | `/api/v1/auth/register`| Register a new user             | `RegistrationRequest` (email, firstname, lastname, password) | 202 Accepted (empty object)  |
| POST   | `/api/v1/auth/authenticate` | Authenticate user & get JWT token | `AuthenticationRequest` (email, password) | `AuthenticationResponse` (token string) |

### 💸 Budget Plan Endpoints

| Method | Endpoint                     | Description                         | Request Body                | Response                         |
|--------|------------------------------|-----------------------------------|-----------------------------|---------------------------------|
| GET    | `/api/v1/budget/`            | List all budget plans (supports paging) | Query params: `page`, `size` | Paginated list of `BudgetPlanDto` |
| POST   | `/api/v1/budget/`            | Create a new budget plan           | `BudgetPlanDto`             | UUID of created budget plan     |
| GET    | `/api/v1/budget/{id}`        | Get a budget plan by ID            |                             | `BudgetPlanDto`                 |
| PUT    | `/api/v1/budget/{id}`        | Update an existing budget plan     | `BudgetPlanDto`             | Updated `BudgetPlanDto`         |
| DELETE | `/api/v1/budget/{id}`        | Delete a budget plan               |                             | 200 OK                         |
| GET    | `/api/v1/budget/current-month/with-transactions` | Get current month budget with transactions |                             | `BudgetChartDataDto`            |

### 📊 Transaction Endpoints

| Method | Endpoint                          | Description                         | Request Body            | Response                     |
|--------|----------------------------------|-----------------------------------|-------------------------|------------------------------|
| POST   | `/api/v1/transaction/{budgetPlanId}` | Create a transaction for a budget plan | `TransactionDto`        | UUID of new transaction       |
| GET    | `/api/v1/transaction/{id}`       | Get transaction by ID              |                         | `TransactionDto`             |
| PUT    | `/api/v1/transaction/{id}`       | Update a transaction               | `UpdateTransactionDto`  | Updated `TransactionDto`     |
| DELETE | `/api/v1/transaction/{id}`       | Delete a transaction               |                         | 200 OK                      |
| GET    | `/api/v1/transaction/budget/{budgetId}` | List transactions for a budget (with paging) | Query params: `page`, `size` | Paginated list of `TransactionDto` |
| GET    | `/api/v1/transaction/total-income/{budgetId}` | Get total income for a budget     |                         | Numeric total income          |
| GET    | `/api/v1/transaction/total-expense/{budgetId}`| Get total expenses for a budget   |                         | Numeric total expenses        |
| GET    | `/api/v1/transaction/remaining-budget/{budgetId}` | Get remaining budget amount       |                         | Numeric remaining budget      |

### 👤 Profile Endpoint

| Method | Endpoint            | Description                 | Response                    |
|--------|---------------------|-----------------------------|-----------------------------|
| GET    | `/api/v1/profile/`  | Get current user's profile   | `ProfileResponse` (firstname, lastname, email) |

| PUT    | `/api/v1/profile/password` | Update user password        | `UpdatePasswordRequest` (oldPassword, newPassword) | 200 OK                      |

### 📊 Dashboard Endpoints

| Method | Endpoint                  | Description                 | Response                    |
|--------|---------------------------|-----------------------------|-----------------------------|
| GET    | `/api/v1/dashboard/`      | Get summary dashboard data   | `DashboardResponseDto`       |
| GET    | `/api/v1/dashboard/line`  | Get transaction comparison   | `TransactionComparisonDto`   |

---

### 🔧 Request & Response Models

- **TransactionDto**: Represents a transaction with fields like `title`, `amount`, `date`, `status` (`REAL` or `EXPECTED`), `category` (e.g., `GROCERIES`, `RENT_MORTGAGE`), and `type` (`INCOME` or `EXPENSE`).
- **BudgetPlanDto**: Represents a monthly budget with `initialIncome`, `month`, and `year`.
- **AuthenticationRequest/Response**: Used for login and JWT token retrieval.
- **RegistrationRequest**: Used to register a new user.
- **ProfileResponse**: User info excluding sensitive data.
- **UpdatePasswordRequest**: Payload to change a user’s password securely.

---

### 🔒 Security

- All sensitive endpoints require JWT Bearer token in `Authorization` header.
- Passwords are securely stored using BCrypt hashing.
- Roles and authorization checks are implemented in the backend services.

---

### 🔗 API Servers

| Environment | URL                      |
|-------------|--------------------------|
| Local       | `http://localhost:8081`  |
| Production  | Not Yet                  |

---

### 🛠️ How to Explore the API Locally

- Start your backend
- Visit Swagger UI (if available) at: `http://localhost:8081/swagger-ui/index.html`
- Try endpoints, authenticate to get JWT, and test protected routes

---
