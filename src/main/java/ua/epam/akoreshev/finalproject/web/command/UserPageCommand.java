package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class UserPageCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(UserPageCommand.class);

    private final ActivityService activityService;
    private final IntervalService intervalService;

    public UserPageCommand(ActivityService activityService, IntervalService intervalService) {
        this.activityService = activityService;
        this.intervalService = intervalService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command has started");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null)
            throw new CommandException("The user is not logged in yet");
        try {
            Map<Category, List<Activity>> allActivitiesByCategories = activityService.findAllActivitiesByCategories();
            LOG.debug("Request.setAttribute allActivitiesByCategories is: {}", allActivitiesByCategories);
            req.setAttribute("allActivitiesByCategories", allActivitiesByCategories);

            Map<Activity, Interval> activityIntervalMap = intervalService
                    .findIntervalsByUserAndActivities(user, activityService.findAllActivitiesByUser(user));
            LOG.debug("Request.setAttribute activityIntervalMap is: {}", activityIntervalMap);
            req.setAttribute("activityIntervalMap", activityIntervalMap);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.trace("Command has finished");
        return Path.USER_PAGE;
    }
}
