package model;

public class Task {
    private String taskTitle;
    private String taskSpecification;
    private int taskId;
    private Status taskStatus;
    private TypesOfTasks taskType;

    public Task(String taskTitle, String taskSpecification) {
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
        taskStatus = Status.NEW;
        taskType = TypesOfTasks.TASK;
    }

    public void setTaskType(TypesOfTasks taskType) {
        this.taskType = taskType;
    }

    public TypesOfTasks getTaskType() {
        return taskType;
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

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return  taskId +
                "," + taskType +
                "," + taskTitle +
                "," + taskStatus +
                "," + taskSpecification
                ;
    }
}

