package ua.epam.akoreshev.finalproject.web.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.model.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class EntityValidator {
    private static final Logger LOG = LogManager.getLogger(EntityValidator.class);
    public static final String ENTITY_NAME_REGEXP = "^[\\p{L}][\\p{L} \\-']{0,30}[\\p{L}]$";

    protected boolean validateField(String field) {
        try {
            return field != null && Pattern.matches(ENTITY_NAME_REGEXP, field);
        } catch (NullPointerException e) {
            return false;
        } catch (PatternSyntaxException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    protected String invokeMethod(Entity entity, Method method) {
        try {
            return String.valueOf(method.invoke(entity));
        } catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
            LOG.error("Cannot invoke {} method", method.getName(), e);
            return "";
        }
    }
}
