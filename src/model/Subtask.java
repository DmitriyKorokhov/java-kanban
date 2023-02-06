package model;

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
        return  getTaskId() +
                "," + subtaskType +
                "," + getTaskTitle() +
                "," + subtaskStatus +
                "," + getTaskSpecification() +
                "," + getIdEpic()
                ;
    }
}
