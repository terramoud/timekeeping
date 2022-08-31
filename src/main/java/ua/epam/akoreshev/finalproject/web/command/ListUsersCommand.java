package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ListUsersCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ListUsersCommand.class);
    private final UserService userService;
    private final int LIMIT = 5;

    public ListUsersCommand(UserService userService) {
        this.userService = userService;
    }


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        boolean hasPagination = req.getParameter("pageNumber") != null;
        int pageNumber = (hasPagination) ? Integer.parseInt(req.getParameter("pageNumber")) : 1;
        try {
            int totalPages = (int) Math.ceil(userService.getNumberUsers() / (double) LIMIT);
            LOG.debug("ServletRequest.setAttribute totalPages is: {}", totalPages);
            req.setAttribute("totalPages", totalPages);

            int offset = (pageNumber - 1) * LIMIT;
            List<User> users = userService.getUsers(LIMIT, offset);
            List<UserRoleBean> userRoleBeans = users.stream()
                    .map(u -> new UserRoleBean(u, Role.getRole(u.getRoleId()).toString()))
                    .collect(Collectors.toList());
            LOG.debug("ServletRequest.setAttribute userRoleBeans is: {}", userRoleBeans);
            req.setAttribute("userRoleBeans", userRoleBeans);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage());
        }
        LOG.debug("ServletRequest.setAttribute pageNumber is: {}", pageNumber);
        req.setAttribute("pageNumber", pageNumber);
        LOG.trace("Command finished");
        return Path.LIST_USERS_PAGE;
    }
}
