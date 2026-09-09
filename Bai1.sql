CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

DROP DATABASE student_management;
DROP TABLE Students;
DROP PROCEDURE get_all_students;
DROP PROCEDURE add_student;
DROP PROCEDURE update_student;
DROP PROCEDURE find_student_by_student_id;
DROP PROCEDURE delete_student;
-- =========================================
-- 1. CREATE TABLE
-- =========================================

CREATE TABLE Students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- =========================================
-- 2. GET ALL STUDENTS
-- =========================================

DELIMITER //

CREATE PROCEDURE get_all_students()
BEGIN
    SELECT *
    FROM Students;
END //

DELIMITER ;

-- =========================================
-- 3. ADD STUDENT
-- =========================================

DELIMITER //

CREATE PROCEDURE add_student(
    IN in_full_name VARCHAR(100),
    IN in_date_of_birth DATE,
    IN in_email VARCHAR(100)
)
BEGIN
    INSERT INTO Students(full_name, date_of_birth, email)
    VALUES (in_full_name, in_date_of_birth, in_email);
END //

DELIMITER ;

-- =========================================
-- 4. UPDATE STUDENT
-- =========================================

DELIMITER //

CREATE PROCEDURE update_student(
    IN in_student_id INT,
    IN in_full_name VARCHAR(100),
    IN in_date_of_birth DATE,
    IN in_email VARCHAR(100)
)
BEGIN
    UPDATE Students
    SET
        full_name = in_full_name,
        date_of_birth = in_date_of_birth,
        email = in_email
    WHERE student_id = in_student_id;
END //

DELIMITER ;

-- =========================================
-- 5. FIND STUDENT BY ID
-- =========================================

DELIMITER //

CREATE PROCEDURE find_student_by_student_id(
    IN in_student_id INT
)
BEGIN
    SELECT *
    FROM Students
    WHERE student_id = in_student_id;
END //

DELIMITER ;

-- =========================================
-- 6. DELETE STUDENT
-- =========================================

DELIMITER //

CREATE PROCEDURE delete_student(
    IN in_student_id INT
)
BEGIN
    DELETE FROM Students
    WHERE student_id = in_student_id;
END //

DELIMITER ;

CALL get_all_students();