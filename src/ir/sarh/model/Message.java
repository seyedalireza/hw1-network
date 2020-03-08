package ir.sarh.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class Message implements Serializable {

    private static final long serialVersionUID = -6176810702283314133L;

    private String username;
    private String password;
    private RequestType type;
    private String courseName;
    private Set<Course> courses;
    private String errorMessage;

    private Message(String username, String password, RequestType type, String courseName, Set<Course> courses) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.courseName = courseName;
        this.courses = courses;
    }

    private Message(String errorMessage) {
        this.errorMessage = errorMessage;
        this.type = RequestType.ERROR_OR_INFORMATION;
    }

    public static Message loginMessageFrom(String username, String password) {
        return new Message(username, password, RequestType.LOGIN, null, Collections.emptySet());
    }

    public static Message logoutMessageFrom(String username) {
        return new Message(username, null, RequestType.LOGOUT, null, Collections.emptySet());
    }

    public static Message courseListMessageFrom(String username, Set<Course> courses) {
        return new Message(username, null, RequestType.COURSE_LIST, null, courses);
    }

    public static Message myCourseMessageFrom(String username, Set<Course> courses) {
        return new Message(username, null, RequestType.MY_COURSE, null, courses);
    }

    public static Message dropCourseMessageFrom(String username, String courseName) {
        return new Message(username, null, RequestType.DROP, courseName, Collections.emptySet());
    }

    public static Message takeCourseMessageFrom(String username, String courseName) {
        return new Message(username, null, RequestType.TAKE, courseName, Collections.emptySet());
    }

    public static Message errorMessageFrom(String error) {
        return new Message(error);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public RequestType getType() {
        return type;
    }

    public String getCourseName() {
        return courseName;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Message message = (Message) o;
        return Objects.equals(username, message.username)
                && Objects.equals(password, message.password)
                && type == message.type
                && Objects.equals(courseName, message.courseName)
                && Objects.equals(courses, message.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, type, courseName, courses);
    }
}
