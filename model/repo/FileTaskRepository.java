package repo;

import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple file-backed repository using Java serialization.
 * Good for demos; replace with JSON/DB in production.
 *
 * Note: this persists the whole list on every change. For interview/demo purposes only.
 */
public class FileTaskRepository implements TaskRepository {
    private final File storage;

    public FileTaskRepository(String path) {
        this.storage = new File(path);
        if (!storage.exists()) {
            try {
                storage.createNewFile();
                persist(new ArrayList<>());
            } catch (IOException e) {
                throw new RuntimeException("Unable to create storage file", e);
            }
        }
    }

    @Override
    public synchronized Task save(Task task) {
        List<Task> list = readAll();
        boolean replaced = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(task.getId())) {
                list.set(i, task);
                replaced = true;
                break;
            }
        }
        if (!replaced) list.add(task);
        persist(list);
        return task;
    }

    @Override
    public synchronized Optional<Task> findById(String id) {
        return readAll().stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public synchronized List<Task> findAll() {
        return readAll();
    }

    @Override
    public synchronized List<Task> findOverdue() {
        List<Task> res = new ArrayList<>();
        for (Task t : readAll()) if (t.isOverdue()) res.add(t);
        return res;
    }

    @Override
    public synchronized boolean delete(String id) {
        List<Task> list = readAll();
        boolean removed = list.removeIf(t -> t.getId().equals(id));
        if (removed) persist(list);
        return removed;
    }

    @Override
    public synchronized void clear() {
        persist(new ArrayList<>());
    }

    // --- helpers ---
    @SuppressWarnings("unchecked")
    private List<Task> readAll() {
        if (storage.length() == 0) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storage))) {
            Object obj = ois.readObject();
            if (obj instanceof List) return (List<Task>) obj;
            return new ArrayList<>();
        } catch (Exception e) {
            // If deserialization fails, return empty to avoid crashing demo
            System.err.println("Warning: failed to read tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void persist(List<Task> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storage))) {
            oos.writeObject(list);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist tasks", e);
        }
    }
}
