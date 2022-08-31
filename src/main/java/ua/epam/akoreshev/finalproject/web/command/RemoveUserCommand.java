package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.service.impl.UserServiceImpl;

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
        long userId = Long.parseLong(req.getParameter("user_id"));

        try {
            req.getSession().setAttribute("request_status", "failed");
            if (userService.removeUser(userId)) {
                req.getSession().setAttribute("request_status", "success");
            }
        } catch (ServiceException e) {
            req.getSession().setAttribute("request_status", "failed");
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
