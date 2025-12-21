# Phase 4: Low-Level Design (LLD) (Enriched)

## 1. Introduction
The Low-Level Design (LLD) defines the internal logic, class structures, and API signatures for the School ERP system. It serves as the blueprint for the implementation phase, ensuring modularity, security, and scalability.

## 2. Technology Stack Overview
- **Backend Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Security**: Spring Security with JWT (JSON Web Tokens)
- **Data Access**: Spring Data JPA with Hibernate
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Utilities**: Lombok for boilerplate reduction, Jakarta Validation for input sanitization.

## 3. Global Design Patterns
- **Controller-Service-Repository Pattern**: Ensures separation of concerns.
- **DTO (Data Transfer Objects)**: Decouples the API layer from the persistent entities.
- **Global Exception Handling**: Centralized error mapping for consistent API responses.

## 4. Security Philosophy
The system follows a stateless authentication model using JWT. Key security principles include:
- **BCrypt Hashing**: All passwords are encrypted before storage.
- **Method-Level Security**: `@PreAuthorize` annotations are used on controllers to enforce Role-Based Access Control (RBAC).
- **JWT Validation**: A custom filter intercepts every request to validate the token and set the security context.

---

## 5.0 Package Structure
The codebase is organized into functional packages:
- `com.school.config`: Configuration classes (JPA, DataSeeder).
- `com.school.controller`: REST Controllers defining the API endpoints.
- `com.school.dto`: Data Transfer Objects for request/response payloads.
- `com.school.model`: JPA Entities and Enums.
- `com.school.repository`: Spring Data JPA Repository interfaces.
- `com.school.service`: Business logic layer.
- `com.school.security`: Security filters, JWT utilities, and authentication logic.

---

## 5.1 Authentication & Security Module (Completed)

### Entities
*   **User**: `id`, `username`, `password_hash`, `role` (ADMIN, TEACHER, STUDENT), `status` (ACTIVE, INACTIVE), `created_at`.

### Connectors / DTOs
*   `LoginRequest`: { `username`, `password` }
*   `LoginResponse`: { `token`, `role`, `user_id`, `username` }
*   `ChangePasswordRequest`: { `oldPassword`, `newPassword` (min 6 chars) }

### APIs
*   `POST /api/auth/login` - Authenticates user and returns JWT.
*   `POST /api/auth/change-password` - Updates user password.
    *   **Validation**: Old password must match. New password min 6 chars.

### Security Implementation
*   **JWT Filter**: Intercepts requests, validates token, sets `SecurityContext`.
*   **Method Security**: Enabled.
    *   `ROLE_ADMIN`: Access to `/api/admin/**` (Strictly enforced via `@PreAuthorize`).
    *   `ROLE_TEACHER`: Access to marking attendance/exams.
    *   `ROLE_STUDENT`: Access to `/api/student/**`.

---

## 5.2 Academic Structure Module (Completed)

### Entities
*   **SchoolClass**: `id`, `class_number` (1–12).
*   **Section**: `id`, `name` (A, B, C...), `class_id`.
*   **Subject**: `id`, `name` (Maths, Science, etc.).

### APIs (Admin - ROLE_ADMIN)
*   `POST /api/admin/classes` - Create new Class (e.g., Class 10).
*   `POST /api/admin/classes/{classId}/sections` - Add section (e.g., Section A).
*   `POST /api/admin/subjects` - Create Subject.
*   `GET /api/admin/classes` - List all classes with sections.

---

## 5.3 Student Information Module (Completed)

### Entities
*   **Student**: `id`, `user_id` (FK), `admission_no` (Unique), `roll_no`, `first_name`, `last_name`, `class_id` (FK), `section_id` (FK), `dob`, `gender`, `parent_contact`.

### APIs (Admin - ROLE_ADMIN)
*   `POST /api/admin/students`
    *   **Body (StudentDto)**: { `firstName`, `lastName`, `admissionNo`, `rollNo`, `classId`, `sectionId`, `dob`, `gender` (M/F/O), `parentContact` }
    *   **Validation**:
        - `firstName`, `admissionNo`, `gender`: Not Blank.
        - `rollNo`, `classId`, `sectionId`: Not Null.
    *   **Action**: Creates Student + User (role=STUDENT). User username = `admissionNo`. Password default = `123456` (or provided).
*   `GET /api/admin/students` - List all students (impl. varies).

### APIs (Student Portal - ROLE_STUDENT)
*   `GET /api/student/profile` - Returns details of the logged-in student.
*   `GET /api/student/attendance` - Returns full attendance history.
*   `GET /api/student/marks` - Returns all obtained marks.

---

## 5.4 Teacher Information Module (Completed)

### Entities
*   **Teacher**: `id`, `user_id`, `name`, `qualification`, `email`.

