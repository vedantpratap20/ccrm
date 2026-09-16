package edu.ccrm.service;

public interface Persistable {
    void save() throws Exception;
    void load() throws Exception;
    
    // Default method for diamond problem resolution
    default String getLastModified() {
        return "Unknown";
    }
}