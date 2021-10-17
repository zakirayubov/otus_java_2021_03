package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DbServiceClientImpl;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var userTemplate = new DataTemplateHibernate<>(User.class);
///
        var dbServiceUser = new DbServiceClientImpl(transactionManager, userTemplate);

        log.info("All users");
        dbServiceUser.findAll().forEach(user -> log.info("user:{}", user));
    }
}
