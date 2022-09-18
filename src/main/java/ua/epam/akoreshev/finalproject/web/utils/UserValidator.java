package ua.epam.akoreshev.finalproject.web.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UserValidator {
    private static final Logger LOG = LogManager.getLogger(UserValidator.class);
    private static final String EMAIL_REGEXP = "^\\w+([.-]?\\w+){0,249}@\\w+([.-]?\\w+){0,249}(\\.\\w{2,3}){1,249}$";
    private static final String LOGIN_REGEXP = "^[\\p{L}][\\p{L}0-9]{4,31}$";
    private static final String PASSWORD_REGEXP = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,31}$";

    public boolean validateId(long userId) {
        return userId >= 0 && userId <= Integer.MAX_VALUE;
    }

    public boolean validateRoleId(int roleId) {
        return roleId > 0;
    }

    public boolean validateLogin(String login) {
        return validateField(login, LOGIN_REGEXP) && login.length() <= 32;
    }

    public boolean validateEmail(String email) {
        return validateField(email, EMAIL_REGEXP) && email.length() <= 255;
    }

    public boolean validatePassword(String password) {
        return validateField(password, PASSWORD_REGEXP) && password.length() <= 32;
    }

    public boolean validate(User user) {
        return validateId(user.getId())
                && validateLogin(user.getLogin())
                && validateEmail(user.getEmail())
                && validatePassword(user.getPassword())
                && validateRoleId(user.getRoleId());
    }

    private boolean validateField(String field, String regexp) {
        try {
            return field != null && Pattern.matches(regexp, field);
        } catch (NullPointerException e) {
            return false;
        } catch (PatternSyntaxException e) {
            LOG.error(e);
            return false;
        }
    }
}
