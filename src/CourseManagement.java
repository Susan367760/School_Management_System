import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Central management class for courses, students, enrolments and grades.
 *
 * The main collections are static because this application treats CourseManagement
 * as one shared management service for the entire program.
 */
public class CourseManagement {
    private static final List<Course> courses = new ArrayList<>();
    private static final Map<Student, Double> overallCourseGrades = new LinkedHashMap<>();
    private static final Map<String, Student> students = new LinkedHashMap<>();
    private static final Map<Course, Integer> courseEnrolmentCounts = new LinkedHashMap<>();

    private CourseManagement() {
        // Utility class; do not create CourseManagement objects.
    }

    /** Adds a new course to the shared course list. */
    public static Course addCourse(String courseCode, String courseName, int maximumCapacity) {
        if (findCourse(courseCode) != null) {
            throw new IllegalArgumentException("A course with code " + courseCode + " already exists.");
        }

        Course course = new Course(courseCode, courseName, maximumCapacity);
        courses.add(course);
        courseEnrolmentCounts.put(course, 0);
        return course;
    }

    /** Registers a student so the administrator can enrol and manage the student. */
    public static Student addStudent(String name, String studentId) {
        if (findStudent(studentId) != null) {
            throw new IllegalArgumentException("A student with ID " + studentId + " already exists.");
        }

        Student student = new Student(name, studentId);
        students.put(student.getStudentId(), student);
        return student;
    }

    /** Enrols a student in a course by calling the Student instance method. */
    public static void enrollStudent(Student student, Course course) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course are required.");
        }
        if (!courses.contains(course)) {
            throw new IllegalArgumentException("Course is not registered in the course catalogue.");
        }

        student.enrollInCourse(course);
    }

    /** Assigns a grade by calling the Student instance method. */
    public static void assignGrade(Student student, Course course, double grade) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course are required.");
        }
        student.assignGrade(course, grade);
        calculateOverallGrade(student);
    }

    /**
     * Calculates the average of all grades that have been assigned to the student.
     * Courses without grades are excluded from the calculation.
     */
    public static double calculateOverallGrade(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student is required.");
        }

        double total = 0;
        int gradedCourses = 0;

        for (Double grade : student.getEnrolledCourses().values()) {
            if (grade != null) {
                total += grade;
                gradedCourses++;
            }
        }

        if (gradedCourses == 0) {
            overallCourseGrades.put(student, 0.0);
            return 0.0;
        }

        double average = total / gradedCourses;
        overallCourseGrades.put(student, average);
        return average;
    }

    public static Double getOverallGrade(Student student) {
        return overallCourseGrades.get(student);
    }

    public static Student findStudent(String studentId) {
        if (studentId == null) return null;
        return students.get(studentId.trim().toUpperCase());
    }

    public static Course findCourse(String courseCode) {
        if (courseCode == null) return null;
        String code = courseCode.trim().toUpperCase();
        for (Course course : courses) {
            if (course.getCourseCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    public static List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public static List<Student> getStudents() {
        return Collections.unmodifiableList(new ArrayList<>(students.values()));
    }

    public static Map<Student, Double> getOverallCourseGrades() {
        return Collections.unmodifiableMap(overallCourseGrades);
    }

    public static int getEnrolledCount(Course course) {
        return courseEnrolmentCounts.getOrDefault(course, 0);
    }

    /** Updates the per-course count and the Course-wide static counter. */
    static void recordSuccessfulEnrolment(Course course) {
        courseEnrolmentCounts.put(course, getEnrolledCount(course) + 1);
        Course.recordEnrolment();
    }
}
