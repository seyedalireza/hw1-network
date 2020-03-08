package ir.sarh.model;

import java.io.Serializable;
import java.util.Objects;

public class Course implements Serializable {

    private static final long serialVersionUID = 1827324698736604798L;

    private String name;
    private int capacity;
    private int numberOfEnrolled;
    private int startTime;
    private int endTime;

    public Course(String name, int capacity, int numberOfEnrolled, int startTime, int endTime) {
        this.name = name;
        this.capacity = capacity;
        this.numberOfEnrolled = numberOfEnrolled;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // for deserializer
    public Course() {
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getNumberOfEnrolled() {
        return numberOfEnrolled;
    }

    public void setNumberOfEnrolled(int numberOfEnrolled) {
        this.numberOfEnrolled = numberOfEnrolled;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public synchronized void enrolled() {
        this.numberOfEnrolled++;
    }

    public synchronized void leaved() {
        this.numberOfEnrolled--;
    }

    @Override
    public String toString() {
        return name + "\t" + capacity + "\t" + numberOfEnrolled + "\t" + startTime + "-" + endTime;
    }

    public boolean hasOverlap(Course course) {
        if (this.startTime == course.startTime) {
            return true;
        } else if (this.startTime > course.startTime) {
            return this.startTime < course.endTime;
        } else {
            return this.endTime > course.startTime;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
