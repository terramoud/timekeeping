package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;
import ua.epam.akoreshev.finalproject.web.command.Command;
import ua.epam.akoreshev.finalproject.web.listener.ContextListener;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class TimekeepingReportCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(TimekeepingReportCommand.class);

    private final IntervalService intervalService;
    private int LIMIT = 5;

    public TimekeepingReportCommand(IntervalService intervalService) {
        this.intervalService = intervalService;
    }


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        boolean hasPagination = req.getParameter("pageNumber") != null;
        int pageNumber = (hasPagination) ? Integer.parseInt(req.getParameter("pageNumber")) : 1;
        try {
            int totalPages = (int) Math.ceil(intervalService.getCountUsersActivities() / (double) LIMIT);
            LOG.debug("ServletRequest.setAttribute totalPages is: {}", totalPages);
            req.setAttribute("totalPages", totalPages);

            int offset = (pageNumber - 1) * LIMIT;
            List<UserStatistic> spentTimeForActivities = intervalService.getStatisticsByUsers(LIMIT, offset);
            LOG.debug("ServletRequest.setAttribute spentTimeForActivities is: {}", spentTimeForActivities);
            req.setAttribute("spentTimeForActivities", spentTimeForActivities);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage());
        }
        LOG.debug("ServletRequest.setAttribute pageNumber is: {}", pageNumber);
        req.setAttribute("pageNumber", pageNumber);
        LOG.trace("Command finished");
        return Path.REPORT_PAGE;
    }
}
