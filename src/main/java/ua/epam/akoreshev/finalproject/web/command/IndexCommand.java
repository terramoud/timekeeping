package ua.epam.akoreshev.finalproject.web.command;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.model.entity.Role;
import ua.epam.akoreshev.finalproject.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;


public class IndexCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(IndexCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        LOG.trace("Command starts");
        String result = Path.INDEX_PAGE;
//        HttpSession session = req.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            LOG.debug("Command finished");
//            return result;
//        }
//        User user = (User) session.getAttribute("user");
//        Role userRole = Role.getRole(user.getRoleId());
//        LOG.debug("Current user role is {}", userRole);
//        if (userRole == Role.USER) {
//            result = Path.USER_PAGE;
//            LOG.debug("Forward user command is: {}", Path.USER_PAGE);
//        }
//        if (userRole == Role.ADMIN) {
//            result = Path.ADMIN_PAGE;
//            LOG.debug("Forward admin command is: {}", Path.ADMIN_PAGE);
//        }
        LOG.trace("Command finished");
        return result;
    }
}
