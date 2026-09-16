package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person {
    private String department;
    private List<String> assignedCourses;
    
    public Instructor(String id, Name fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
        this.assignedCourses = new ArrayList<>();
    }
    
    @Override
    public String getRole() {
        return "Instructor";
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== Instructor Profile ===");
        System.out.println("ID: " + id);
        System.out.println("Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Department: " + department);
        System.out.println("Assigned Courses: " + assignedCourses.size());
    }
    
    public void assignCourse(String courseCode) {
        if (!assignedCourses.contains(courseCode)) {
            assignedCourses.add(courseCode);
        }
    }
    
    public void unassignCourse(String courseCode) {
        assignedCourses.remove(courseCode);
    }
    
    // Getters and setters
    public String getDepartment() { return department; }
    public List<String> getAssignedCourses() { return new ArrayList<>(assignedCourses); }
    
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String toString() {
        return String.format("Instructor[%s] %s (%s) - %s", id, fullName, email, department);
    }
}