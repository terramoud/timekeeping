package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveAccountCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RemoveAccountCommand.class);
    private final UserService userService;

    public RemoveAccountCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long userId = validator.getLong("user_id");
        try {
            if (!userService.removeUser(userId)) {
                putToSession(req, "remove.account.failed", true, LOG);
                return req.getHeader("referer");
            }
        } catch (ServiceException e) {
            LOG.error("Cannot remove account by user id: {}", userId);
            throw new CommandException("Cannot remove account by user id", e);
        }
        LOG.trace("Command finished");
        req.getSession().invalidate();
        return Path.INDEX_PAGE;
    }
}
