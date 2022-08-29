package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RegisterCommand.class);
    private final UserService userService;

    public RegisterCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.debug("Command starts");
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        user.setRoleId(Role.getRoleId(Role.USER));
        try {
            if (userService.addUser(user)) {
                LOG.trace("Registration completed");
            }
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException("Cannot register new user", e);
        }
        LOG.debug("Command finished");
        return req.getHeader("referer");
    }
}
