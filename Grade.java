package edu.ccrm.domain;

public enum Grade {
    S("S", 10.0),
    A("A", 9.0),
    B("B", 8.0),
    C("C", 7.0),
    D("D", 6.0),
    E("E", 5.0),
    F("F", 0.0);
    
    private final String letter;
    private final double gradePoint;
    
    Grade(String letter, double gradePoint) {
        this.letter = letter;
        this.gradePoint = gradePoint;
    }
    
    public String getLetter() { return letter; }
    public double getGradePoint() { return gradePoint; }
    
    @Override
    public String toString() {
        return letter + " (" + gradePoint + ")";
    }
}
