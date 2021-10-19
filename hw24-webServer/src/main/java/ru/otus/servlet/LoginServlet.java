package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.servicies.TemplateProcessor;
import ru.otus.servicies.auth.UserAuthService;


import java.io.IOException;
import java.util.Collections;

public class LoginServlet extends HttpServlet {

    private static final String ID_USER = "id";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";


    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/users");
        } else {
            response.sendRedirect("/login");
        }

    }

}
