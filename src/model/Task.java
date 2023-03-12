package model;

import java.time.*;


public class Task {
    private String taskTitle;
    private String taskSpecification;
    private int taskId;
    private Status taskStatus;
    private TypesOfTasks taskType;
    private LocalDateTime taskStartTime;
    private Duration taskDuration;
    private LocalDateTime taskEndTime;

    public Task(String taskTitle, String taskSpecification) {
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
        taskStatus = Status.NEW;
        taskType = TypesOfTasks.TASK;
    }

    public Task(String taskTitle, String taskSpecification, LocalDateTime taskStartTime, Duration taskDuration) {
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
        this.taskStartTime = taskStartTime;
        this.taskDuration = taskDuration;
        taskStatus = Status.NEW;
        taskType = TypesOfTasks.TASK;
        setTaskEndTime(taskStartTime.plus(taskDuration));
    }

    public Task(int taskId, String taskTitle, String taskSpecification) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
    }

    public Task(int taskId, String taskTitle, String taskSpecification, LocalDateTime taskStartTime, Duration taskDuration) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskSpecification = taskSpecification;
        this.taskStartTime = taskStartTime;
        this.taskDuration = taskDuration;
        taskType = TypesOfTasks.TASK;
        setTaskEndTime(taskStartTime.plus(taskDuration));
    }

    public LocalDateTime getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(LocalDateTime taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public LocalDateTime getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(LocalDateTime taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public Duration getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(Duration taskDuration) {
        this.taskDuration = taskDuration;
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
        if (taskStartTime == null) {
            return taskId +
                    "," + taskType +
                    "," + taskTitle +
                    "," + taskStatus +
                    "," + taskSpecification +
                    "," + "indefinitely"
                    ;
        } else {
            return taskId +
                    "," + taskType +
                    "," + taskTitle +
                    "," + taskStatus +
                    "," + taskSpecification +
                    "," + taskStartTime +
                    "," + taskDuration +
                    "," + taskEndTime
                    ;
        }
    }
}

