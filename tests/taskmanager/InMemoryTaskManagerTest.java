package taskmanager;

import org.junit.jupiter.api.BeforeEach;
import service.managers.InMemoryTaskManager;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();
        super.beforeEach();
    }
}