package repo;

import model.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple thread-safe in-memory implementation useful for tests and demos.
 */
public class InMemoryTaskRepository implements TaskRepository {
    private final ConcurrentHashMap<String, Task> map = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        map.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Task> findAll() {
        return map.values().stream()
                .sorted(Comparator.comparingLong(Task::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findOverdue() {
        return map.values().stream().filter(Task::isOverdue).collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        return map.remove(id) != null;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
