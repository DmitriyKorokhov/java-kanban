package taskmanager;

import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.InvalidValueException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    private final String file = "file.txt";
    private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
    private final BufferedReader bufferedReader;

    {
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager("file.txt");
        super.beforeEach();
    }

    @Test
    public void saveOrRestoreAStateWithAnEmptyTaskList() throws IOException {
        fileBackedTasksManager.save();
        assertEquals("", bufferedReader.readLine(), "Неверное поведение при сохранении");
    }

    @Test
    public void saveAStateWithAnEpicWithoutSubtask() throws IOException {
        Epic epic = new Epic("Epic", "Test description");
        fileBackedTasksManager.saveEpic(epic);
        assertEquals("0,EPIC,Epic,NEW,Test description,indefinitely", bufferedReader.readLine(), "Данные не сохранены");
    }

    @Test
    public void saveOrRestoreAStateWithAnEmptyHistoryList() {
        Epic epic = new Epic("Epic", "Test description");
        fileBackedTasksManager.saveEpic(epic);
        InvalidValueException ex = assertThrows(InvalidValueException.class,
                () -> fileBackedTasksManager.historyFromString(file)
        );
        assertEquals("История удалена или ее элементы не вводились", ex.getMessage());
    }

    @Test
    public void restoreAStateHistoryAndWithAnEpicWithoutSubtask() throws InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        fileBackedTasksManager.saveEpic(epic);
        final int epicId = epic.getTaskId();
        fileBackedTasksManager.outputByIdEpic(epicId);
        fileBackedTasksManager.fromString(file);
        fileBackedTasksManager.historyFromString(file);
        assertEquals(epic.toString(), fileBackedTasksManager.getEpicTable().get(epicId).toString());
        assertEquals(epic.getTaskId(), fileBackedTasksManager.getListOfTasksIdForHistory().get(0));
    }


}