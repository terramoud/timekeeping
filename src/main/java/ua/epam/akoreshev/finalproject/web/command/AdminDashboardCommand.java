package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.web.command.Command;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AdminDashboardCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(AdminDashboardCommand.class);
    private final RequestService requestService;
    public static final int LIMIT = 5;

    public AdminDashboardCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        boolean hasPagination = req.getParameter("pageNumTableRequests") != null;
        boolean hasPaginationForArchive = req.getParameter("pageNumTableArchive") != null;
        int pageNumOfRequestsTable = (hasPagination) ? Integer.parseInt(req.getParameter("pageNumTableRequests")) : 1;
        int pageNumOfArchiveTable = (hasPaginationForArchive) ? Integer.parseInt(req.getParameter("pageNumTableArchive")) : 1;
        try {
            int totalPagesForTableRequests = (int) Math.ceil(requestService.getCountRequestsByStatuses(1) / (double) LIMIT);
            int offset = (pageNumOfRequestsTable - 1) * LIMIT;
            List<UserActivityRequest> requestsByStatusPending = requestService.findRequestsByStatuses(LIMIT, offset, new int[]{1});
            LOG.debug("ServletRequest.setAttribute totalPagesForTableRequests is: {}", totalPagesForTableRequests);
            req.setAttribute("totalPagesForTableRequests", totalPagesForTableRequests);
            LOG.debug("ServletRequest.setAttribute requestsByStatusPending is: {}", requestsByStatusPending);
            req.setAttribute("requestsByStatusPending", requestsByStatusPending);
            // second table
            int totalPagesForTableArchive = (int) Math.ceil(requestService.getCountRequestsByStatuses(2, 3) / (double) LIMIT);
            int offsetForTableArchive = (pageNumOfArchiveTable - 1) * LIMIT;
            List<UserActivityRequest> archiveOfRequests = requestService.findRequestsByStatuses(LIMIT, offsetForTableArchive, new int[]{2, 3});
            LOG.debug("ServletRequest.setAttribute totalPagesForTableArchive is: {}", totalPagesForTableArchive);
            req.setAttribute("totalPagesForTableArchive", totalPagesForTableArchive);
            LOG.debug("ServletRequest.setAttribute archiveOfRequests is: {}", archiveOfRequests);
            req.setAttribute("archiveOfRequests", archiveOfRequests);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage());
        }
        LOG.debug("ServletRequest.setAttribute pageNumTableRequests is: {}", pageNumOfRequestsTable);
        req.setAttribute("pageNumTableRequests", pageNumOfRequestsTable);
        LOG.debug("ServletRequest.setAttribute pageNumTableArchive is: {}", pageNumOfArchiveTable);
        req.setAttribute("pageNumTableArchive", pageNumOfArchiveTable);
        LOG.trace("Command finished");
        return Path.ADMIN_PAGE;
    }
}
