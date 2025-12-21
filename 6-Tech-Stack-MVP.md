# Phase 6: Tech Stack & Next Steps (MVP Plan)

## 1. Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Spring Boot (Java) |
| **ORM / Data Access** | Hibernate / Spring Data JPA |
| **Database** | MySQL (8.x) |
| **Security** | Spring Security (JWT or Session) |
| **Frontend** | Thymeleaf (Server-side) or React (SPA) - *Started with Web* |
| **Build Tool** | Maven or Gradle |

## 2. MVP Scope (Locked)

1.  **Authentication**: Login, Role management.
2.  **Academic Structure Setup**: Creating Classes 1-12, Sections, Subjects.
3.  **Student Info**: Admission, Profile.
5.  **Marks & Exams**: Basic entry and display.
6.  **Fees**: Definition of heads/structure and simple collection.
7.  **Timetable**: Basic scheduling with overlap checks.
8.  **Reporting**: Simple dashboards.

## 3. Execution Plan

### Step 1: Project Setup (Day 1)
*   Initialize Spring Boot project.
*   Setup MySQL database.
*   Configure Spring Security foundation.

### Step 2: Core Entities (Day 2-3)
*   Implement User, Class, Section, Student entities.
*   Create CRUD APIs for Admin.

### Step 3: Attendance Module (Day 4-5)
*   Implement Attendance entity.
*   Create "Mark Attendance" logic.
*   Ensure date validation rules.

### Step 4: Examination Module (Day 6-7)
*   Implement Exam and Marks entities.
*   Logic for marks entry validation.

### Step 5: UI & Integration (Day 8-10)
*   Build basic screens for Admin, Teacher, Student.
*   Connect to REST APIs.

### Step 6: Testing & Deploy (Day 11-12)
*   Unit testing (JUnit/Mockito).
*   User Acceptance Testing (UAT).
*   Deploy to local or cloud server.
