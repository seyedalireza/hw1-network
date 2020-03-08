package ir.sarh.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Database class for saving data on memory and persisting Course on disk.
 */
public class DataBase {

    public static final String DATABASE_FILE_NAME = "resources/Courses.txt";
    private static HashMap<String, Course> courses;
    private static HashMap<String, Student> students;

    static {
        try {
            load(DATABASE_FILE_NAME);
        } catch (FileNotFoundException e) {
            System.exit(-1);
        }
    }

    private synchronized static void load(String filePath) throws FileNotFoundException {
        courses = new HashMap<>();
        students = new HashMap<>();
        Scanner scanner = new Scanner(new File(DATABASE_FILE_NAME));
        while (scanner.hasNextLine()) {
            String courseStr = scanner.nextLine();
            Course course = parseCourse(courseStr);
            courses.put(course.getName(), course);
        }
        scanner.close();
    }

    public synchronized static Course getCourse(String name) {
        if (courses.containsKey(name)) {
            return courses.get(name);
        }
        return null;
    }

    public synchronized static Student getStudent(String username) {
        if (students.containsKey(username)) {
            return students.get(username);
        }
        return null;
    }

    public synchronized static void addStudent(Student student) {
        students.put(student.getUsername(), student);
    }

    public synchronized static void save() throws IOException {
        FileWriter fileWriter = new FileWriter(new File(DATABASE_FILE_NAME));
        for (Map.Entry<String, Course> entry : courses.entrySet()) {
            fileWriter.write(entry.getValue().toString() + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }

    private static Course parseCourse(String courseStr) {
        String[] split = courseStr.split("\t");
        String name = split[0];
        int cap = Integer.parseInt(split[1]);
        int enrolled = Integer.parseInt(split[2]);
        int startTime = Integer.parseInt(split[3].split("-")[0]);
        int endTime = Integer.parseInt(split[3].split("-")[1]);
        return new Course(name, cap, enrolled, startTime, endTime);
    }

    public static Set<Course> getCourses() {
        return new HashSet<>(courses.values());
    }
}
