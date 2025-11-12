** DemoMain.java (manual DI + demo) *

import model.Task;
import repo.FileTaskRepository;
import repo.InMemoryTaskRepository;
import repo.TaskRepository;
import scheduler.TaskScheduler;
import service.TaskService;
import service.TaskServiceImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Demo main showing how to wire components manually (simple DI).
 * Interview talking point: manual DI is fine for small apps; use a DI container for larger apps.
 */
public class DemoMain {
    public static void main(String[] args) throws InterruptedException {
        // Swap repository implementation here:
        // TaskRepository repo = new InMemoryTaskRepository();
        TaskRepository repo = new FileTaskRepository("tasks.db"); // persists to file

        TaskService service = new TaskServiceImpl(repo);

        // Scheduler shows separation of concerns
        TaskScheduler scheduler = new TaskScheduler(service);
        scheduler.start(2, 10); // check every 10 seconds

        // create some tasks
        Task t1 = service.create("Write interview notes", "Prepare answers", Instant.now().plus(1, ChronoUnit.MINUTES).toEpochMilli());
        Task t2 = service.create("Refactor project", "Make repo+service", 0);

        System.out.println("Created:");
        List<Task> all = service.listAll();
        all.forEach(System.out::println);

        // mark done
        service.complete(t1.getId());
        System.out.println("After completing t1:");
        service.listAll().forEach(System.out::println);

        // let scheduler run once or twice
        Thread.sleep(12_000);

        // cleanup for demo
        scheduler.stop();
        repo.clear();
        System.out.println("Demo finished.");
    }
}
