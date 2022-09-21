package ua.epam.akoreshev.finalproject.web.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestParameterValidator {
    private final HttpServletRequest req;

    public RequestParameterValidator(HttpServletRequest request) {
        this.req = request;
    }

    public long getLong(String paramName) {
        long number;
        try {
            String temp = Long.parseUnsignedLong(req.getParameter(paramName)) + "";
            number = Long.parseLong(temp);
        } catch (NumberFormatException | NullPointerException e) {
            number = 0;
        }
        return number;
    }

    public int getInt(String paramName) {
        int number;
        try {
            String temp = Integer.parseUnsignedInt(req.getParameter(paramName)) + "";
            number = Integer.parseInt(temp);
        } catch (NumberFormatException | NullPointerException e) {
            number = 0;
        }
        return number;
    }

    public String getString(String paramName) {
        String parameterValue = req.getParameter(paramName);
        return (parameterValue == null) ? "" : parameterValue;
    }

    public int getPaginationPageNumber(String pageNumber) {
        return (getInt(pageNumber) == 0) ? 1 : getInt(pageNumber);
    }

    public boolean getBoolean(String desc) {
        return Boolean.parseBoolean(req.getParameter(desc));
    }
}
