package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.RequestService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApproveRequestCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(ApproveRequestCommand.class);
    private final RequestService requestService;

    public ApproveRequestCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long requestId = validator.getLong("request_id");
        long userId = validator.getLong("user_id");
        long activityId = validator.getLong("activity_id");
        long typeId = validator.getLong("type_id");
        try {
            boolean isError = !requestService.approveRequest(requestId, userId, activityId, typeId);
            String message = (isError) ? "request.approve.failed" : "request.approve.success";
            putToSession(req, message, isError, LOG);
        } catch (RequestException e) {
            putToSession(req, e.getMessage(), true, LOG);
        } catch (ServiceException e) {
            LOG.error("Admin cannot approve request");
            throw new CommandException("Admin cannot approve request", e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
