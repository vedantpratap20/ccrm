package edu.ccrm.domain;

import java.time.LocalDateTime;

public class Course {
    private String code;
    private String title;
    private int credits;
    private String instructor;
    private Semester semester;
    private String department;
    private boolean active;
    private LocalDateTime createdAt;
    
    // Private constructor for Builder pattern
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.active = builder.active;
        this.createdAt = LocalDateTime.now();
    }
    
    // Builder pattern implementation
    public static class Builder {
        private String code;
        private String title;
        private int credits;
        private String instructor = "TBA";
        private Semester semester = Semester.FALL;
        private String department = "General";
        private boolean active = true;
        
        public Builder(String code, String title, int credits) {
            this.code = code;
            this.title = title;
            this.credits = credits;
        }
        
        public Builder instructor(String instructor) {
            this.instructor = instructor;
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }
        
        public Builder department(String department) {
            this.department = department;
            return this;
        }
        
        public Builder active(boolean active) {
            this.active = active;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
    
    // Getters
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Setters
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("Course[%s] %s (%d credits) - %s, %s", 
            code, title, credits, instructor, semester);
    }
}