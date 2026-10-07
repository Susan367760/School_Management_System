/**
 * Represents a university course.
 *
 * Each Course object stores its own course information, while the static
 * totalEnrolledStudents variable keeps a system-wide count across all
 * Course instances.
 */
public class Course {
    private final String courseCode;
    private final String courseName;
    private final int maximumCapacity;

    // Shared by every Course object.
    private static int totalEnrolledStudents = 0;

    public Course(String courseCode, String courseName, int maximumCapacity) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        if (courseName == null || courseName.isBlank()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        if (maximumCapacity <= 0) {
            throw new IllegalArgumentException("Maximum capacity must be greater than zero.");
        }

        this.courseCode = courseCode.trim().toUpperCase();
        this.courseName = courseName.trim();
        this.maximumCapacity = maximumCapacity;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    /** Returns the total number of successful enrolments across all courses. */
    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    /**
     * Increases the shared enrolment counter after a successful enrolment.
     * Package-private so only classes in this application can update it.
     */
    static void recordEnrolment() {
        totalEnrolledStudents++;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName + " (Capacity: " + maximumCapacity + ")";
    }
}
