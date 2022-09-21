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

public class EditActivityCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(EditActivityCommand.class);
    private final ActivityService activityService;
    public EditActivityCommand(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long activityId = validator.getLong("activity_id");
        String nameEn = validator.getString("name_en");
        String nameUk = validator.getString("name_uk");
        long categoryId = validator.getLong("category_id");
        Activity activity = new Activity(activityId, nameEn, nameUk, categoryId);
        LOG.debug("Obtained activity entity from request parameters is '{}'", activity);
        try {
            boolean isError = !activityService.editActivity(activity);
            String message = (isError) ? "activity.edit.failed" : "activity.edit.success";
            putToSession(req, message, isError, LOG);
        } catch (ServiceException e) {
            LOG.error("Cannot edit activity: {}", activity);
            throw new CommandException("Cannot edit activity", e);
        } catch (ActivityException e) {
            putToSession(req, e.getMessage(), true, LOG);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
