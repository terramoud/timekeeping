package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.RequestService;
import ua.epam.akoreshev.finalproject.web.service.impl.RequestServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApproveRequestCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ApproveRequestCommand.class);
    private final RequestService requestService;

    public ApproveRequestCommand(RequestServiceImpl requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        long requestId = Long.parseLong(req.getParameter("request_id"));
        long userId = Long.parseLong(req.getParameter("user_id"));
        long activityId = Long.parseLong(req.getParameter("activity_id"));
        long typeId = Long.parseLong(req.getParameter("type_id"));
        try {
            req.getSession().setAttribute("request_status", "failed");
            if (requestService.approveRequest(requestId, userId, activityId, typeId)) {
                req.getSession().setAttribute("request_status", "success");
            }
        } catch (ServiceException e) {
            req.getSession().setAttribute("request_status", "failed");
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
