package taskmanager;

import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.managers.FileBackedTasksManager;
import service.exception.InvalidValueException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    private final String file = "file.txt";
    private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
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
        taskManager = new FileBackedTasksManager();
        super.beforeEach();
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
}