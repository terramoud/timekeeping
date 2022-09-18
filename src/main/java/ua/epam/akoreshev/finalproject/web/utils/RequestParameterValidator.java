package ua.epam.akoreshev.finalproject.web.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RequestParameterValidator {
    private static final Logger LOG = LogManager.getLogger(RequestParameterValidator.class);
    public static final String REGEXP = "^[0-9a-zA-Z]([0-9a-zA-Z_\\-]*[0-9a-zA-Z])?$";
    private final HttpServletRequest req;

    public RequestParameterValidator(HttpServletRequest request) {
        this.req = request;
    }

    public long getLong(String number) {
        long result;
        try {
            String temp = Long.parseUnsignedLong(req.getParameter(number)) + "";
            result = Long.parseLong(temp);
        } catch (NumberFormatException | NullPointerException e) {
            result = 0;
        }
        return result;
    }

    public int getInt(String number) {
        int result;
        try {
            String temp = Integer.parseUnsignedInt(req.getParameter(number)) + "";
            result = Integer.parseInt(temp);
        } catch (NumberFormatException | NullPointerException e) {
            result = 0;
        }
        return result;
    }

    public String getString(String str) {
        String param = req.getParameter(str);
        return (param != null && validateParameter(param)) ? param : "";
    }

    public int getPaginationPageNumber(String pageNumber) {
        return (getInt(pageNumber) == 0) ? 1 : getInt(pageNumber);
    }

    public boolean getBoolean(String desc) {
        return Boolean.parseBoolean(req.getParameter(desc));
    }

    private boolean validateParameter(String parameter) {
        try {
            return parameter != null && Pattern.matches(REGEXP, parameter);
        } catch (NullPointerException e) {
            return false;
        } catch (PatternSyntaxException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }
}
