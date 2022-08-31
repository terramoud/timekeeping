package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ListActivitiesCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ListActivitiesCommand.class);

    private final ActivityService activityService;
    private final int LIMIT = 5;

    public ListActivitiesCommand(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        boolean hasPagination = req.getParameter("pageNumber") != null;
        int pageNumber = (hasPagination) ? Integer.parseInt(req.getParameter("pageNumber")) : 1;
        try {
            int totalPages = (int) Math.ceil(activityService.getNumberActivities() / (double) LIMIT);
            LOG.debug("ServletRequest.setAttribute totalPages is: {}", totalPages);
            req.setAttribute("totalPages", totalPages);

            int offset = (pageNumber - 1) * LIMIT;
            List<ActivityCategoryBean> activityCategoryBeans = activityService.getActivities(LIMIT, offset);
            LOG.debug("ServletRequest.setAttribute activityCategoryBeans is: {}", activityCategoryBeans);
            req.setAttribute("activityCategoryBeans", activityCategoryBeans);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage());
        }
        LOG.debug("ServletRequest.setAttribute pageNumber is: {}", pageNumber);
        req.setAttribute("pageNumber", pageNumber);
        LOG.trace("Command finished");
        return Path.LIST_ACTIVITIES_PAGE;
    }
}
