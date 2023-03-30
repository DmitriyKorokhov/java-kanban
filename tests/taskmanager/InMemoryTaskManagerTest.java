package taskmanager;

import org.junit.jupiter.api.BeforeEach;
import service.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        super.beforeEach();
    }
}