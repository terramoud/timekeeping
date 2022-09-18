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

public class SetStopTimeCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(SetStopTimeCommand.class);

    private final IntervalService intervalService;

    public SetStopTimeCommand(IntervalService intervalService) {
        this.intervalService = intervalService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        User user = (User) req.getSession().getAttribute("user");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long activityId = validator.getLong("activity_id");
        LocalDateTime stopTime = LocalDateTime.now();
        try {
            if (!intervalService.setFinishTimeForUserActivity(user.getId(), activityId, stopTime))
                putToSession(req, "stop_timekeeping.failed", true, LOG);
        } catch (IntervalException e) {
            putToSession(req, e.getMessage(), true, LOG);
        } catch (ServiceException e) {
            LOG.error("User hasn't set stop time");
            throw new CommandException("User hasn't set stop time", e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
