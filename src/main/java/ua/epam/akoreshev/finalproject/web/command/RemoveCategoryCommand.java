package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.web.service.CategoryService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveCategoryCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(RemoveCategoryCommand.class);
    private final CategoryService categoryService;

    public RemoveCategoryCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long categoryId = validator.getLong("category_id");
        try {
            putToSession(req, "category.remove.failed", true, LOG);
            if (categoryService.removeCategory(categoryId)) {
                putToSession(req, "category.remove.success", false, LOG);
            }
        } catch (ServiceException e) {
            LOG.error("Cannot remove category by id: {}", categoryId);
            throw new CommandException("Cannot remove category by id", e);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
