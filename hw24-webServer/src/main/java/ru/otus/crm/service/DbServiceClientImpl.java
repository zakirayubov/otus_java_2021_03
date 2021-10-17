package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
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
    public List<User> findAll() {
        return transactionManager.doInTransaction(session -> {
            var userList = userDataTemplate.findAll(session);
            log.info("userList:{}", userList);
            return userList;
       });
    }
}
