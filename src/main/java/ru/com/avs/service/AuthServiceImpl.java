package ru.com.avs.service;

import static ru.com.avs.util.UserUtils.getHash;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.com.avs.model.Property;

@Service("AuthService")
@Scope("singleton")
public class AuthServiceImpl implements AuthService {

    private final PropertyService propertyService;
    private Boolean isAdmin = false;

    public AuthServiceImpl(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public boolean isSuccessAuth(String password) {

        if (password != null) {
            String currentPassword = propertyService.getProperty("password");
            return currentPassword.equals(getHash(password));
        } else {
            return false;
        }
    }

    @Override
    public boolean updatePassword(String newPassword) {
            Property property = propertyService.findByName("password");
            property.setValue(getHash(newPassword));
            propertyService.update(property);
            return true;
    }

    @Override
    public void setAdminMode(boolean enable) {
        isAdmin = enable;
    }

    @Override
    public boolean isAdminMode() {
        return isAdmin;
    }

    @Override
    public void logout() {
        isAdmin = false;
    }
}
