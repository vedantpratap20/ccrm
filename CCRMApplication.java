package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import edu.ccrm.exception.*;
import edu.ccrm.io.BackupService;
import edu.ccrm.util.Comparators;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CCRMApplication {
    private final StudentService studentService;
    private final CourseService courseService;
    private final BackupService backupService;
    private final Scanner scanner;
    private final AppConfig config;
    
    // Inner class for menu options
    private class MenuOption {
        private final String description;
        private final Runnable action;
        
        public MenuOption(String description, Runnable action) {
            this.description = description;
            this.action = action;
        }
        
        public String getDescription() { return description; }
        public void execute() { action.run(); }
    }
    
    public CCRMApplication() {
        this.config = AppConfig.getInstance();
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.backupService = new BackupService(config.getBackupFolderPath());
        this.scanner = new Scanner(System.in);
        
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        try {
            // Add sample students
            Student student1 = new Student("S001", new Name("John", "Doe"), "john@example.com", "REG001");
            Student student2 = new Student("S002", new Name("Jane", "Smith"), "jane@example.com", "REG002");
            
            studentService.addStudent(student1);
            studentService.addStudent(student2);
            
            // Add sample courses using Builder pattern
            Course course1 = new Course.Builder("CS101", "Introduction to Programming", 3)
                    .instructor("Dr. Wilson")
                    .semester(Semester.FALL)
                    .department("Computer Science")
                    .build();
                    
            Course course2 = new Course.Builder("MATH201", "Calculus II", 4)
                    .instructor("Prof. Johnson")
                    .semester(Semester.SPRING)
                    .department("Mathematics")
                    .build();
            
            courseService.addCourse(course1);
            courseService.addCourse(course2);
            
            // Enroll students and add grades
            student1.enrollInCourse("CS101", 3);
            student1.addGrade(Grade.A);
            student2.enrollInCourse("MATH201", 4);
            student2.addGrade(Grade.S);
            
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }
    
    public void start() {
        System.out.println("=== Campus Course & Records Manager ===");
        System.out.println("Welcome to CCRM - Java SE Application");
        config.displayInfo();
        
        displayPlatformInfo();
        
        Map<Integer, MenuOption> menuOptions = createMenuOptions();
        
        mainLoop: while (true) {
            displayMainMenu(menuOptions);
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 0:
                        System.out.println("Thank you for using CCRM!");
                        break mainLoop; // Labeled break
                    default:
                        MenuOption option = menuOptions.get(choice);
                        if (option != null) {
                            option.execute();
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private Map<Integer, MenuOption> createMenuOptions() {
        Map<Integer, MenuOption> options = new HashMap<>();
        
        options.put(1, new MenuOption("Manage Students", this::manageStudents));
        options.put(2, new MenuOption("Manage Courses", this::manageCourses));
        options.put(3, new MenuOption("Enrollment & Grading", this::manageEnrollment));
        options.put(4, new MenuOption("Reports", this::generateReports));
        options.put(5, new MenuOption("Backup & File Operations", this::manageBackup));
        options.put(6, new MenuOption("Search Operations", this::searchOperations));
        
        return options;
    }
    
    private void displayMainMenu(Map<Integer, MenuOption> options) {
        System.out.println("\n=== Main Menu ===");
        options.forEach((key, value) -> 
            System.out.println(key + ". " + value.getDescription()));
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void manageStudents() {
        System.out.println("\n=== Student Management ===");
        System.out.println("1. Add Student");
        System.out.println("2. List All Students");
        System.out.println("3. Update Student");
        System.out.println("4. Deactivate Student");
        System.out.println("5. Display Student Profile");
        System.out.print("Choose option: ");
        
        int option = scanner.nextInt();
        scanner.nextLine();
        
        switch (option) {
            case 1:
                addStudent();
                break;
            case 2:
                listAllStudents();
                break;
            case 3:
                updateStudent();
                break;
            case 4:
                deactivateStudent();
                break;
            case 5:
                displayStudentProfile();
                break;
            default:
                System.out.println("Invalid option");
        }
    }
    
    private void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Registration Number: ");
            String regNo = scanner.nextLine();
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            Student student = new Student(id, new Name(firstName, lastName), email, regNo);
            studentService.addStudent(student);
            
            System.out.println("Student added successfully!");
            
        } catch (DuplicateEnrollmentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void listAllStudents() {
        List<Student> students = studentService.findAll();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        // Using enhanced for loop
        System.out.println("\n=== All Students ===");
        for (Student student : students) {
            System.out.println(student);
        }
        
        // Demonstrate sorting with lambda
        System.out.println("\n=== Students Sorted by GPA ===");
        students.stream()
                .sorted(Comparators.BY_GPA)
                .forEach(System.out::println);
    }
    
    private void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine();
        
        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        
        studentService.updateStudent(id, email);
        System.out.println("Student updated successfully!");
    }
    
    private void deactivateStudent() {
        System.out.print("Enter Student ID to deactivate: ");
        String id = scanner.nextLine();
        
        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        studentService.deactivateStudent(id);
        System.out.println("Student deactivated successfully!");
    }
    
    private void displayStudentProfile() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        // Polymorphism in action - calling overridden method
        student.displayProfile();
        
        // Display transcript
        System.out.println("\n=== Transcript ===");
        List<Grade> grades = student.getGrades();
        if (grades.isEmpty()) {
            System.out.println("No grades recorded.");
        } else {
            int i = 0;
            // Traditional for loop with continue example
            for (i = 0; i < grades.size(); i++) {
                Grade grade = grades.get(i);
                if (grade == Grade.F) {
                    System.out.print("Failed Course, ");
                    continue; // Jump control example
                }
                System.out.println("Course " + (i+1) + ": " + grade);
            }
            System.out.println("Final GPA: " + student.calculateGPA());
        }
    }
    
    private void manageCourses() {
        System.out.println("\n=== Course Management ===");
        System.out.println("1. Add Course");
        System.out.println("2. List All Courses");
        System.out.println("3. Update Course");
        System.out.println("4. Deactivate Course");
        System.out.println("5. Search Courses");
        System.out.print("Choose option: ");
        
        int option = scanner.nextInt();
        scanner.nextLine();
        
        switch (option) {
            case 1 -> addCourse(); // Enhanced switch
            case 2 -> listAllCourses();
            case 3 -> updateCourse();
            case 4 -> deactivateCourse();
            case 5 -> searchCourses();
            default -> System.out.println("Invalid option");
        }
    }
    
    private void addCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine();
        
        System.out.print("Enter Course Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter Credits: ");
        int credits = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter Instructor: ");
        String instructor = scanner.nextLine();
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        
        System.out.println("Select Semester:");
        // Using enhanced for with enum
        int semIndex = 1;
        for (Semester sem : Semester.values()) {
            System.out.println(semIndex + ". " + sem);
            semIndex++;
        }
        
        int semChoice = scanner.nextInt();
        scanner.nextLine();
        
        Semester selectedSemester = Semester.values()[semChoice - 1];
        
        // Using Builder pattern
        Course course = new Course.Builder(code, title, credits)
                .instructor(instructor)
                .department(department)
                .semester(selectedSemester)
                .build();
        
        courseService.addCourse(course);
        System.out.println("Course added successfully!");
    }
    
    private void listAllCourses() {
        List<Course> courses = courseService.findAll();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.println("\n=== All Courses ===");
        // Using method reference
        courses.forEach(System.out::println);
        
        // Demonstrate Stream operations
        System.out.println("\n=== Courses by Department ===");
        Map<String, List<Course>> coursesByDept = courses.stream()
                .collect(Collectors.groupingBy(Course::getDepartment));
        
        coursesByDept.forEach((dept, courseList) -> {
            System.out.println(dept + ":");
            courseList.forEach(c -> System.out.println("  " + c.getCode() + " - " + c.getTitle()));
        });
    }
    
    private void updateCourse() {
        System.out.print("Enter Course Code to update: ");
        String code = scanner.nextLine();
        
        Course course = courseService.findById(code);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.print("Enter new instructor: ");
        String instructor = scanner.nextLine();
        
        courseService.updateCourse(code, instructor);
        System.out.println("Course updated successfully!");
    }
    
    private void deactivateCourse() {
        System.out.print("Enter Course Code to deactivate: ");
        String code = scanner.nextLine();
        
        Course course = courseService.findById(code);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        courseService.deactivateCourse(code);
        System.out.println("Course deactivated successfully!");
    }
    
    private void searchCourses() {
        System.out.println("1. Search by Department");
        System.out.println("2. Search by Instructor");
        System.out.println("3. Search by Semester");
        System.out.print("Choose search type: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        List<Course> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                System.out.print("Enter Department: ");
                String dept = scanner.nextLine();
                results = courseService.findByDepartment(dept);
                break;
            case 2:
                System.out.print("Enter Instructor: ");
                String instructor = scanner.nextLine();
                results = courseService.findByInstructor(instructor);
                break;
            case 3:
                System.out.println("Select Semester:");
                int semIndex = 1;
                for (Semester sem : Semester.values()) {
                    System.out.println(semIndex + ". " + sem);
                    semIndex++;
                }
                int semChoice = scanner.nextInt();
                Semester selectedSem = Semester.values()[semChoice - 1];
                results = courseService.findBySemester(selectedSem);
                break;
        }
        
        if (results.isEmpty()) {
            System.out.println("No courses found matching the criteria.");
        } else {
            System.out.println("Search Results:");
            results.forEach(System.out::println);
        }
    }
    
    private void manageEnrollment() {
        System.out.println("\n=== Enrollment & Grading ===");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Unenroll Student from Course");
        System.out.println("3. Record Grade");
        System.out.print("Choose option: ");
        
        int option = scanner.nextInt();
        scanner.nextLine();
        
        switch (option) {
            case 1:
                enrollStudent();
                break;
            case 2:
                unenrollStudent();
                break;
            case 3:
                recordGrade();
                break;
        }
    }
    
    private void enrollStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        
        Course course = courseService.findById(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        try {
            student.enrollInCourse(courseCode, course.getCredits());
            System.out.println("Student enrolled successfully!");
            
            // Assertion example - enable with -ea flag
            assert student.getTotalCredits() <= 18 : "Student credits exceeded limit";
            
        } catch (MaxCreditLimitExceededException e) {
            System.err.println("Enrollment failed: " + e.getMessage());
        }
    }
    
    private void unenrollStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        
        Course course = courseService.findById(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        student.unenrollFromCourse(courseCode, course.getCredits());
        System.out.println("Student unenrolled successfully!");
    }
    
    private void recordGrade() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Select Grade:");
        int gradeIndex = 1;
        for (Grade grade : Grade.values()) {
            System.out.println(gradeIndex + ". " + grade);
            gradeIndex++;
        }
        
        int gradeChoice = scanner.nextInt();
        Grade selectedGrade = Grade.values()[gradeChoice - 1];
        
        student.addGrade(selectedGrade);
        System.out.println("Grade recorded successfully!");
    }
    
    private void generateReports() {
        System.out.println("\n=== Reports ===");
        System.out.println("1. Top Students by GPA");
        System.out.println("2. GPA Distribution");
        System.out.println("3. Course Statistics");
        System.out.println("4. Student Statistics");
        System.out.print("Choose report: ");
        
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                showTopStudents();
                break;
            case 2:
                showGPADistribution();
                break;
            case 3:
                showCourseStatistics();
                break;
            case 4:
                showStudentStatistics();
                break;
        }
    }
    
    private void showTopStudents() {
        List<Student> topStudents = studentService.getTopStudentsByGPA(5);
        
        System.out.println("\n=== Top 5 Students by GPA ===");
        if (topStudents.isEmpty()) {
            System.out.println("No students with grades found.");
            return;
        }
        
        int rank = 1;
        for (Student student : topStudents) {
            System.out.printf("%d. %s - GPA: %.2f%n", 
                rank++, student.getFullName(), student.calculateGPA());
        }
    }
    
    private void showGPADistribution() {
        List<Student> allStudents = studentService.findAll();
        
        // Using Stream API for aggregation
        Map<String, Long> gpaRanges = allStudents.stream()
                .filter(s -> !s.getGrades().isEmpty())
                .collect(Collectors.groupingBy(
                    s -> {
                        double gpa = s.calculateGPA();
                        if (gpa >= 9.0) return "Excellent (9.0+)";
                        else if (gpa >= 8.0) return "Very Good (8.0-8.9)";
                        else if (gpa >= 7.0) return "Good (7.0-7.9)";
                        else if (gpa >= 6.0) return "Average (6.0-6.9)";
                        else return "Below Average (<6.0)";
                    },
                    Collectors.counting()
                ));
        
        System.out.println("\n=== GPA Distribution ===");
        gpaRanges.forEach((range, count) -> 
            System.out.println(range + ": " + count + " students"));
    }
    
    private void showCourseStatistics() {
        List<Course> courses = courseService.findAll();
        
        System.out.println("\n=== Course Statistics ===");
        System.out.println("Total Courses: " + courses.size());
        
        // Group by semester
        Map<Semester, Long> courseBySemester = courses.stream()
                .collect(Collectors.groupingBy(Course::getSemester, Collectors.counting()));
        
        System.out.println("\nCourses by Semester:");
        courseBySemester.forEach((sem, count) -> 
            System.out.println(sem + ": " + count));
        
        // Average credits
        double avgCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .average()
                .orElse(0.0);
        
        System.out.printf("Average Credits per Course: %.2f%n", avgCredits);
    }
    
    private void showStudentStatistics() {
        List<Student> students = studentService.findAll();
        
        // Using static nested class
        double avgGPA = students.stream()
                .filter(s -> !s.getGrades().isEmpty())
                .mapToDouble(Student::calculateGPA)
                .average()
                .orElse(0.0);
        
        Student.Statistics stats = new Student.Statistics(students.size(), avgGPA);
        
        System.out.println("\n=== Student Statistics ===");
        System.out.println("Total Students: " + stats.getTotalStudents());
        System.out.printf("Average GPA: %.2f%n", stats.getAverageGPA());
        
        // Status distribution
        Map<StudentStatus, Long> statusDistribution = students.stream()
                .collect(Collectors.groupingBy(Student::getStatus, Collectors.counting()));
        
        System.out.println("\nStudent Status Distribution:");
        statusDistribution.forEach((status, count) -> 
            System.out.println(status + ": " + count));
    }
    
    private void manageBackup() {
        System.out.println("\n=== Backup & File Operations ===");
        System.out.println("1. Create Backup");
        System.out.println("2. Show Backup Directory Size");
        System.out.println("3. List Backup Files");
        System.out.println("4. Export Data");
        System.out.print("Choose option: ");
        
        int choice = scanner.nextInt();
        
        try {
            switch (choice) {
                case 1:
                    backupService.createBackup();
                    break;
                case 2:
                    showBackupSize();
                    break;
                case 3:
                    listBackupFiles();
                    break;
                case 4:
                    exportData();
                    break;
            }
        } catch (IOException e) {
            System.err.println("File operation failed: " + e.getMessage());
        }
    }
    
    private void showBackupSize() throws IOException {
        long size = backupService.calculateDirectorySize(
            java.nio.file.Paths.get(config.getBackupFolderPath()));
        
        System.out.printf("Backup directory size: %d bytes (%.2f KB)%n", 
            size, size / 1024.0);
    }
    
    private void listBackupFiles() throws IOException {
        System.out.println("\n=== Backup Directory Contents ===");
        backupService.listFilesByDepth(
            java.nio.file.Paths.get(config.getBackupFolderPath()), 3);
    }
    
    private void exportData() {
        System.out.println("Exporting data...");
        
        // Simulate file operations
        try {
            studentService.save();
            courseService.save();
            System.out.println("Data exported successfully!");
        } catch (Exception e) {
            System.err.println("Export failed: " + e.getMessage());
        }
    }
    
    private void searchOperations() {
        System.out.println("\n=== Search Operations ===");
        System.out.println("1. Search Students by Status");
        System.out.println("2. Search Students by GPA Range");
        System.out.println("3. Search Courses by Credits");
        System.out.print("Choose search: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                searchStudentsByStatus();
                break;
            case 2:
                searchStudentsByGPA();
                break;
            case 3:
                searchCoursesByCredits();
                break;
        }
    }
    
    private void searchStudentsByStatus() {
        System.out.println("Select Status:");
        int statusIndex = 1;
        for (StudentStatus status : StudentStatus.values()) {
            System.out.println(statusIndex + ". " + status);
            statusIndex++;
        }
        
        int choice = scanner.nextInt();
        StudentStatus selectedStatus = StudentStatus.values()[choice - 1];
        
        List<Student> results = studentService.findByStatus(selectedStatus);
        
        System.out.println("\n=== Students with status: " + selectedStatus + " ===");
        if (results.isEmpty()) {
            System.out.println("No students found.");
        } else {
            results.forEach(System.out::println);
        }
    }
    
    private void searchStudentsByGPA() {
        System.out.print("Enter minimum GPA: ");
        double minGPA = scanner.nextDouble();
        
        // Using lambda for filtering
        Predicate<Student> gpaFilter = s -> !s.getGrades().isEmpty() && s.calculateGPA() >= minGPA;
        
        List<Student> results = studentService.search(gpaFilter);
        
        System.out.println("\n=== Students with GPA >= " + minGPA + " ===");
        if (results.isEmpty()) {
            System.out.println("No students found.");
        } else {
            results.stream()
                .sorted(Comparators.BY_GPA)
                .forEach(s -> System.out.printf("%s - GPA: %.2f%n", 
                    s.getFullName(), s.calculateGPA()));
        }
    }
    
    private void searchCoursesByCredits() {
        System.out.print("Enter minimum credits: ");
        int minCredits = scanner.nextInt();
        
        // Using lambda for filtering
        List<Course> results = courseService.search(c -> c.getCredits() >= minCredits);
        
        System.out.println("\n=== Courses with credits >= " + minCredits + " ===");
        if (results.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            results.stream()
                .sorted(Comparators.BY_CREDITS)
                .forEach(System.out::println);
        }
    }
    
    private void displayPlatformInfo() {
        System.out.println("\n=== Java Platform Information ===");
        System.out.println("Java SE (Standard Edition): Core Java platform for desktop and server applications");
        System.out.println("Java ME (Micro Edition): For mobile and embedded devices");
        System.out.println("Java EE (Enterprise Edition): For enterprise applications with web services");
        System.out.println("Current Application: CCRM runs on Java SE");
        System.out.println("JDK: Development kit with compiler, JRE: Runtime environment, JVM: Virtual machine");
    }
    
    // Demonstration of operator precedence and bitwise operations
    private void demonstrateOperators() {
        int a = 10, b = 3, c = 5;
        
        // Arithmetic and precedence: * has higher precedence than +
        int result1 = a + b * c; // 10 + (3 * 5) = 25
        
        // Relational and logical
        boolean result2 = (a > b) && (c < a); // true && true = true
        
        // Bitwise operations
        int result3 = a & b; // 10 & 3 = 2 (binary: 1010 & 0011 = 0010)
        int result4 = a | b; // 10 | 3 = 11 (binary: 1010 | 0011 = 1011)
        
        System.out.println("Operator demonstrations:");
        System.out.println("a + b * c = " + result1);
        System.out.println("(a > b) && (c < a) = " + result2);
        System.out.println("a & b = " + result3);
        System.out.println("a | b = " + result4);
    }
    
    // Method demonstrating upcast and downcast with instanceof
    private void demonstratePolymorphism() {
        // Upcast (automatic)
        Person person = new Student("S999", new Name("Test", "User"), "test@example.com", "REG999");
        
        System.out.println("Polymorphism demonstration:");
        person.displayProfile(); // Calls Student's overridden method
        
        // Downcast (explicit) with instanceof check
        if (person instanceof Student) {
            Student student = (Student) person; // Safe downcast
            System.out.println("Downcast successful: " + student.getRegNo());
        }
    }
    
    // Method overloading example
    public void displayInfo(String message) {
        System.out.println("Info: " + message);
    }
    
    public void displayInfo(String title, String message) {
        System.out.println(title + ": " + message);
    }
    
    public void displayInfo(int code, String message) {
        System.out.println("Code " + code + ": " + message);
    }
    
    public static void main(String[] args) {
        // Enable assertions with -ea flag
        System.out.println("Starting CCRM Application...");
        System.out.println("Note: Run with -ea flag to enable assertions");
        
        CCRMApplication app = new CCRMApplication();
        
        // Demonstrate method overloading
        app.displayInfo("Application initialized");
        app.displayInfo("Status", "Ready to start");
        app.displayInfo(200, "System OK");
        
        // Demonstrate operators and polymorphism
        app.demonstrateOperators();
        app.demonstratePolymorphism();
        
        // Start the main application
        app.start();
    }
}