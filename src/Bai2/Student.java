package Bai2;

import java.time.LocalDate;

public class Student {
    private int studentId;
    private static String fullName;
    private LocalDate dateOfBirth;
    private String email;

    public Student() {
    }

    public Student(int studentId, String fullName, LocalDate dateOfBirth, String email) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public static String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return this.studentId + "\t"
                + this.fullName + "\t"
                + this.dateOfBirth + "\t"
                + this.email;
    }
}