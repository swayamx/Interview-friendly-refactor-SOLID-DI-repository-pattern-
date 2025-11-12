package service;

import model.Task;
import repo.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of business rules. Keeps controllers/UI thin.
 */
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task create(String title, String description, long dueEpochMillis) {
        Task t = new Task(title, description, dueEpochMillis);
        return repository.save(t);
    }

    @Override
    public Optional<Task> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Task> listAll() {
        return repository.findAll();
    }

    @Override
    public List<Task> listOverdue() {
        return repository.findOverdue();
    }

    @Override
    public boolean complete(String id) {
        Optional<Task> maybe = repository.findById(id);
        if (maybe.isEmpty()) return false;
        Task t = maybe.get();
        t.markDone();
        repository.save(t);
        return true;
    }

    @Override
    public boolean reopen(String id) {
        Optional<Task> maybe = repository.findById(id);
        if (maybe.isEmpty()) return false;
        Task t = maybe.get();
        t.markUndone();
        repository.save(t);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return repository.delete(id);
    }
}
