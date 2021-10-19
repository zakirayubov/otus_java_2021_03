package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.servicies.TemplateProcessor;
import ru.otus.servicies.auth.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;
import ru.otus.servlet.UsersApiServlet;
import ru.otus.servlet.UsersServlet;

import java.util.Arrays;


public class WebServerWithSecurity implements WebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceClient dbServiceClient;
    protected final TemplateProcessor templateProcessor;
    private final UserAuthService authService;
    private final Server server;
    private Gson gson;

    public WebServerWithSecurity(int port,DBServiceClient dbServiceClient, TemplateProcessor templateProcessor, Gson gson, UserAuthService authService) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
        this.gson = gson;
        this.authService = authService;
        this.server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));

        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(dbServiceClient, templateProcessor)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(dbServiceClient)), "/api/user/*");
        return servletContextHandler;
    }
}
