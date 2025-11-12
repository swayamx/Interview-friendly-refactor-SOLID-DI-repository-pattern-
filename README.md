Interview talking points (concise)

Patterns used

Repository interface abstracts persistence; swap implementations (in-memory / file / DB) without changing service.

Service interface holds business rules; controllers/UI should be thin.

Scheduler delegates domain logic to the service — single responsibility principle.

Dependency injection

Manual DI in DemoMain — easy to replace with a framework (Guice / Spring) in bigger apps.

Benefits: easy unit testing (inject InMemoryTaskRepository or a mock).

Testability

Because TaskService depends on an interface, you can unit-test service by injecting a fake repository.

Concurrency & thread-safety

InMemoryTaskRepository uses ConcurrentHashMap — non-blocking reads; FileTaskRepository synchronizes to keep simple file operations safe.

Discuss trade-offs: file persistence approach is simple but not scalable; a DB or append-only log would be better for production.

Extensibility

DTOs, validation, and mapping layers can be added later.

To convert to a web service: add controllers, map Task to DTOs, and reuse TaskService.

Improvements

Replace serialization with JSON (Jackson) or an embedded DB (H2), add transactions, add optimistic locking/versioning for concurrent updates.

Add input validation, error handling strategy, and logging (SLF4J + Logback).
