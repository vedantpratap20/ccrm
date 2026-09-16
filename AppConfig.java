package edu.ccrm.config;

import java.time.LocalDateTime;

// Singleton pattern implementation
public class AppConfig {
    private static AppConfig instance;
    private String dataFolderPath;
    private String backupFolderPath;
    private LocalDateTime startupTime;
    
    private AppConfig() {
        this.dataFolderPath = "./data";
        this.backupFolderPath = "./backups";
        this.startupTime = LocalDateTime.now();
        System.out.println("AppConfig initialized at: " + startupTime);
    }
    
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    
    public String getDataFolderPath() { return dataFolderPath; }
    public String getBackupFolderPath() { return backupFolderPath; }
    public LocalDateTime getStartupTime() { return startupTime; }
    
    public void setDataFolderPath(String dataFolderPath) {
        this.dataFolderPath = dataFolderPath;
    }
    
    public void setBackupFolderPath(String backupFolderPath) {
        this.backupFolderPath = backupFolderPath;
    }
    
    public void displayInfo() {
        System.out.println("=== Application Configuration ===");
        System.out.println("Data Folder: " + dataFolderPath);
        System.out.println("Backup Folder: " + backupFolderPath);
        System.out.println("Startup Time: " + startupTime);
    }
}