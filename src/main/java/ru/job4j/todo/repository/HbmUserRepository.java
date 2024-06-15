package ru.job4j.todo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
            userOptional = Optional.of(user);
        } catch (Exception ignored) {

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
