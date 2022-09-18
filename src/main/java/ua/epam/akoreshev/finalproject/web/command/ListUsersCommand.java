package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListUsersCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ListUsersCommand.class);
    private final UserService userService;
    public static final String SORT_COLUMN = "order_by";

    public ListUsersCommand(UserService userService) {
        this.userService = userService;
    }


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator =  new RequestParameterValidator(req);
        int pageNumber = validator.getPaginationPageNumber("pageNumber");
        String columnName = validator.getString(SORT_COLUMN).isEmpty() ? "login" : validator.getString(SORT_COLUMN);
        String sortOrder = validator.getBoolean("desc") ? "DESC" : "ASC";
        try {
            int limit = 5;
            int totalPages = (int) Math.ceil(userService.getNumberUsers() / (double) limit);
            LOG.debug("ServletRequest.setAttribute totalPages is: {}", totalPages);
            req.setAttribute("totalPages", totalPages);
            int offset = (pageNumber - 1) * limit;
            List<User> users = userService.getUsers(limit, offset, columnName, sortOrder);
            List<UserRoleBean> userRoleBeans = users.stream()
                    .map(u -> new UserRoleBean(u, Role.getRole(u.getRoleId())))
                    .collect(Collectors.toList());
            LOG.debug("ServletRequest.setAttribute userRoleBeans is: {}", userRoleBeans);
            req.setAttribute("userRoleBeans", userRoleBeans);
            Map<Integer, String> userRoles = List.of(Role.values()).stream()
                    .collect(Collectors.toMap(Role::getRoleId, String::valueOf));
            LOG.debug("ServletRequest.setAttribute userRoles is: {}", userRoles);
            req.setAttribute("userRoles", userRoles);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.debug("ServletRequest.setAttribute order_by is: {}", columnName);
        req.setAttribute(SORT_COLUMN, columnName);
        LOG.debug("ServletRequest.setAttribute pageNumber is: {}", pageNumber);
        req.setAttribute("pageNumber", pageNumber);
        LOG.debug("ServletRequest.setAttribute desc is: {}", !validator.getBoolean("desc"));
        req.setAttribute("desc", !validator.getBoolean("desc"));
        LOG.trace("Command finished");
        return Path.LIST_USERS_PAGE;
    }
}
