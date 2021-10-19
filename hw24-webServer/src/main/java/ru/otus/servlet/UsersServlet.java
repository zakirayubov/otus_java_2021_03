package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.servicies.TemplateProcessor;

import java.io.IOException;
import java.util.*;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<User> users = dbServiceClient.findAll();
        User randomUser = users.get(new Random(System.currentTimeMillis()).nextInt(users.size()));
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, randomUser);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
