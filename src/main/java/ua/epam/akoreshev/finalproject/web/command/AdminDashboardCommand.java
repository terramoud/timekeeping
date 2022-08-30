package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.web.command.Command;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminDashboardCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(AdminDashboardCommand.class);

    public AdminDashboardCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    private final RequestService requestService;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {

        boolean hasPagination = req.getParameter("pageNumTableRequests") != null;
        boolean hasPaginationForArchive = req.getParameter("pageNumTableArchive") != null;
        int pageNumTableRequests = (hasPagination) ? Integer.parseInt(req.getParameter("pageNumTableRequests")) : 1;
        int pageNumTableArchive = (hasPaginationForArchive) ? Integer.parseInt(req.getParameter("pageNumTableArchive")) : 1;



        req.setAttribute("totalPagesForTableRequests", 10);
        req.setAttribute("totalPagesForTableArchive", 12);
        /*requestService.*/


        LOG.debug("Request.setAttribute pageNumTableRequests is: {}", pageNumTableRequests);
        req.setAttribute("pageNumTableRequests", pageNumTableRequests);
        LOG.debug("Request.setAttribute pageNumTableArchive is: {}", pageNumTableArchive);
        req.setAttribute("pageNumTableArchive", pageNumTableArchive);
        return Path.ADMIN_PAGE;
    }
}
