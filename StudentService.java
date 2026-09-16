package edu.ccrm.service;
import edu.ccrm.exception.DuplicateEnrollmentException;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentService implements Searchable<Student>, Persistable {
    private Map<String, Student> students;
    
    public StudentService() {
        this.students = new HashMap<>();
    }
    
    public void addStudent(Student student) throws DuplicateEnrollmentException {
        if (students.containsKey(student.getId())) {
            throw new DuplicateEnrollmentException("Student with ID " + student.getId() + " already exists");
        }
        students.put(student.getId(), student);
    }
    
    public void updateStudent(String id, String email) {
        Student student = students.get(id);
        if (student != null) {
            student.setEmail(email);
        }
    }
    
    public void deactivateStudent(String id) {
        Student student = students.get(id);
        if (student != null) {
            student.setStatus(StudentStatus.INACTIVE);
        }
    }
    
    @Override
    public List<Student> search(Predicate<Student> criteria) {
        return students.values().stream()
                .filter(criteria)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }
    
    @Override
    public Student findById(String id) {
        return students.get(id);
    }
    
    public List<Student> findByStatus(StudentStatus status) {
        return search(s -> s.getStatus() == status);
    }
    
    public List<Student> getTopStudentsByGPA(int limit) {
        return students.values().stream()
                .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public void save() throws Exception {
        System.out.println("Saving students to file...");
        // Implementation would write to file
    }
    
    @Override
    public void load() throws Exception {
        System.out.println("Loading students from file...");
        // Implementation would read from file
    }
}