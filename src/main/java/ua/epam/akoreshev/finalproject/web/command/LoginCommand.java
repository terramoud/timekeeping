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
import java.util.Objects;

public class LoginCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);
    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.debug("Command starts");
        String redirectCommand = null;
        RequestParameterValidator validator = new RequestParameterValidator(req);
        String login = validator.getString("login");
        String password = validator.getString("password");
        try {
            User user = userService.findUserByLoginAndPassword(login, password);
            req.getSession().setAttribute("user", user);
            LOG.debug("Session.setAttribute user is: {}", user);
            if (Objects.equals(Role.getRole(user.getRoleId()), Role.USER))
                redirectCommand = Path.USER_PAGE_COMMAND;
            if (Objects.equals(Role.getRole(user.getRoleId()), Role.ADMIN))
                redirectCommand = Path.ADMIN_PAGE_COMMAND;
            LOG.debug("Redirect to : {}", redirectCommand);
        } catch (UserException e) {
            putToSession(req, e.getMessage(), true, LOG);
            redirectCommand = "?command=" + Path.INDEX_PAGE_COMMAND;
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.debug("Command finished");
        return redirectCommand;
    }
}
