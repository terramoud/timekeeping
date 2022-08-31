package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;

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
        long activityId = Long.parseLong(req.getParameter("activity_id"));

        try {
            req.getSession().setAttribute("request_status", "failed");
            if (activityService.removeActivity(activityId)) {
                req.getSession().setAttribute("request_status", "success");
            }
        } catch (ServiceException e) {
            req.getSession().setAttribute("request_status", "failed");
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
