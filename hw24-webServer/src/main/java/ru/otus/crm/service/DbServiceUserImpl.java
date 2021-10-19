package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceUserImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
    public User saveUser(User user) {
        return transactionManager.doInTransaction(session -> {
            if (user.getId() == null) {
                userDataTemplate.insert(session, user);
                log.info("created user: {}", user);

                return user;
            }
            userDataTemplate.update(session, user);
            log.info("updated user: {}", user);
            return user;
        });
    }

    @Override
    public Optional<User> getUser(long id) {
        return transactionManager.doInTransaction(session -> {
            var userOptional = userDataTemplate.findById(session, id);
            log.info("user: {}", userOptional);
            return userOptional;
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInTransaction(session -> {
            var userOptional = userDataTemplate.findByLogin(session, login);
            log.info("user: {}", userOptional);
            return userOptional;
        });
    }

    @Override
    public List<User> findAll() {
        return transactionManager.doInTransaction(session -> {
            var userList = userDataTemplate.findAll(session);
            log.info("userList:{}", userList);
            return userList;
       });
    }
}
