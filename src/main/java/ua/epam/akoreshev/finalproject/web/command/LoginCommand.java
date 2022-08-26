package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            User user = userService.findUserByLoginAndPassword(login, password);
            LOG.debug("User {} has logged in successfully", user);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            LOG.debug("Sesion.setAttribute user is: {}", user);
            if (Objects.equals(Role.getRole(user.getRoleId()), Role.USER))
                redirectCommand = Path.USER_PAGE_COMMAND;

            if (Objects.equals(Role.getRole(user.getRoleId()), Role.ADMIN))
                redirectCommand = Path.ADMIN_PAGE_COMMAND;

            LOG.debug("Redirect to : {}", redirectCommand);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException("User hasn't access to app. " + e.getMessage());
        }
        LOG.debug("Command finished");
        return redirectCommand;
    }
}
