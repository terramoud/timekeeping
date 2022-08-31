package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.RequestService;
import ua.epam.akoreshev.finalproject.web.service.impl.RequestServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DenyRequestCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(DenyRequestCommand.class);
    private final RequestService requestService;

    public DenyRequestCommand(RequestServiceImpl requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        long requestId = Long.parseLong(req.getParameter("request_id"));
        try {
            req.getSession().setAttribute("request_status", "failed");
            if (requestService.rejectRequest(requestId)) {
                req.getSession().setAttribute("request_status", "success");
            }
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage());
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
