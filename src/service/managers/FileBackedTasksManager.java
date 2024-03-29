package service.managers;

import model.*;
import service.exception.InvalidValueException;
import service.exception.ManagerSaveException;
import service.exception.TimeIntersectionException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final String FILE = "file.csv";

    public static FileBackedTasksManager loadFromFile(String file){
        try {
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
            fileBackedTasksManager.fromString(file);
            fileBackedTasksManager.historyFromString(file);
            return fileBackedTasksManager;
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка", e);
        }
    }

    public void save(){
        try {
            FileWriter fileWriter = new FileWriter(FILE, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (Integer key : getTaskTable().keySet()) {
                bufferedWriter.write(toString(getTaskTable().get(key)) + "\n");
            }
            for (Integer key : getEpicTable().keySet()) {
                bufferedWriter.write(toString(getEpicTable().get(key)) + "\n");
            }
            for (Integer key : getSubtaskTable().keySet()) {
                bufferedWriter.write(toString(getSubtaskTable().get(key)) + "\n");
            }
            bufferedWriter.write("\n");
            for (int i = 0; i < getListOfTasksIdForHistory().size(); i++) {
                bufferedWriter.write(String.valueOf(getListOfTasksIdForHistory().get(i)));
                if (i < getListOfTasksIdForHistory().size() - 1){
                    bufferedWriter.append(",");
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в сохранении данных", e);
        }
    }

    public String toString(Task task) {
        return task.toString();
    }

    public void fromString(String file){
        String content = readFileContentsOrNull(file);
        String[] lines = content.split("\r?\n");
        for (String line : lines) {
            if (!line.equals("")) {
                String[] parts = line.split(",");
                if (parts[1].equals("TASK") && parts[5].equals("indefinitely")) {
                    Task task = new Task(parts[2], parts[4]);
                    task.setTaskId(Integer.parseInt(parts[0]));
                    task.setTaskStatus(Status.valueOf(parts[3]));
                    getTaskTable().put(Integer.parseInt(parts[0]), task);
                    getPrioritizedTasks().add(task);
                } else if (parts[1].equals("TASK") && !parts[5].equals("indefinitely")) {
                    Task task = new Task(parts[2], parts[4], LocalDateTime.parse(parts[5]), Duration.parse(parts[6]));
                    task.setTaskId(Integer.parseInt(parts[0]));
                    task.setTaskStatus(Status.valueOf(parts[3]));
                    task.setTaskEndTime(LocalDateTime.parse(parts[7]));
                    getTaskTable().put(Integer.parseInt(parts[0]), task);
                    getPrioritizedTasks().add(task);
                } else if (parts[1].equals("EPIC") && parts[5].equals("indefinitely")) {
                    Epic epic = new Epic(parts[2], parts[4]);
                    epic.setTaskId(Integer.parseInt(parts[0]));
                    epic.setEpicStatus(Status.valueOf(parts[3]));
                    getEpicTable().put(Integer.parseInt(parts[0]), epic);
                    getPrioritizedTasks().add(epic);
                } else if (parts[1].equals("EPIC") && !parts[5].equals("indefinitely")) {
                    Epic epic = new Epic(parts[2], parts[4]);
                    epic.setTaskId(Integer.parseInt(parts[0]));
                    epic.setEpicStatus(Status.valueOf(parts[3]));
                    epic.setTaskStartTime(LocalDateTime.parse(parts[5]));
                    epic.setTaskEndTime(LocalDateTime.parse(parts[7]));
                    getEpicTable().put(Integer.parseInt(parts[0]), epic);
                    getPrioritizedTasks().add(epic);
                } else if (parts[1].equals("SUBTASK") && parts[6].equals("indefinitely")) {
                    Subtask subtask = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]));
                    subtask.setTaskId(Integer.parseInt(parts[0]));
                    subtask.setSubtaskStatus(Status.valueOf(parts[3]));
                    getSubtaskTable().put(Integer.parseInt(parts[0]), subtask);
                    getPrioritizedTasks().add(subtask);
                } else if (parts[1].equals("SUBTASK") && !parts[6].equals("indefinitely")){
                    Subtask subtask = new Subtask(parts[2], parts[4],
                            Integer.parseInt(parts[5]), LocalDateTime.parse(parts[6]), Duration.parse(parts[7]));
                    subtask.setTaskId(Integer.parseInt(parts[0]));
                    subtask.setSubtaskStatus(Status.valueOf(parts[3]));
                    getSubtaskTable().put(Integer.parseInt(parts[0]), subtask);
                    getPrioritizedTasks().add(subtask);
                }
            } else {
                break;
            }
        }
    }

    public String readFileContentsOrNull(String file) {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return null;
        }
    }

    public void historyFromString(String file) throws InvalidValueException {
        boolean check = false;
        String line = "";
        String content = readFileContentsOrNull(file);
        String[] lines = content.split("\r?\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].equals("")) {
                line = lines[i + 1];
                check = true;
                break;
            }
        }
        if (check) {
            String[] parts = line.split(",");
            for (String part : parts) {
                int idHistory = Integer.parseInt(part);
                if (getTaskTable().containsKey(idHistory)) {
                    getHistoryManager().add(getTaskTable().get(idHistory));
                    getListOfTasksIdForHistory().add(idHistory);
                } else if (getEpicTable().containsKey(idHistory)) {
                    getHistoryManager().add(getEpicTable().get(idHistory));
                    getListOfTasksIdForHistory().add(idHistory);
                } else if (getSubtaskTable().containsKey(idHistory)) {
                    getHistoryManager().add(getSubtaskTable().get(idHistory));
                    getListOfTasksIdForHistory().add(idHistory);
                }
            }
        } else {
            throw new InvalidValueException("История удалена или ее элементы не вводились");
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public void saveTask(Task task) throws TimeIntersectionException {
        super.saveTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) throws InvalidValueException, TimeIntersectionException {
        super.updateTask(task);
        save();
    }

    @Override
    public void outputAllTasks() throws InvalidValueException {
        super.outputAllTasks();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void outputByIdTask(Integer id) throws InvalidValueException {
        super.outputByIdTask(id);
        save();
    }

    @Override
    public void clearByIdTask(Integer id) throws InvalidValueException {
        super.clearByIdTask(id);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws InvalidValueException, TimeIntersectionException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void outputAllEpics() throws InvalidValueException {
        super.outputAllEpics();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void outputByIdEpic(Integer id) throws InvalidValueException {
        super.outputByIdEpic(id);
        save();
    }

    @Override
    public Collection<Task> getListTasks() {
        return super.getListTasks();
    }

    @Override
    public Collection<Epic> getListEpics() {
        return super.getListEpics();
    }

    @Override
    public Collection<Subtask> getListSubtasks() {
        return super.getListSubtasks();
    }

    @Override
    public void clearByIdEpic(Integer id) throws InvalidValueException {
        super.clearByIdEpic(id);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask, int epicId) throws TimeIntersectionException {
        super.saveSubtask(subtask, epicId);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws InvalidValueException, TimeIntersectionException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void outputAllSubtasks() throws InvalidValueException {
        super.outputAllSubtasks();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void outputByIdSubtasks(Integer id) throws InvalidValueException {
        super.outputByIdSubtasks(id);
        save();
    }

    @Override
    public void clearByIdSubtasks(Integer id) throws InvalidValueException {
        super.clearByIdSubtasks(id);
        save();
    }

    @Override
    public void epicStatus(Epic epic, ArrayList<Integer> epicListId) {
        super.epicStatus(epic, epicListId);
    }

    @Override
    public List<Task> getHistory() throws InvalidValueException{
        return super.getHistory();
    }

    @Override
    public void removeHistoryById(int id) {
        super.removeHistoryById(id);
    }

    @Override
    public List<Subtask> getSubTasksByEpicId(int id) throws InvalidValueException {
        return super.getSubTasksByEpicId(id);
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskTable() {
        return super.getSubtaskTable();
    }

    @Override
    public HashMap<Integer, Epic> getEpicTable() {
        return super.getEpicTable();
    }

    @Override
    public HashMap<Integer, Task> getTaskTable() {
        return super.getTaskTable();
    }

    @Override
    public HashMap<Integer, ArrayList<Integer>> getMapIdSubtaskByEpic() {
        return super.getMapIdSubtaskByEpic();
    }

    @Override
    public ArrayList<Integer> getListOfTasksIdForHistory() {
        return super.getListOfTasksIdForHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }
}