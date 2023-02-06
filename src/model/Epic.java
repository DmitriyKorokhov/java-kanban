package model;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> epicListId;

    private Status epicStatus;
    private TypesOfTasks epicType;

    public Epic(String taskTitle, String taskSpecification) {
        super(taskTitle, taskSpecification);
        epicListId = new ArrayList<>();
        epicStatus = Status.NEW;
        epicType = TypesOfTasks.EPIC;
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
        return  getTaskId() +
                "," + epicType +
                "," + getTaskTitle() +
                "," + epicStatus +
                "," + getTaskSpecification()
                ;
    }
}