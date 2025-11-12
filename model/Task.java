package model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity. Immutable-ish for safer concurrency: setters are limited.
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String title;
    private String description;
    private long dueEpochMillis; // 0 = none
    private boolean done;
    private final long createdAt;

    public Task(String title, String description, long dueEpochMillis) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueEpochMillis = dueEpochMillis;
        this.done = false;
        this.createdAt = Instant.now().toEpochMilli();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public long getDueEpochMillis() { return dueEpochMillis; }
    public boolean isDone() { return done; }
    public long getCreatedAt() { return createdAt; }

    // Domain operations (encapsulate state change)
    public void markDone() { this.done = true; }
    public void markUndone() { this.done = false; }
    public void updateTitle(String title) { this.title = title; }
    public void updateDescription(String description) { this.description = description; }
    public void updateDue(long dueEpochMillis) { this.dueEpochMillis = dueEpochMillis; }

    public boolean isOverdue() {
        if (dueEpochMillis <= 0) return false;
        return !done && Instant.now().toEpochMilli() > dueEpochMillis;
    }

    @Override
    public String toString() {
        return String.format("Task[id=%s,title=%s,done=%s,due=%d]", id, title, done, dueEpochMillis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task t = (Task) o;
        return id.equals(t.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
