import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a university student and the courses/grades belonging to that student.
 */
public class Student {
    private String name;
    private String studentId;

    // LinkedHashMap keeps courses in enrolment order and associates each course with its grade.
    // A null grade means that the student is enrolled but a grade has not yet been assigned.
    private final Map<Course, Double> enrolledCourses;

    public Student(String name, String studentId) {
        setName(name);
        setStudentId(studentId);
        this.enrolledCourses = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID cannot be empty.");
        }
        this.studentId = studentId.trim().toUpperCase();
    }

    /**
     * Enrols the student in a course.
     *
     * @return true when the enrolment succeeds, false when the student is already enrolled.
     * @throws IllegalStateException when the course has reached capacity.
     */
    public boolean enrollInCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }

        if (enrolledCourses.containsKey(course)) {
            return false;
        }

        if (CourseManagement.getEnrolledCount(course) >= course.getMaximumCapacity()) {
            throw new IllegalStateException("Course " + course.getCourseCode() + " has reached its maximum capacity.");
        }

        enrolledCourses.put(course, null);
        CourseManagement.recordSuccessfulEnrolment(course);
        return true;
    }

    /**
     * Assigns or updates a grade for a course in which the student is enrolled.
     */
    public void assignGrade(Course course, double grade) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
        if (!enrolledCourses.containsKey(course)) {
            throw new IllegalStateException("Student is not enrolled in " + course.getCourseCode() + ".");
        }

        enrolledCourses.put(course, grade);
    }

    /** Returns an unmodifiable view of the student's enrolled courses and grades. */
    public Map<Course, Double> getEnrolledCourses() {
        return Collections.unmodifiableMap(enrolledCourses);
    }

    /** Returns the list of courses for display purposes. */
    public List<Course> getCourses() {
        return Collections.unmodifiableList(new ArrayList<>(enrolledCourses.keySet()));
    }

    /** Returns the grade for a course, or null if no grade has been assigned. */
    public Double getGrade(Course course) {
        return enrolledCourses.get(course);
    }

    @Override
    public String toString() {
        return studentId + " - " + name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Student)) return false;
        Student student = (Student) other;
        return studentId.equalsIgnoreCase(student.studentId);
    }

    @Override
    public int hashCode() {
        return studentId.toUpperCase().hashCode();
    }
}
