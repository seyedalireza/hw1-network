package ir.sarh.server.thread;

import ir.sarh.model.Course;
import ir.sarh.model.DataBase;
import ir.sarh.model.Message;
import ir.sarh.model.Student;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserControllerThread extends Thread {

    private Student student;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public UserControllerThread(Socket clientSocket) throws IOException {
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message req = (Message) inputStream.readObject();
                outputStream.reset();
                switch (req.getType()) {
                    case LOGIN:
                        handleLoginRequest(req);
                        break;
                    case LOGOUT:
                        if (student == null) {
                            outputStream.writeObject(Message.errorMessageFrom("Login first"));
                            break;
                        }
                        outputStream.writeObject(Message.errorMessageFrom("Logged out successfully."));
                        this.student = null;
                        break;
                    case COURSE_LIST:
                        if (student == null) {
                            outputStream.writeObject(Message.errorMessageFrom("Login first"));
                            break;
                        }
                        outputStream.writeObject(Message.courseListMessageFrom(req.getUsername(),
                                DataBase.getCourses()));
                        break;
                    case MY_COURSE:
                        if (student == null) {
                            outputStream.writeObject(Message.errorMessageFrom("Login first"));
                            break;
                        }
                        outputStream.writeObject(Message.myCourseMessageFrom(req.getUsername(),
                                student.getCourses()));
                        break;
                    case TAKE:
                        if (student == null) {
                            outputStream.writeObject(Message.errorMessageFrom("Login first"));
                            break;
                        }
                        handleTackCourse(req);
                        break;
                    case DROP:
                        if (student == null) {
                            outputStream.writeObject(Message.errorMessageFrom("Login first"));
                            break;
                        }
                        handleRemoveCourse(req);
                        break;
                    case ERROR_OR_INFORMATION:
                        System.out.println("Client sends error message:)))), message:" + req.getUsername());
                        break;
                }
            } catch (IOException|ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void handleRemoveCourse(Message req) throws IOException {
        Course course = DataBase.getCourse(req.getCourseName());
        if (course == null) {
            outputStream.writeObject(Message.errorMessageFrom("Course is invalid."));
        } else {
            synchronized (course) {
                if (student.removeCourse(course)) {
                    DataBase.save();
                    course.leaved();
                    outputStream.writeObject(Message
                            .errorMessageFrom("Course deleted"));
                } else {
                    outputStream.writeObject(Message
                            .errorMessageFrom("Student can't remove this course."));
                }
            }

        }
    }

    private void handleTackCourse(Message req) throws IOException {
        Course course = DataBase.getCourse(req.getCourseName());
        if (course == null) {
            outputStream.writeObject(Message.errorMessageFrom("Course is invalid."));
        } else {
            synchronized (course) {
                if (course.getCapacity() <= course.getNumberOfEnrolled()) {
                    outputStream.writeObject(Message.errorMessageFrom("Course has not capacity"));
                } else {
                    boolean added = student.addCourse(course);
                    if (added) {
                        course.enrolled();
                        DataBase.save();
                        outputStream.writeObject(Message
                                .errorMessageFrom("Course added successfully."));
                    } else {
                        outputStream.writeObject(Message
                                .errorMessageFrom("Course has overlap with other courses"));
                    }
                }
            }
        }
    }

    private void handleLoginRequest(Message message) throws IOException {
        if (this.student != null) {
            outputStream.writeObject(Message.errorMessageFrom("Invalid login request."));
            return;
        }
        String username = message.getUsername();
        String password = message.getPassword();
        Student student = DataBase.getStudent(username);
        if (student != null) {
            this.student = student;
        }
        else {
            this.student = new Student(username, password);
            DataBase.addStudent(this.student);
        }
        outputStream.writeObject(Message.errorMessageFrom("You are successfully logged in."));
    }
}
