package ru.job4j.todo.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmUserRepository implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            int id = (int) session.save(user);
            user.setId(id);
            userOptional = Optional.of(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> userOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User where login=:login and password=:password", User.class);
            query.setParameter("login", login);
            query.setParameter("password", password);
            userOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }
}
