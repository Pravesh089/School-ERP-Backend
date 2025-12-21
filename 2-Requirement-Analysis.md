# Phase 2: Requirement Analysis

## A. Functional Requirements

### 1. User Management
*   **Admin can:**
    *   Create teachers
    *   Create students
    *   Assign roles
*   **Users can:**
    *   Login/logout
    *   Change password

### 2. Academic Structure Management
*   **Admin can create:**
    *   Classes (e.g., Class 1 to 12)
    *   Sections (A, B, C)
    *   Subjects
*   **Admin can assign:**
    *   Teachers -> Subjects -> Classes

### 3. Student Management
*   Store:
    *   Personal details (Name, DOB, Gender, etc.)
    *   Class & Section mapping
    *   Roll number (Unique per class/section)
    *   Status (Active/Inactive)
*   **Constraint:** Admission number must be unique.

### 4. Attendance Management
*   **Teacher can:**
    *   Mark daily attendance (Present/Absent)
    *   Edit attendance (within allowed window, e.g., same day)
*   **Student/Parent can:**
    *   View own attendance history

### 5. Examination & Marks
*   **Admin can:**
    *   Create exams (Unit Test, Mid-Term, Final)
*   **Teacher can:**
    *   Enter marks per subject
    *   Edit marks before result publication
*   **Student/Parent can:**
    *   View marks

### 6. Reporting
*   **Admin Reports:**
    *   Class-wise attendance trends
    *   Subject-wise performance
*   **Teacher Reports:**
    *   Student progress tracking
*   **Student Dashboard:**
    *   Marks summary

### 7. Fee Management
*   **Admin can:**
    *   Define Fee Heads (e.g. Tuition, Bus).
    *   Set Fee Structure per Class/Academic Year.
    *   Record student payments.
*   **Student can:**
    *   View pending dues and payment history.

### 8. Timetable Management
*   **Admin can:**
    *   Schedule subjects for class/sections.
    *   Assign teachers to time slots.
*   **Constraint:** No overlapping slots for classes or teachers.
*   **Users can:**
    *   View weekly timetable (Class-wise or Teacher-wise).

---

## B. Non-Functional Requirements

| Category | Requirement |
| :--- | :--- |
| **Security** | Role-based access control (RBAC). Passwords hashed. |
| **Performance** | Attendance entry should take < 2 sec. |
| **Availability** | 99% uptime aimed. |
| **Scalability** | Handle 1k–5k students. |
| **Data Integrity** | No duplicate roll numbers. Referential integrity. |
| **Backup** | Daily Database backup strategy. |
| **Audit** | Track who created/updated critical records. |
