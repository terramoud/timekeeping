package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

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
        long activityId = Long.parseLong(req.getParameter("activity_id"));
        LocalDateTime stopTime = LocalDateTime.now();
        try {
            if (!intervalService.setFinishTimeForUserActivity(user.getId(), activityId, stopTime)) {
                req.getSession().setAttribute("request_status", "failed");
            }
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage());
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
