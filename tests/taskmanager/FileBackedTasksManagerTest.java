package taskmanager;

import org.junit.jupiter.api.BeforeEach;
import service.FileBackedTasksManager;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager("file.txt");
        super.beforeEach();
    }
}