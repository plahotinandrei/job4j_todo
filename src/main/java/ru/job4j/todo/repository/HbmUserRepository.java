package ru.job4j.todo.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    private final static Logger LOG = LoggerFactory.getLogger(HbmUserRepository.class.getName());

    @Override
    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
            userOptional = Optional.of(user);
        } catch (Exception e) {
            LOG.error("Failed to create user", e);
        }
        return userOptional;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User where login=:login and password=:password",
                User.class,
                Map.of("login", login, "password", password)
        );
    }
}
