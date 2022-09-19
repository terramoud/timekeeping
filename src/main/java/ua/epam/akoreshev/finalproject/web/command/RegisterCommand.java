package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.exceptions.UserException;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RegisterCommand.class);
    private final UserService userService;

    public RegisterCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req,
                          HttpServletResponse resp) throws CommandException {
        LOG.debug("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        User user = new User();
        user.setLogin(validator.getString("login"));
        user.setEmail(validator.getString("email"));
        user.setPassword(validator.getString("password"));
        user.setRoleId(Role.getRoleId(Role.USER));
        String passwordConfirm = validator.getString("password_confirm");
        try {
            boolean isError = !userService.addUser(user, passwordConfirm);
            String message = (isError) ? "registration.failed" : "registration.success";
            putToSession(req, message, isError, LOG);
        } catch (UserException e) {
            LOG.warn(e);
            putToSession(req, e.getMessage(), true, LOG);
        } catch (ServiceException e) {
            LOG.error("Cannot register new user {}" , user);
            throw new CommandException("Cannot register new user", e);
        }
        LOG.debug("Command finished");
        return "?command=" + Path.INDEX_PAGE_COMMAND;
    }
}
