package ua.epam.akoreshev.finalproject.web.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocalizedMessageBuilder {
    private static final Logger LOG = LogManager.getLogger(LocalizedMessageBuilder.class);
    public static final String LOCALIZATION_CONTEXT = "javax.servlet.jsp.jstl.fmt.localizationContext";

    private final Locale locale;
    private final String bundleName;
    private final HttpServletRequest req;

    public LocalizedMessageBuilder(HttpServletRequest req) {
        this.req = req;
        this.bundleName = getBundleName();
        this.locale = getLocale();
    }

    private Locale getLocale() {
        Locale result;
        try {
            String language = (String) req.getSession().getAttribute("language");
            result = Locale.forLanguageTag(language);
        } catch (NullPointerException | ClassCastException e) {
            result = Locale.getDefault();
        }
        return result;
    }

    private String getBundleName() {
        try {
            ServletContext context = req.getServletContext();
            return context.getInitParameter(LOCALIZATION_CONTEXT);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getLocalizedMessage(String key) {
        String result;
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, locale);
            result = resourceBundle.getString(key);
        } catch (MissingResourceException | NullPointerException | IllegalArgumentException e) {
            result = "Warning! Cannot load message about result of action";
            LOG.error("Cannot load message from bundle: '{}'", bundleName, e);
        }
        return result;
    }
}
