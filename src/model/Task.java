package model;

import service.Status;

public class Task {
    private String taskTitle;
    private String taskSpecification;
    private int taskId;
    private Status taskStatus;

    public Task(String taskTitle, String taskSpecification) {
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
        taskStatus = Status.NEW;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskSpecification() {
        return taskSpecification;
    }

    public void setTaskSpecification(String taskSpecification) {
        this.taskSpecification = taskSpecification;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskTitle='" + taskTitle + '\'' +
                ", taskSpecification='" + taskSpecification + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }
}

