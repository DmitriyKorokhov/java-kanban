package model;

import java.time.*;
import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> epicListId;
    private Status epicStatus;
    private TypesOfTasks epicType;
    private LocalDateTime epicStartTime;
    private LocalDateTime epicEndTime;

    public Epic(String taskTitle, String taskSpecification) {
        super(taskTitle, taskSpecification);
        epicListId = new ArrayList<>();
        epicStatus = Status.NEW;
        epicType = TypesOfTasks.EPIC;
    }

    public Epic(int taskId, String taskTitle, String taskSpecification) {
        super(taskId, taskTitle, taskSpecification);
        epicType = TypesOfTasks.EPIC;
    }

    @Override
    public LocalDateTime getTaskStartTime() {
        return epicStartTime;
    }

    @Override
    public void setTaskStartTime(LocalDateTime epicStartTime) {
        this.epicStartTime = epicStartTime;
    }

    @Override
    public LocalDateTime getTaskEndTime() {
        return epicEndTime;
    }

    @Override
    public void setTaskEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    @Override
    public Duration getTaskDuration() {
        if (getTaskStartTime()!= null) {
            setTaskDuration(Duration.between(getTaskStartTime(), getTaskEndTime()));
        }
        return super.getTaskDuration();
    }

    @Override
    public void setTaskDuration(Duration taskDuration) {
        super.setTaskDuration(taskDuration);
    }


    public void setEpicType(TypesOfTasks epicType) {
        this.epicType = epicType;
    }

    public TypesOfTasks getEpicType() {
        return epicType;
    }

    public ArrayList<Integer> getEpicListId() {
        return epicListId;
    }

    public void setEpicListId(ArrayList<Integer> epicListId) {
        this.epicListId = epicListId;
    }

    public Status getEpicStatus() {
        return epicStatus;
    }

    public void setEpicStatus(Status epicStatus) {
        this.epicStatus = epicStatus;
    }

    @Override
    public String getTaskTitle() {
        return super.getTaskTitle();
    }

    @Override
    public void setTaskTitle(String taskTitle) {
        super.setTaskTitle(taskTitle);
    }

    @Override
    public String getTaskSpecification() {
        return super.getTaskSpecification();
    }

    @Override
    public void setTaskSpecification(String taskSpecification) {
        super.setTaskSpecification(taskSpecification);
    }

    @Override
    public String toString() {
        if (epicStartTime != null && getTaskStartTime() != LocalDateTime.MAX) {
            return getTaskId() +
                    "," + epicType +
                    "," + getTaskTitle() +
                    "," + epicStatus +
                    "," + getTaskSpecification() +
                    "," + getTaskStartTime() +
                    "," + getTaskDuration() +
                    "," + getTaskEndTime()
                    ;
        } else {
            return getTaskId() +
                    "," + epicType +
                    "," + getTaskTitle() +
                    "," + epicStatus +
                    "," + getTaskSpecification() +
                    "," + "indefinitely"
                    ;
        }
    }
}