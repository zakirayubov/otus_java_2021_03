package ru.otus.servlet;

import ru.otus.crm.service.DBServiceClient;

import ru.otus.crm.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UsersApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;

    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public UsersApiServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = dbServiceClient.getUser(extractIdFromRequest(request)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(GSON.toJson(user));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
