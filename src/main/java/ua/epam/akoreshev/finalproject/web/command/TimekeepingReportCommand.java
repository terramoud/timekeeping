package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class TimekeepingReportCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(TimekeepingReportCommand.class);

    private final IntervalService intervalService;
    public static final String SORT_COLUMN = "order_by";

    public TimekeepingReportCommand(IntervalService intervalService) {
        this.intervalService = intervalService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator =  new RequestParameterValidator(req);
        int pageNumber = validator.getPaginationPageNumber("pageNumber");
        String sortOrder = validator.getBoolean("desc") ? "DESC" : "ASC";
        String columnName = validator.getString(SORT_COLUMN).isEmpty() ? "login" : validator.getString(SORT_COLUMN);
        try {
            int limit = 5;
            int totalPages = (int) Math.ceil(intervalService.getNumberUsersActivities() / (double) limit);
            LOG.debug("ServletRequest.setAttribute totalPages is: {}", totalPages);
            req.setAttribute("totalPages", totalPages);
            int offset = (pageNumber - 1) * limit;
            List<UserStatistic> spentTimeForActivities =
                    intervalService.getStatisticsByUsers(limit, offset, columnName, sortOrder);
            LOG.debug("ServletRequest.setAttribute spentTimeForActivities is: {}", spentTimeForActivities);
            req.setAttribute("spentTimeForActivities", spentTimeForActivities);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.debug("ServletRequest.setAttribute pageNumber is: {}", pageNumber);
        req.setAttribute("pageNumber", pageNumber);
        LOG.debug("ServletRequest.setAttribute order_by is: {}", columnName);
        req.setAttribute(SORT_COLUMN, columnName);
        LOG.debug("ServletRequest.setAttribute desc is: {}", !validator.getBoolean("desc"));
        req.setAttribute("desc", !validator.getBoolean("desc"));
        LOG.trace("Command finished");
        return Path.REPORT_PAGE;
    }
}