### APIs (Admin - ROLE_ADMIN)
*   `POST /api/admin/teachers`
    *   **Body (TeacherDto)**: { `name`, `username`, `password`, `qualification`, `email` }
    *   **Validation**:
        - `name`, `username`, `password`: Not Blank.
        - `email`: Valid Email format.
    *   **Action**: Creates Teacher + User (role=TEACHER).

---

## 5.5 Attendance Module (Completed)

### Entities
*   **Attendance**: `id`, `student_id`, `class_id`, `calendar_date`, `status` (PRESENT, ABSENT, LEAVE), `marked_by`.

### APIs (Teacher - ROLE_TEACHER)
*   `POST /api/attendance/mark`
    *   **Body**: { `classId`, `sectionId`, `date`, `attendanceList`: [ { `studentId`, `status` }, ... ] }
    *   **Logic**:
        - Records attendance for the date.
        - Prevents future dates.
        - Tracks which teacher marked it.

---

## 5.6 Examination & Marks Module (Completed)

### Entities
*   **Exam**: `id`, `name` (e.g., Mid-Term), `start_date`, `end_date`, `published`.
*   **ExamSchedule**: `id`, `exam_id`, `class_id`, `subject_id`, `exam_date`, `max_marks`.
*   **Marks**: `id`, `exam_schedule_id`, `student_id`, `marks_obtained`, `remarks`.

### APIs (Teacher - ROLE_TEACHER)
*   `POST /api/exams/marks/entry`
    *   **Body**: { `examId`, `subjectId`, `classId`, `studentMarks`: [ { `studentId`, `marks` }, ... ] }
    *   **Validation**:
        - Check if Marks <= Max Marks.
        - Check if Student belongs to Class.

### APIs (Admin - ROLE_ADMIN)
*   `POST /api/exams` - Create Exam.
*   `POST /api/exams/schedule` - Create Exam Schedule (e.g. Math Exam for Class 10 on Date X).

---

## 5.7 Fees Module (Proposed)

### Entities
*   **FeeHead**: `id`, `name` (Tuition, Labs).
*   **FeeStructure**: `id`, `class_id`, `fee_head_id`, `amount`, `academic_year`, `due_date`.
*   **StudentFee**: `id`, `student_id`, `fee_structure_id`, `status` (PENDING/PAID), `paid_date`, `paid_amount`.

### APIs (Admin - ROLE_ADMIN)

#### 1. Fee Management
*   `POST /api/admin/fees/heads`
    *   **Body**: `{ name, description }`
    *   **Action**: Create a fee category (e.g. "Tuition Fee").
*   `POST /api/admin/fees/structure`
    *   **Body**: `{ classId, feeHeadId, amount, academicYear, dueDate }`
    *   **Action**: 
        1. Create FeeStructure record.
        2. **Auto-Assign**: Asynchronously create `StudentFee` records (status=PENDING) for ALL current students in that `classId`.
        *   *Note*: This ensures every student has a pending bill immediately.

#### 2. Fee Collection (Manual Entry by Admin/Accountant)
*   `GET /api/admin/fees/student/{studentId}`
    *   **Action**: List all fees (Pending & Paid) for a student.
*   `POST /api/admin/fees/pay`
    *   **Body**: `{ studentFeeId, paidAmount, paymentMode }`
    *   **Action**: Mark the specific fee record as PAID. Generate Transaction/Receipt Reference.

### APIs (Student - ROLE_STUDENT)

#### 1. Fee Portal
*   `GET /api/student/fees`
    *   **Action**: View my own fee status. Shows Due Date and Amount.

### Business Rules
*   **Partial Payments**: For MVP, allow full payment only or simple updates. Let's aim for **Full Payment** logic for simplicity (Status flips PENDING -> PAID).
*   **New Student**: When a new student is added to a Class, we should technically find all `FeeStructures` for that class and generate `StudentFee` records. (For MVP, we might skip this auto-trigger and rely on a "Refresh Fees" button or manual script).

---

## 5.8 Timetable Module (Proposed)

### Entities
*   **TimetableSlot**: `id`, `class_id`, `section_id`, `subject_id`, `teacher_id`, `day_of_week`, `start_time`, `end_time`.

### APIs (Admin - ROLE_ADMIN)
*   `POST /api/admin/timetable/slots`
    *   **Body**: `{ classId, sectionId, subjectId, teacherId, dayOfWeek, startTime, endTime }`
    *   **Action**: Create a schedule slot.
*   `DELETE /api/admin/timetable/slots/{id}` - Delete a slot.

### APIs (General - ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN)
*   `GET /api/timetable/class/{classId}/section/{sectionId}`
    *   **Action**: Returns weekly timetable for a specific class/section.
*   `GET /api/timetable/teacher/{teacherId}`
    *   **Action**: Returns weekly schedule for a specific teacher.

### Business Rules
*   **Overlap Check (Class)**: A class/section cannot have two slots that overlap in time on the same day.
*   **Overlap Check (Teacher)**: A teacher cannot be assigned to two different classes at overlapping times on the same day.

