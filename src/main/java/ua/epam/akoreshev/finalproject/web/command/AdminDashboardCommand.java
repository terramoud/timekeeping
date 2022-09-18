package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.web.service.RequestService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AdminDashboardCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(AdminDashboardCommand.class);
    private final RequestService requestService;
    public static final int LIMIT = 5;
    public static final String MAIN_TABLE_SORT_PARAMETER = "desc";
    public static final String ARCHIVE_TABLE_SORT_PARAMETER = "archiveDesc";
    public static final String MAIN_TABLE_SORT_COLUMN = "order_by";
    public static final String ARCHIVE_TABLE_SORT_COLUMN = "archiveOrder_by";

    public AdminDashboardCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator =  new RequestParameterValidator(req);
        String sortOrder = validator.getBoolean(MAIN_TABLE_SORT_PARAMETER) ? "DESC" : "ASC";
        String archiveSortOrder = validator.getBoolean(ARCHIVE_TABLE_SORT_PARAMETER) ? "DESC" : "ASC";
        String columnName = validator.getString(MAIN_TABLE_SORT_COLUMN).isEmpty()
                ? "login" : validator.getString(MAIN_TABLE_SORT_COLUMN);
        String archiveColumnName = validator.getString(ARCHIVE_TABLE_SORT_COLUMN).isEmpty()
                ? "login" : validator.getString(ARCHIVE_TABLE_SORT_COLUMN);
        int pageNumOfRequestsTable = validator.getPaginationPageNumber("pageNumTableRequests");
        int pageNumOfArchiveTable = validator.getPaginationPageNumber("pageNumTableArchive");

        try {
            int totalPagesForTableRequests = (int) Math.ceil(requestService.getCountRequestsByStatuses(1) / (double) LIMIT);
            int offset = (pageNumOfRequestsTable - 1) * LIMIT;
            List<UserActivityRequest> requestsByStatusPending =
                    requestService.findRequestsByStatuses(LIMIT, offset, columnName, sortOrder, new int[]{1});
            req.setAttribute("totalPagesForTableRequests", totalPagesForTableRequests);
            req.setAttribute("requestsByStatusPending", requestsByStatusPending);
            // second table
            int totalPagesForTableArchive = (int) Math.ceil(requestService.getCountRequestsByStatuses(2, 3) / (double) LIMIT);
            int offsetForTableArchive = (pageNumOfArchiveTable - 1) * LIMIT;
            List<UserActivityRequest> archiveOfRequests =
                    requestService.findRequestsByStatuses(LIMIT,
                                                            offsetForTableArchive,
                                                            archiveColumnName,
                                                            archiveSortOrder,
                                                            new int[]{2, 3});
            req.setAttribute("totalPagesForTableArchive", totalPagesForTableArchive);
            req.setAttribute("archiveOfRequests", archiveOfRequests);
            LOG.debug("ServletRequest.setAttribute totalPagesForTableRequests is: {}", totalPagesForTableRequests);
            LOG.debug("ServletRequest.setAttribute requestsByStatusPending is: {}", requestsByStatusPending);
            LOG.debug("ServletRequest.setAttribute totalPagesForTableArchive is: {}", totalPagesForTableArchive);
            LOG.debug("ServletRequest.setAttribute archiveOfRequests is: {}", archiveOfRequests);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        req.setAttribute("pageNumTableRequests", pageNumOfRequestsTable);
        req.setAttribute("pageNumTableArchive", pageNumOfArchiveTable);
        req.setAttribute(MAIN_TABLE_SORT_COLUMN, columnName);
        req.setAttribute(ARCHIVE_TABLE_SORT_COLUMN, archiveColumnName);
        req.setAttribute(MAIN_TABLE_SORT_PARAMETER, !validator.getBoolean(MAIN_TABLE_SORT_PARAMETER));
        req.setAttribute(ARCHIVE_TABLE_SORT_PARAMETER, !validator.getBoolean(ARCHIVE_TABLE_SORT_PARAMETER));
        LOG.debug("ServletRequest.setAttribute pageNumTableRequests is: {}", pageNumOfRequestsTable);
        LOG.debug("ServletRequest.setAttribute pageNumTableArchive is: {}", pageNumOfArchiveTable);
        LOG.debug("ServletRequest.setAttribute order_by is: {}", columnName);
        LOG.debug("ServletRequest.setAttribute archiveOrder_by is: {}", archiveColumnName);
        LOG.debug("ServletRequest.setAttribute desc is: {}", !validator.getBoolean(MAIN_TABLE_SORT_PARAMETER));
        LOG.debug("ServletRequest.setAttribute archiveDesc is: {}",
                !validator.getBoolean(ARCHIVE_TABLE_SORT_PARAMETER));
        LOG.trace("Command finished");
        return Path.ADMIN_PAGE;
    }
}
