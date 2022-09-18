package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.IntervalException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class SetStartTimeCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(SetStartTimeCommand.class);

    private final IntervalService intervalService;

    public SetStartTimeCommand(IntervalService intervalService) {
        this.intervalService = intervalService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        User user = (User) req.getSession().getAttribute("user");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long activityId = validator.getLong("activity_id");
        LocalDateTime startTime = LocalDateTime.now();
        try {
            if (!intervalService.setStartTimeForUserActivity(user.getId(), activityId, startTime))
                putToSession(req, "start_timekeeping.failed", true, LOG);
        } catch (IntervalException e) {
            putToSession(req, e.getMessage(), true, LOG);
        } catch (ServiceException e) {
            LOG.error("User hasn't set start time");
            throw new CommandException("User hasn't set start time", e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
