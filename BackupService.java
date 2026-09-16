package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class BackupService {
    private final Path backupRoot;
    
    public BackupService(String backupPath) {
        this.backupRoot = Paths.get(backupPath);
        try {
            Files.createDirectories(backupRoot);
        } catch (IOException e) {
            System.err.println("Failed to create backup directory: " + e.getMessage());
        }
    }
    
    public void createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path backupDir = backupRoot.resolve("backup_" + timestamp);
        
        Files.createDirectories(backupDir);
        System.out.println("Created backup directory: " + backupDir);
        
        // Simulate copying files
        Files.write(backupDir.resolve("students.csv"), "Sample student data".getBytes());
        Files.write(backupDir.resolve("courses.csv"), "Sample course data".getBytes());
        Files.write(backupDir.resolve("enrollments.csv"), "Sample enrollment data".getBytes());
        
        System.out.println("Backup completed successfully!");
    }
    
    // Recursive method to calculate directory size
    public long calculateDirectorySize(Path directory) throws IOException {
        if (!Files.exists(directory)) return 0;
        
        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try {
                        return Files.size(path);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
        }
    }
    
    // Recursive method to list files by depth
    public void listFilesByDepth(Path directory, int maxDepth) throws IOException {
        if (!Files.exists(directory)) return;
        
        try (Stream<Path> paths = Files.walk(directory, maxDepth)) {
            paths.forEach(path -> {
                int depth = directory.relativize(path).getNameCount() - 1;
                String indent = "  ".repeat(Math.max(0, depth));
                System.out.println(indent + path.getFileName());
            });
        }
    }
}