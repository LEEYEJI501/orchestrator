package com.example.orchestrator.constants;
import java.util.List;

public class SecurityPathConstants {
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String USER_CHECK_USERNAME = "/users/check-username";
    public static final String USER_REGISTER = "/users/register";

    public static List<String> getPublicPaths() {
        return List.of(AUTH_LOGIN, AUTH_REGISTER, USER_CHECK_USERNAME, USER_REGISTER);
    }
}
