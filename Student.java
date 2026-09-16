package edu.ccrm.domain;
import edu.ccrm.exception.MaxCreditLimitExceededException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private StudentStatus status;
    private List<String> enrolledCourses;
    private double totalCredits;
    private List<Grade> grades;
    
    // Static nested class for student statistics
    public static class Statistics {
        private int totalStudents;
        private double averageGPA;
        
        public Statistics(int totalStudents, double averageGPA) {
            this.totalStudents = totalStudents;
            this.averageGPA = averageGPA;
        }
        
        public int getTotalStudents() { return totalStudents; }
        public double getAverageGPA() { return averageGPA; }
    }
    
    public Student(String id, Name fullName, String email, String regNo) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.status = StudentStatus.ACTIVE;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.totalCredits = 0.0;
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== Student Profile ===");
        System.out.println("ID: " + id);
        System.out.println("Registration No: " + regNo);
        System.out.println("Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Status: " + status);
        System.out.println("Total Credits: " + totalCredits);
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        System.out.println("GPA: " + calculateGPA());
    }
    
    public void enrollInCourse(String courseCode, int credits) throws MaxCreditLimitExceededException {
        if (totalCredits + credits > 18) { // Max credits per semester
            throw new MaxCreditLimitExceededException("Cannot exceed 18 credits per semester");
        }
        
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
            totalCredits += credits;
        }
    }
    
    public void unenrollFromCourse(String courseCode, int credits) {
        if (enrolledCourses.remove(courseCode)) {
            totalCredits -= credits;
        }
    }
    
    public double calculateGPA() {
        if (grades.isEmpty()) return 0.0;
        
        return grades.stream()
                .mapToDouble(Grade::getGradePoint)
                .average()
                .orElse(0.0);
    }
    
    public void addGrade(Grade grade) {
        this.grades.add(grade);
    }
    
    // Getters and setters
    public String getRegNo() { return regNo; }
    public StudentStatus getStatus() { return status; }
    public List<String> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    public double getTotalCredits() { return totalCredits; }
    public List<Grade> getGrades() { return new ArrayList<>(grades); }
    
    public void setStatus(StudentStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("Student[%s] %s (%s) - %s", regNo, fullName, email, status);
    }
}
