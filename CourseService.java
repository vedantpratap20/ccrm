package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CourseService implements Searchable<Course>, Persistable {
    private Map<String, Course> courses;
    
    public CourseService() {
        this.courses = new HashMap<>();
    }
    
    public void addCourse(Course course) {
        courses.put(course.getCode(), course);
    }
    
    public void updateCourse(String code, String instructor) {
        Course course = courses.get(code);
        if (course != null) {
            course.setInstructor(instructor);
        }
    }
    
    public void deactivateCourse(String code) {
        Course course = courses.get(code);
        if (course != null) {
            course.setActive(false);
        }
    }
    
    @Override
    public List<Course> search(Predicate<Course> criteria) {
        return courses.values().stream()
                .filter(criteria)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public Course findById(String id) {
        return courses.get(id);
    }
    
    public List<Course> findByDepartment(String department) {
        return search(c -> c.getDepartment().equalsIgnoreCase(department));
    }
    
    public List<Course> findByInstructor(String instructor) {
        return search(c -> c.getInstructor().equalsIgnoreCase(instructor));
    }
    
    public List<Course> findBySemester(Semester semester) {
        return search(c -> c.getSemester() == semester);
    }
    
    @Override
    public void save() throws Exception {
        System.out.println("Saving courses to file...");
    }
    
    @Override
    public void load() throws Exception {
        System.out.println("Loading courses from file...");
    }
}