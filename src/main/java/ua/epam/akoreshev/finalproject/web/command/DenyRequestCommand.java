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

public class DenyRequestCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(DenyRequestCommand.class);
    private final RequestService requestService;

    public DenyRequestCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long requestId = validator.getLong("request_id");
        try {
            putToSession(req, "request.reject.failed", true, LOG);
            if (requestService.rejectRequest(requestId)) {
                putToSession(req, "request.reject.success", false, LOG);
            }
        } catch (RequestException e) {
            putToSession(req, e.getMessage(), true, LOG);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
