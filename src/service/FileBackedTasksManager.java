package service;

import model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager{
    private final String file;

    public FileBackedTasksManager(String file) {
        this.file = file;
    }

    public static void main(String[] args) throws InvalidValueException {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBackedTasksManager();

        String taskTitleOne = "Переезд";
        String taskSpecificationOne = "Я переезжаю в новую квартиру";
        LocalDateTime taskStartTimeOne = LocalDateTime.now();
        Duration taskDurationOne = Duration.ofDays(1);
        Task taskOne = new Task(taskTitleOne, taskSpecificationOne, taskStartTimeOne, taskDurationOne);

        String taskTitleTwo = "Переезд";
        String taskSpecificationTwo = "Продумал инструкции и начинаю подготовку к переезду";
        Task taskTwo = new Task(taskTitleTwo, taskSpecificationTwo);

        String epicTitleOne = "Сбор коробок";
        String epicSpecificationOne = "Мне нужно собрать много вещей и распределить по коробкам";
        Epic epicOne = new Epic(epicTitleOne, epicSpecificationOne);

        String epicTitleTwo = "Проверка и маркировка коробок в машину";
        String epicSpecificationTwo = "Сбор и погрузка всех вещей в машину";
        Epic epicTwo = new Epic(epicTitleTwo, epicSpecificationTwo);

        String subtaskTitleOne1 = "Распределение вещей";
        String subtaskSpecificationOne1 = "Вещи будут распределены по соответствующим коробкам в определенном порядке";
        LocalDateTime subtask1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration subtask1Duration = Duration.ofHours(2);
        Subtask subtaskOne1 = new Subtask(subtaskTitleOne1, subtaskSpecificationOne1, epicOne.getTaskId(), subtask1StartTime, subtask1Duration);

        String subtaskTitleOne2 = "Упаковка";
        String subtaskSpecificationOne2 = "Распределенные вещи нужно аккуратно упаковать";
        LocalDateTime subtask2StartTime = LocalDateTime.of(2023, Month.DECEMBER, 6, 12, 11);
        Duration subtask2Duration = Duration.ofHours(1);
        Subtask subtaskOne2 = new Subtask(subtaskTitleOne2, subtaskSpecificationOne2, epicOne.getTaskId(), subtask2StartTime, subtask2Duration);

        String subtaskTitleOne3 = "Упаковка";
        String subtaskSpecificationOne3 = "Распределенные вещи нужно аккуратно упаковать";
        Subtask subtaskOne3 = new Subtask(subtaskTitleOne3, subtaskSpecificationOne3, epicOne.getTaskId());


        System.out.println("Добавление задачи 1");
        fileBackedTasksManager.saveTask(taskOne);
        System.out.println("Добавление задачи 2");
        fileBackedTasksManager.saveTask(taskTwo);

        System.out.println("clear 1");
        fileBackedTasksManager.clearByIdTask(0);
        System.out.println("clear 2");
        fileBackedTasksManager.clearByIdTask(0);
        System.out.println("Вывод всех задач");
        fileBackedTasksManager.outputAllTasks();
        /*
        System.out.println("Добавление эпика 1");
        fileBackedTasksManager.saveEpic(epicOne);
        System.out.println("Добавление эпика 2");
        fileBackedTasksManager.saveEpic(epicTwo);
        System.out.println("Добавление подзадачи 1 для 1 эпика");
        fileBackedTasksManager.saveSubtask(subtaskOne1, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 2 для 1 эпика");
        fileBackedTasksManager.saveSubtask(subtaskOne2, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 3 для 1 эпика");
        fileBackedTasksManager.saveSubtask(subtaskOne3, epicOne, epicOne.getEpicListId());
        System.out.println("Вывод всех задач");
        fileBackedTasksManager.outputAllTasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех задач по сортировке");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        /*
        System.out.println("Удаляю задачу с id = 1");
        fileBackedTasksManager.clearByIdTask(1);
        System.out.println("Вывод всех задач");
        fileBackedTasksManager.outputAllTasks();
        System.out.println("Вывожу подзадачу с id = 6");
        fileBackedTasksManager.outputByIdSubtasks(6);
        System.out.println("Вывожу подзадачу с id = 6");
        fileBackedTasksManager.outputByIdSubtasks(6);
        System.out.println("Вывожу подзадачу с id = 5");
        fileBackedTasksManager.outputByIdSubtasks(5);
        System.out.println("Вывожу подзадачу с id = 4");
        fileBackedTasksManager.outputByIdSubtasks(4);
        System.out.println("Вывожу подзадачу с id = 6");
        fileBackedTasksManager.outputByIdSubtasks(6);
        System.out.println("Вывод истории");
        System.out.println(fileBackedTasksManager.getHistory());
  /*

        FileBackedTasksManager newFileBackedTasksManager = loadFromFile("file.csv");
        System.out.println("Вывод всех задач");
        newFileBackedTasksManager.outputAllTasks();
        System.out.println("Вывод всех эпиков");
        newFileBackedTasksManager.outputAllEpics();
        System.out.println("Вывод всех подзадач");
        newFileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод истории");
        System.out.println(newFileBackedTasksManager.getHistory());
*/
   }

    public static FileBackedTasksManager loadFromFile(String file){
        try {
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
            fileBackedTasksManager.fromString(file);
            fileBackedTasksManager.historyFromString(file);
            return fileBackedTasksManager;
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка", e);
        }
    }

    public void save(){
        try {
            FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
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
                } else if (parts[1].equals("TASK") && !parts[5].equals("indefinitely")) {
                    Task task = new Task(parts[2], parts[4], LocalDateTime.parse(parts[5]), Duration.parse(parts[6]));
                    task.setTaskId(Integer.parseInt(parts[0]));
                    task.setTaskStatus(Status.valueOf(parts[3]));
                    task.setTaskEndTime(LocalDateTime.parse(parts[7]));
                    getTaskTable().put(Integer.parseInt(parts[0]), task);
                } else if (parts[1].equals("EPIC") && parts[5].equals("indefinitely")) {
                    Epic epic = new Epic(parts[2], parts[4]);
                    epic.setTaskId(Integer.parseInt(parts[0]));
                    epic.setEpicStatus(Status.valueOf(parts[3]));
                    getEpicTable().put(Integer.parseInt(parts[0]), epic);
                } else if (parts[1].equals("EPIC") && !parts[5].equals("indefinitely")) {
                    Epic epic = new Epic(parts[2], parts[4]);
                    epic.setTaskId(Integer.parseInt(parts[0]));
                    epic.setEpicStatus(Status.valueOf(parts[3]));
                    epic.setTaskStartTime(LocalDateTime.parse(parts[5]));
                    epic.setTaskEndTime(LocalDateTime.parse(parts[7]));
                    getEpicTable().put(Integer.parseInt(parts[0]), epic);
                } else if (parts[1].equals("SUBTASK") && parts[6].equals("indefinitely")) {
                    Subtask subtask = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]));
                    subtask.setTaskId(Integer.parseInt(parts[0]));
                    subtask.setSubtaskStatus(Status.valueOf(parts[3]));
                    getSubtaskTable().put(Integer.parseInt(parts[0]), subtask);
                } else if (parts[1].equals("SUBTASK") && !parts[6].equals("indefinitely")){
                    Subtask subtask = new Subtask(parts[2], parts[4],
                            Integer.parseInt(parts[5]), LocalDateTime.parse(parts[6]), Duration.parse(parts[7]));
                    subtask.setTaskId(Integer.parseInt(parts[0]));
                    subtask.setSubtaskStatus(Status.valueOf(parts[3]));
                    getSubtaskTable().put(Integer.parseInt(parts[0]), subtask);
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
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) throws InvalidValueException {
        super.updateTask(task);
        save();
    }

    @Override
    public void outputAllTasks() {
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
    public void updateEpic(Epic epic) throws InvalidValueException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void outputAllEpics() {
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
    public void clearByIdEpic(Integer id) throws InvalidValueException {
        super.clearByIdEpic(id);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId) {
        super.saveSubtask(subtask, epic, epicListId);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws InvalidValueException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void outputAllSubtasks() {
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
    public List<Task> getHistory() throws InvalidValueException {
        return super.getHistory();
    }

    @Override
    public void removeHistoryById(int id) {
        super.removeHistoryById(id);
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