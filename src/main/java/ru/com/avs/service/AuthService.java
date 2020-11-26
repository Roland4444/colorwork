package ru.com.avs.service;

public interface AuthService {

    boolean isSuccessAuth(String password);

    boolean updatePassword(String newPassword);

    void setAdminMode(boolean enable);

    boolean isAdminMode();

    void logout();
}
