# School ERP Backend (MVP)

A robust and secure School Management System built with **Spring Boot 3**, designed to digitize various academic and administrative operations.

## 🚀 Features

- **Advanced Security**: 
  - JWT-based Stateless Authentication.
  - Strict Role-Based Access Control (RBAC) with `ROLE_ADMIN`, `ROLE_TEACHER`, and `ROLE_STUDENT`.
  - Method-level security for fine-grained protection.
- **core Modules**:
  - **Academic Management**: Define Classes, Sections, and Subjects.
  - **Student Information System**: admission, profiles, and class associations.
  - **Attendance Module**: Daily tracking by teachers with student portal history.
  - **Exam & Marks**: Schedule-based examinations with automated validation against max marks.
  - **Fee Management**: Categories (Heads), structured class fees, and student payment ledgers.
  - **Timetable Module**: Weekly schedule management with **Conflict Detection** (prevents teacher or class overlaps).

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.3+
- **Security**: Spring Security, JWT (JJWT)
- **Database**: MySQL 8.x
- **ORM**: Spring Data JPA / Hibernate
- **Utilities**: Lombok, Jakarta Validation
- **Build Tool**: Maven

## 📋 Installation & Setup

### Prerequisites
- JDK 17 or higher
- MySQL Server
- Maven

### Configuration
1. Create a database named `school_erp_db` in MySQL.
2. Open `src/main/resources/application.properties` and update the datasource credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/school_erp_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8081`.

## 🔑 Default Credentials
On the first run, the system seeds a default administrator:
- **Username**: `admin`
- **Password**: `password`

## 🧪 Testing with Postman
A comprehensive Postman collection is included in the root directory:
`School-ERP-Postman-Collection.json`

**How to use:**
1. Import the collection into Postman.
2. Run the **Login (Admin)** request first. 
3. The collection script will automatically save the JWT token to an environment variable.
4. All subsequent requests will automatically use this token for authentication.

## 📂 Project Structure
- `com.school.config`: Project configurations.
- `com.school.controller`: REST Controllers.
- `com.school.dto`: Request/Response Data Transfer Objects.
- `com.school.model`: JPA Entities & Enums.
- `com.school.repository`: Data Access Layer.
- `com.school.service`: Business Logic Layer.
- `com.school.security`: JWT Filter & Auth Logic.

---

*Developed with ❤️ as a modular, scalable School Management solution.*
