package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(LogoutCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        req.getSession().invalidate();
        LOG.trace("Command finished");
        return Path.INDEX_PAGE;
    }
}
