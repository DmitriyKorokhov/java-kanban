package model;

import service.Status;

public class Subtask extends Task{
    private int epicId;
    public Status subtaskStatus;

    public Subtask(String mainTask, String mainSpecification, Integer epicId) {
        super(mainTask, mainSpecification);
        this.epicId = epicId;
        subtaskStatus = Status.NEW;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
        return "SubTask{" +
                "SubTaskTitle='" + getTaskTitle() + '\'' +
                ", SubTaskSpecification='" + getTaskSpecification()+ '\'' +
                ", SubTaskStatus='" + getSubtaskStatus() + '\'' +
                ", SubTaskId=" + epicId +
                '}';
    }

    public Status getSubtaskStatus() {
        return subtaskStatus;
    }

    public void setSubtaskStatus(Status subtaskStatus) {
        this.subtaskStatus = subtaskStatus;
    }
}
