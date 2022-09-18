package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ListActivitiesCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ListActivitiesCommand.class);

    private final ActivityService activityService;
    public static final String SORT_COLUMN = "order_by";
    public static final String SORT_PARAMETER = "desc";

    public ListActivitiesCommand(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        String columnName = validator.getString(SORT_COLUMN).isEmpty()
                ? "activities.name_en" : validator.getString(SORT_COLUMN);
        String sortOrder = validator.getBoolean(SORT_PARAMETER) ? "DESC" : "ASC";
        int pageNumber = validator.getPaginationPageNumber("pageNumber");
        try {
            int limit = 5;
            int totalPages = (int) Math.ceil(activityService.getNumberActivities() / (double) limit);
            LOG.debug("ServletRequest.setAttribute totalPages is: {}", totalPages);
            req.setAttribute("totalPages", totalPages);
            int offset = (pageNumber - 1) * limit;
            List<ActivityCategoryBean> activityCategoryBeans = activityService.getActivities(limit, offset, columnName, sortOrder);
            LOG.debug("ServletRequest.setAttribute activityCategoryBeans is: {}", activityCategoryBeans);
            req.setAttribute("activityCategoryBeans", activityCategoryBeans);
            List<Category> categories = activityService.findAllCategories();
            LOG.debug("ServletRequest.setAttribute categories is: {}", categories);
            req.setAttribute("categories", categories);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.debug("ServletRequest.setAttribute order_by is: {}", columnName);
        req.setAttribute(SORT_COLUMN, columnName);
        LOG.debug("ServletRequest.setAttribute pageNumber is: {}", pageNumber);
        req.setAttribute("pageNumber", pageNumber);
        LOG.debug("ServletRequest.setAttribute desc is: {}", !validator.getBoolean(SORT_PARAMETER));
        req.setAttribute(SORT_PARAMETER, !validator.getBoolean(SORT_PARAMETER));
        LOG.trace("Command finished");
        return Path.LIST_ACTIVITIES_PAGE;
    }
}
