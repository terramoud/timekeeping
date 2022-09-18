package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.EditUserException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangePasswordCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ChangePasswordCommand.class);

    private final UserService userService;

    public ChangePasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long userId = validator.getLong("user_id");
        String oldPassword = validator.getString("old_password");
        String newPassword = validator.getString("new_password");
        String confirmNewPassword = validator.getString("confirm_new_password");
        try {
            putToSession(req, "password.change.failed", true, LOG);
            if (userService.changePassword(userId, oldPassword, newPassword, confirmNewPassword))
                putToSession(req, "password.change.success", false, LOG);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        } catch (EditUserException e) {
            LOG.warn(e);
            putToSession(req, e.getMessage(), true, LOG);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
