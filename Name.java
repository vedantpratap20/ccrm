package edu.ccrm.domain;

public final class Name {
    private final String firstName;
    private final String lastName;
    
    public Name(String firstName, String lastName) {
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
    }
    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    
    @Override
    public String toString() {
        return getFullName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Name name = (Name) obj;
        return firstName.equals(name.firstName) && lastName.equals(name.lastName);
    }
    
    @Override
    public int hashCode() {
        return firstName.hashCode() + lastName.hashCode();
    }
}