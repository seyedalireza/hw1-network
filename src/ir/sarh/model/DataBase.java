package ir.sarh.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Database class for saving data on memory and persisting Course on disk.
 */
public class DataBase {

    private static HashMap<String, Course> courses;
    private static HashMap<String, Student> students;

    public synchronized static void load(String filePath) throws FileNotFoundException {
        courses = new HashMap<>();
        students = new HashMap<>();
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextLine()) {
            String courseStr = scanner.nextLine();
            Course course = parseCourse(courseStr);
            courses.put(course.getName(), course);
        }
        scanner.close();
    }

    public static Course getCourse(String name) {
        if (courses.containsKey(name)) {
            return courses.get(name);
        }
        return null;
    }

    public static Student getStudent(String username) {
        if (students.containsKey(username)) {
            return students.get(username);
        }
        return null;
    }

    public static Student addStudent(Student student) {
        return students.put(student.getUsername(), student);
    }

    public synchronized static void save(String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(filePath));
        for (Map.Entry<String, Course> entry : courses.entrySet()) {
            fileWriter.write(entry.getValue().toString());
        }
        fileWriter.flush();
        fileWriter.close();
    }

    private static Course parseCourse(String courseStr) {
        String[] split = courseStr.split(" ");
        String name = split[0];
        int cap = Integer.parseInt(split[1]);
        int enrolled = Integer.parseInt(split[2]);
        int startTime = Integer.parseInt(split[3].split("-")[0]);
        int endTime = Integer.parseInt(split[3].split("-")[1]);
        return new Course(name, cap, enrolled, startTime, endTime);
    }
}
