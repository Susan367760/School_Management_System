import java.util.Map;
import java.util.Scanner;

/**
 * Interactive command-line interface used by administrators.
 */
public class AdministratorInterface {
    private final Scanner scanner;

    public AdministratorInterface() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        printWelcome();

        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> registerStudent();
                    case 2 -> addCourse();
                    case 3 -> enrollStudent();
                    case 4 -> assignGrade();
                    case 5 -> calculateOverallGrade();
                    case 6 -> listStudents();
                    case 7 -> listCourses();
                    case 8 -> showTotalEnrolments();
                    case 0 -> {
                        running = false;
                        System.out.println("\nThank you for using the Course Enrolment and Grade Management System.");
                    }
                    default -> System.out.println("Invalid option. Please choose a number from 0 to 8.");
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Operation failed: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    private void printWelcome() {
        System.out.println("============================================================");
        System.out.println("       BRAINY MAGGIE COLLEGE OF ARTS");
        System.out.println("   Course Enrolment & Grade Management System");
        System.out.println("============================================================");
    }

    private void printMenu() {
        System.out.println("\n---------------- ADMINISTRATOR MENU ----------------");
        System.out.println("1. Register Student");
        System.out.println("2. Add New Course");
        System.out.println("3. Enrol Student in Course");
        System.out.println("4. Assign Grade");
        System.out.println("5. Calculate Overall Grade");
        System.out.println("6. List Students");
        System.out.println("7. List Courses");
        System.out.println("8. Show Total Enrolments");
        System.out.println("0. Exit");
        System.out.println("------------------------------------------------------");
    }

    private void registerStudent() {
        String name = readRequiredString("Enter student name: ");
        String id = readRequiredString("Enter student ID: ");
        Student student = CourseManagement.addStudent(name, id);
        System.out.println("Student registered successfully: " + student);
    }

    private void addCourse() {
        String code = readRequiredString("Enter course code: ");
        String name = readRequiredString("Enter course name: ");
        int capacity = readPositiveInt("Enter maximum capacity: ");

        Course course = CourseManagement.addCourse(code, name, capacity);
        System.out.println("Course added successfully: " + course);
    }

    private void enrollStudent() {
        String studentId = readRequiredString("Enter student ID: ");
        String courseCode = readRequiredString("Enter course code: ");

        Student student = CourseManagement.findStudent(studentId);
        Course course = CourseManagement.findCourse(courseCode);

        if (student == null) {
            throw new IllegalArgumentException("Student not found.");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course not found.");
        }

        boolean enrolled = false;
        try {
            enrolled = student.enrollInCourse(course);
        } catch (IllegalStateException exception) {
            throw exception;
        }

        // Student.enrollInCourse performs the actual operation. The method above is
        // called directly here so the interface clearly demonstrates instance behaviour.
        if (enrolled) {
            // The direct Student call above already records the successful enrolment.
            System.out.println("Student enrolled successfully.");
        } else {
            System.out.println("Student is already enrolled in this course.");
        }
    }

    private void assignGrade() {
        String studentId = readRequiredString("Enter student ID: ");
        String courseCode = readRequiredString("Enter course code: ");
        double grade = readDouble("Enter grade (0 - 100): ");

        Student student = CourseManagement.findStudent(studentId);
        Course course = CourseManagement.findCourse(courseCode);

        if (student == null) {
            throw new IllegalArgumentException("Student not found.");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course not found.");
        }

        CourseManagement.assignGrade(student, course, grade);
        System.out.printf("Grade %.2f assigned successfully.%n", grade);
    }

    private void calculateOverallGrade() {
        String studentId = readRequiredString("Enter student ID: ");
        Student student = CourseManagement.findStudent(studentId);

        if (student == null) {
            throw new IllegalArgumentException("Student not found.");
        }

        double average = CourseManagement.calculateOverallGrade(student);
        System.out.printf("Overall grade for %s (%s): %.2f%%%n", student.getName(), student.getStudentId(), average);
        displayStudentGrades(student);
    }

    private void listStudents() {
        System.out.println("\nRegistered Students:");
        if (CourseManagement.getStudents().isEmpty()) {
            System.out.println("No students registered.");
            return;
        }

        for (Student student : CourseManagement.getStudents()) {
            System.out.println("- " + student);
        }
    }

    private void listCourses() {
        System.out.println("\nCourse Catalogue:");
        if (CourseManagement.getCourses().isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        for (Course course : CourseManagement.getCourses()) {
            int enrolled = CourseManagement.getEnrolledCount(course);
            System.out.printf("- %s | Enrolled: %d/%d%n", course, enrolled, course.getMaximumCapacity());
        }
    }

    private void showTotalEnrolments() {
        System.out.println("Total enrolments across all courses: " + Course.getTotalEnrolledStudents());
    }

    private void displayStudentGrades(Student student) {
        System.out.println("Course grades:");
        if (student.getEnrolledCourses().isEmpty()) {
            System.out.println("- No courses enrolled.");
            return;
        }

        for (Map.Entry<Course, Double> entry : student.getEnrolledCourses().entrySet()) {
            String grade = entry.getValue() == null ? "Not assigned" : String.format("%.2f", entry.getValue());
            System.out.println("- " + entry.getKey().getCourseCode() + ": " + grade);
        }
    }

    private String readRequiredString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Please enter a number greater than zero.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= 0 && value <= 100) return value;
                System.out.println("Grade must be between 0 and 100.");
            } catch (NumberFormatException exception) {
                System.out.println("Invalid grade. Please enter a number such as 78.5.");
            }
        }
    }
}
