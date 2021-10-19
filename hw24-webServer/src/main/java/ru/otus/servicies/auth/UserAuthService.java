package ru.otus.servicies.auth;

public interface UserAuthService {

    boolean authenticate(String login, String password);
}
