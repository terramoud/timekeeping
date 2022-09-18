package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.EditUserException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeUserCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ChangeUserCommand.class);
    private final UserService userService;

    public ChangeUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long userId = validator.getLong("user_id");
        String login = validator.getString("login");
        String email = validator.getString("email");
        int roleId = validator.getInt("role_id");
        User user = new User(userId, login, email, null, roleId);
        try {
            boolean isError = !userService.editUser(user);
            String message = (isError) ? "user.edit.failed" : "user.edit.success";
            putToSession(req, message, isError, LOG);
        } catch (ServiceException e) {
            LOG.error("Cannot edit user: {}", user);
            throw new CommandException("Cannot edit user", e);
        } catch (EditUserException e) {
            putToSession(req, e.getMessage(), true, LOG);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
