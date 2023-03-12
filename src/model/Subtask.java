package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task{
    private Status subtaskStatus;
    private TypesOfTasks subtaskType;
    private int idEpic;

    public Subtask(String mainTask, String mainSpecification, int idEpic) {
        super(mainTask, mainSpecification);
        subtaskStatus = Status.NEW;
        subtaskType = TypesOfTasks.SUBTASK;
        this.idEpic = idEpic;
    }

    public Subtask(String taskTitle, String taskSpecification, int idEpic, LocalDateTime taskStartTime, Duration taskDuration) {
        super(taskTitle, taskSpecification, taskStartTime, taskDuration);
        subtaskStatus = Status.NEW;
        subtaskType = TypesOfTasks.SUBTASK;
        this.idEpic = idEpic;
        setTaskEndTime(taskStartTime.plus(taskDuration));
    }

    public Subtask(int taskId, String taskTitle, String taskSpecification, LocalDateTime taskStartTime, Duration taskDuration) {
        super(taskId, taskTitle, taskSpecification, taskStartTime, taskDuration);
        subtaskType = TypesOfTasks.SUBTASK;
    }

    public Subtask(int taskId, String taskTitle, String taskSpecification) {
        super(taskId, taskTitle, taskSpecification);
        subtaskType = TypesOfTasks.SUBTASK;
    }

    @Override
    public LocalDateTime getTaskStartTime() {
        return super.getTaskStartTime();
    }

    @Override
    public Duration getTaskDuration() {
        return super.getTaskDuration();
    }

    @Override
    public void setTaskDuration(Duration taskDuration) {
        super.setTaskDuration(taskDuration);
    }

    @Override
    public void setTaskStartTime(LocalDateTime taskStartTime) {
        super.setTaskStartTime(taskStartTime);
    }

    @Override
    public LocalDateTime getTaskEndTime() {
        return super.getTaskEndTime();
    }

    @Override
    public void setTaskEndTime(LocalDateTime taskEndTime) {
        super.setTaskEndTime(taskEndTime);
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public void setSubtaskType(TypesOfTasks subtaskType) {
        this.subtaskType = subtaskType;
    }

    public TypesOfTasks getSubtaskType() {
        return subtaskType;
    }

    public Status getSubtaskStatus() {
        return subtaskStatus;
    }

    public void setSubtaskStatus(Status subtaskStatus) {
        this.subtaskStatus = subtaskStatus;
    }

    @Override
    public TypesOfTasks getTaskType() {
        return subtaskType;
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
        if (getTaskStartTime() == null) {
            return getTaskId() +
                    "," + subtaskType +
                    "," + getTaskTitle() +
                    "," + subtaskStatus +
                    "," + getTaskSpecification() +
                    "," + getIdEpic() +
                    "," + "indefinitely"
                    ;
        } else {
            return getTaskId() +
                    "," + subtaskType +
                    "," + getTaskTitle() +
                    "," + subtaskStatus +
                    "," + getTaskSpecification() +
                    "," + getIdEpic() +
                    "," + getTaskStartTime() +
                    "," + getTaskDuration() +
                    "," + getTaskEndTime()
                    ;
        }
    }
}
