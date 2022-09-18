package ua.epam.akoreshev.finalproject.web.command;


import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.web.utils.LocalizedMessageBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Command {
    protected Command() {
    }

    public abstract String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException;

    public void putToSession(HttpServletRequest request, String message, boolean isError, Logger logger) {
        LocalizedMessageBuilder lmb = new LocalizedMessageBuilder(request);
        logger.debug("Session.setAttribute 'message': {}", lmb.getLocalizedMessage(message));
        request.getSession().setAttribute("message", lmb.getLocalizedMessage(message));
        request.getSession().setAttribute("isErrorMessage", isError);
    }

    @Override
    public String toString() {
        return "(Command is instanceof " + getClass().getSimpleName() + ")";
    }
}
