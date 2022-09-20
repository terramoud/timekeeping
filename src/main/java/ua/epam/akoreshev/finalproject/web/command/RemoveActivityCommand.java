package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveActivityCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RemoveActivityCommand.class);
    private final ActivityService activityService;

    public RemoveActivityCommand(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long activityId = validator.getLong("activity_id");
        try {
            boolean isError = !activityService.removeActivity(activityId);
            String message = (isError) ? "activity.remove.failed" : "activity.remove.success";
            putToSession(req, message, isError, LOG);
        } catch (ServiceException e) {
            LOG.error("Cannot remove activity by id: {}", activityId);
            throw new CommandException("Cannot remove activity by id", e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
