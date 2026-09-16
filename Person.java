package edu.ccrm.domain;

import java.time.LocalDateTime;

public abstract class Person {
    protected String id;
    protected Name fullName;
    protected String email;
    protected LocalDateTime createdAt;
    
    public Person(String id, Name fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract String getRole();
    public abstract void displayProfile();
    
    // Getters and setters (Encapsulation)
    public String getId() { return id; }
    public Name getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Email: %s", id, fullName, email);
    }
}