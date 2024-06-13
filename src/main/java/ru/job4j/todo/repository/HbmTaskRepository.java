package ru.job4j.todo.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public Optional<Task> create(Task task) {
        Optional<Task> taskOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            int id = (int) session.save(task);
            task.setId(id);
            taskOptional = Optional.of(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskOptional;
    }

    @Override
    public boolean update(int id, Task task) {
        boolean rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery("""
                        UPDATE Task SET title = :title,
                        description = :description, done=:done
                        WHERE id = :id
                    """)
                    .setParameter("id", id)
                    .setParameter("title", task.getTitle())
                    .setParameter("description", task.getDescription())
                    .setParameter("done", task.isDone())
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Optional<Task> toDone(int id) {
        Optional<Task> taskOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("UPDATE Task SET done=true WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            Query<Task> query = session.createQuery(
                    "from Task where id=:id", Task.class);
            query.setParameter("id", id);
            taskOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskOptional;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery(
                            "DELETE Task WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> taskOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task where id=:id", Task.class);
            query.setParameter("id", id);
            taskOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskOptional;
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task", Task.class);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        List<Task> tasks = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task where done=:done", Task.class);
            query.setParameter("done", isDone);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }
}
