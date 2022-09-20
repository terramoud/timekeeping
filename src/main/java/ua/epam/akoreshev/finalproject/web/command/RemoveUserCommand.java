package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveUserCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RemoveUserCommand.class);
    private final UserService userService;
    public RemoveUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long userId = validator.getLong("user_id");
        try {
            boolean isError = !userService.removeUser(userId);
            String message = (isError) ? "remove.user.failed" : "remove.user.success";
            putToSession(req, message, isError, LOG);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
