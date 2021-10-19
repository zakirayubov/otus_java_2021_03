package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DbServiceUserImpl;
import ru.otus.server.WebServer;
import ru.otus.server.WebServerWithSecurity;
import ru.otus.servicies.TemplateProcessor;
import ru.otus.servicies.TemplateProcessorImpl;
import ru.otus.servicies.auth.UserAuthService;
import ru.otus.servicies.auth.UserAuthServiceImpl;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var userTemplate = new DataTemplateHibernate<>(User.class);

        var dbServiceUser = new DbServiceUserImpl(transactionManager, userTemplate);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceUser);

        WebServer usersWebServer = new WebServerWithSecurity(WEB_SERVER_PORT, dbServiceUser,
                templateProcessor, gson, authService);

        usersWebServer.start();
        usersWebServer.join();
    }
}
