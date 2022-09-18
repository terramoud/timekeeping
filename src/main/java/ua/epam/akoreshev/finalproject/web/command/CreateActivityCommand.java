package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.ActivityException;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateActivityCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(CreateActivityCommand.class);

    private final ActivityService activityService;

    public CreateActivityCommand(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        String nameEn = validator.getString("name_en");
        String nameUk = validator.getString("name_uk");
        long categoryId = validator.getLong("category_id");
        Activity activity = new Activity(0, nameEn, nameUk, categoryId);
        try {
            boolean isError = !activityService.createActivity(activity);
            String message = (isError) ? "activity.create.failed" : "activity.create.success";
            putToSession(req, message, isError, LOG);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        } catch (ActivityException e) {
            putToSession(req, e.getMessage(), true, LOG);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
