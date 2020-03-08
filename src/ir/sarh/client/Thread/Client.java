package ir.sarh.client.Thread;

import ir.sarh.model.Course;
import ir.sarh.model.Message;
import ir.sarh.model.RequestType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    private String username;
    private Scanner scanner = new Scanner(System.in);
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Client(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to Server successfully.");
    }

    @Override
    public void run() {
        System.out.println("Client started successfully.");

        while (true) {
            if (username == null) {
                authentication();
            }
            try {
                outputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String command = scanner.nextLine();
            if (command.length() < 6) {
                System.out.println("Invalid command.");
                continue;
            }
            if (command.startsWith("Logout")) {
                logout();
            } else if (command.startsWith("ViewList")) {
                viewList();
            } else if (command.startsWith("MyCourse")) {
                myCourse();
            } else if (command.startsWith("TakeCourse")) {
                takeCourse(command);
            } else if (command.startsWith("DropCourse")) {
                deleteCourse(command);
            }
        }
    }

    private void deleteCourse(String command) {
        String courseName = command.substring(11, command.length() - 1);
        try {
            outputStream.writeObject(Message.dropCourseMessageFrom(username, courseName));
            Message message = (Message) inputStream.readObject();
            System.out.println(message.getErrorMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void takeCourse(String command) {
        String courseName = command.substring(11, command.length() - 1);
        try {
            outputStream.writeObject(Message.takeCourseMessageFrom(username, courseName));
            Message message = (Message) inputStream.readObject();
            System.out.println(message.getErrorMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }

    private void myCourse() {
        try {
            outputStream.writeObject(Message.myCourseMessageFrom(username, null));
            Message message = (Message) inputStream.readObject();
            if (message.getType() == RequestType.ERROR_OR_INFORMATION) {
                System.out.println(message.getErrorMessage());
            }
            for (Course course : message.getCourses()) {
                System.out.println(course);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void viewList() {
        try {
            outputStream.writeObject(Message.courseListMessageFrom(username, null));
            Message message = (Message) inputStream.readObject();
            if (message.getType() == RequestType.ERROR_OR_INFORMATION) {
                System.out.println(message.getErrorMessage());
            }
            for (Course course : message.getCourses()) {
                System.out.println(course);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        try {
            outputStream.writeObject(Message.logoutMessageFrom(username));
            Message message = (Message) inputStream.readObject();
            if (message.getType() == RequestType.ERROR_OR_INFORMATION) {
                System.out.println(message.getErrorMessage());
            }
            this.username = null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void authentication() {
        try {
            boolean successful = false;
            while (!successful) {
                System.out.println("First sign in.");
                System.out.println("Username:");
                String username = scanner.nextLine();
                System.out.println("Password:");
                String password = scanner.nextLine();
                outputStream.writeObject(Message.loginMessageFrom(username, password));
                Message message = (Message) inputStream.readObject();
                System.out.println(message.getErrorMessage());
                successful = message.getErrorMessage().equals("You are successfully logged in.");
                this.username = username;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
