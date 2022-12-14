package model;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> epicListId;
    private String epicStatus;
    public Epic(String mainTask, String mainSpecification) {
        super(mainTask, mainSpecification);
        epicListId = new ArrayList<>();
        epicStatus = "NEW";
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
        return "model.Epic{" +
                "epicTitle='" + getTaskTitle() + '\'' +
                ", epicSpecification='" + getTaskSpecification() + '\'' +
                ", epicStatus='" + epicStatus + '\'' +
                ", epicId=" + getTaskId() +
                '}';
    }

    public ArrayList<Integer> getEpicListId() {
        return epicListId;
    }

    public void setEpicListId(ArrayList<Integer> epicListId) {
        this.epicListId = epicListId;
    }

    public String getEpicStatus() {
        return epicStatus;
    }

    public void setEpicStatus(String epicStatus) {
        this.epicStatus = epicStatus;
    }
}