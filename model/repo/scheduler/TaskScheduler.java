package scheduler;

import service.TaskService;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A small example scheduled worker. Keeps logic in service layer.
 */
public class TaskScheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final TaskService service;

    public TaskScheduler(TaskService service) {
        this.service = service;
    }

    public void start(long initialDelaySeconds, long periodSeconds) {
        executor.scheduleAtFixedRate(() -> {
            try {
                var overdue = service.listOverdue();
                if (!overdue.isEmpty()) {
                    System.out.println("[" + Instant.now() + "] Overdue tasks:");
                    overdue.forEach(t -> System.out.println(" - " + t));
                }
            } catch (Exception e) {
                System.err.println("Scheduler error: " + e.getMessage());
            }
        }, initialDelaySeconds, periodSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
