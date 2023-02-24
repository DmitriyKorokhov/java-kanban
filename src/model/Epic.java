package model;

import java.time.*;
import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> epicListId;
    private Status epicStatus;
    private TypesOfTasks epicType;
    private LocalDateTime epicStartTime;
    private LocalDateTime epicEndTime;
    private LocalDateTime MAX;
    private LocalDateTime MIN;

    public Epic(String taskTitle, String taskSpecification) {
        super(taskTitle, taskSpecification);
        epicListId = new ArrayList<>();
        epicStatus = Status.NEW;
        epicType = TypesOfTasks.EPIC;
        MAX = LocalDateTime.MAX;
        MIN = LocalDateTime.MIN;
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

    public void setEpicStartTime(Subtask subtask) {
        MAX = getStartTime();
        if (subtask.getTaskStartTime() != null) {
            if (MAX.isAfter(subtask.getTaskStartTime())) {
                setStartTime(subtask.getTaskStartTime());
            }
        }
        epicStartTime = getStartTime();
    }

    public LocalDateTime getStartTime() {
        return MAX;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.MAX = startTime;
    }

    public void setEpicEndTime(Subtask subtask) {
        MIN = getEndTime();
        if (subtask.getTaskEndTime() != null) {
            if (MIN.isBefore(subtask.getTaskEndTime())) {
                setEndTime(subtask.getTaskEndTime());
            }
        }
        epicEndTime = getEndTime();
    }

    public LocalDateTime getEndTime() {
        return MIN;
    }

    public void setEndTime (LocalDateTime MIN) {
        this.MIN = MIN;
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