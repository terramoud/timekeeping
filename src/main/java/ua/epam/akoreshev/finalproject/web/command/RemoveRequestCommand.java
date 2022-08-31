package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveRequestCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RemoveRequestCommand.class);

    private final RequestService requestService;
    public RemoveRequestCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        User user = (User) req.getSession().getAttribute("user");
        long activityId = Long.parseLong(req.getParameter("activity_id"));
        long typeId = Long.parseLong(req.getParameter("type_id"));
        long statusId = Long.parseLong(req.getParameter("status_id"));
        Request request = new Request();
        request.setUserId(user.getId());
        request.setActivityId(activityId);
        request.setTypeId(typeId);
        request.setStatusId(statusId);
        LOG.debug("Request entity is {}", request);
        try {
            req.getSession().setAttribute("request_status", "failed");
            if (requestService.createRequest(request)) {
                req.getSession().setAttribute("request_status", "success");
            }
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException("User cannot create request for activity, because " + e.getMessage());
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
