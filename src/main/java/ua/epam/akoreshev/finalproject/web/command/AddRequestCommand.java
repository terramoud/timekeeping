package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.RequestService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddRequestCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(AddRequestCommand.class);

    private final RequestService requestService;
    public AddRequestCommand(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        User user = (User) req.getSession().getAttribute("user");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        Request request = new Request();
        request.setUserId(user.getId());
        request.setActivityId(validator.getLong("activity_id"));
        request.setTypeId(validator.getLong("type_id"));
        request.setStatusId(validator.getLong("status_id"));
        LOG.debug("Request entity is {}", request);
        try {
            boolean isError = !requestService.createRequest(request);
            String message = (isError) ? "add.request.failed.message" : "add.request.success.message";
            putToSession(req, message, isError, LOG);
        } catch (RequestException e) {
            putToSession(req, e.getMessage(), true, LOG);
        } catch (ServiceException e) {
            LOG.error("User cannot create request: {}", request);
            throw new CommandException("User cannot create request for activity", e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
