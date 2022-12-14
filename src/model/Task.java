package model;

public class Task {
    private String taskTitle;
    private String taskSpecification;
    private String taskStatus;
    private int taskId;

    public Task(String taskTitle, String taskSpecification) {
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
        taskStatus = "NEW";
        taskId = taskId;
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "taskTitle='" + taskTitle + '\'' +
                ", taskSpecification='" + taskSpecification + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}

