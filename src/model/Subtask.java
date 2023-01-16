package model;

public class Subtask extends Task{
    // исправил модификатор доступа
    private Status subtaskStatus;

    public Subtask(String mainTask, String mainSpecification) {
        super(mainTask, mainSpecification);
        subtaskStatus = Status.NEW;
    }

    public Status getSubtaskStatus() {
        return subtaskStatus;
    }

    public void setSubtaskStatus(Status subtaskStatus) {
        this.subtaskStatus = subtaskStatus;
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
                ", SubTaskId=" + getTaskId() +
                '}';
    }
}
