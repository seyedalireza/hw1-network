package ir.sarh.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Student implements Serializable {

    private static final long serialVersionUID = -2669179132948213296L;

    private String username;
    private String password;
    private Set<Course> courses;

    public Student(String username, String password) {
        this.username = username;
        courses = new HashSet<>();
        this.password = password;
    }

    public Student() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Course> getCourses() {
        return Collections.unmodifiableSet(courses);
    }

    public boolean addCourse(Course course) {
        if (hasOverlapWithCourses(course)) {
            return false;
        }
        return courses.add(course);
    }

    private boolean hasOverlapWithCourses(Course course) {
        for (Course c : courses) {
            if (c.hasOverlap(course)) {
                return true;
            }
        }
        return false;
    }
}
