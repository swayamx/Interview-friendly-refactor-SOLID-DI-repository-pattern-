package repo;

import model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface — implementation can be in-memory, file, DB, etc.
 */
public interface TaskRepository {
    Task save(Task task);               // create or update
    Optional<Task> findById(String id);
    List<Task> findAll();
    List<Task> findOverdue();
    boolean delete(String id);
    void clear();
}
