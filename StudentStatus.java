package edu.ccrm.domain;

public enum StudentStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    GRADUATED("Graduated"),
    SUSPENDED("Suspended");
    
    private final String displayName;
    
    StudentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}