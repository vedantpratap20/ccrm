package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.Comparator;

public class Comparators {
    
    // Lambda expressions for comparators
    public static final Comparator<Student> BY_GPA = 
        (s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA());
    
    public static final Comparator<Student> BY_NAME = 
        (s1, s2) -> s1.getFullName().toString().compareTo(s2.getFullName().toString());
    
    public static final Comparator<Course> BY_CREDITS = 
        (c1, c2) -> Integer.compare(c2.getCredits(), c1.getCredits());
    
    public static final Comparator<Course> BY_CODE = 
        Comparator.comparing(Course::getCode);
    
    // Anonymous inner class example
    public static final Comparator<Student> BY_REGISTRATION_DATE = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            return s1.getCreatedAt().compareTo(s2.getCreatedAt());
        }
    };
}