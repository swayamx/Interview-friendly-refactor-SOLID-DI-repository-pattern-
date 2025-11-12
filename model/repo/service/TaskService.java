package service;

import model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Business logic interface.
 * Keeping interface helps with testing and swapping implementations (mocking).
 */
public interface TaskService {
    Task create(String title, String description, long dueEpochMillis);
    Optional<Task> getById(String id);
    List<Task> listAll();
    List<Task> listOverdue();
    boolean complete(String id);
    boolean reopen(String id);
    boolean delete(String id);
}
