package ua.epam.akoreshev.finalproject.web.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CategoryException;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.web.service.CategoryService;
import ua.epam.akoreshev.finalproject.web.utils.RequestParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditCategoryCommand extends Command {
    private static final Logger LOG = LogManager.getLogger(EditCategoryCommand.class);

    private final CategoryService categoryService;
    public EditCategoryCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        LOG.trace("Command starts");
        RequestParameterValidator validator = new RequestParameterValidator(req);
        long categoryId = validator.getLong("category_id");
        String nameEn = validator.getString("name_en");
        String nameUk = validator.getString("name_uk");
        Category category = new Category(categoryId, nameEn, nameUk);
        try {
            boolean isError = !categoryService.editCategory(category);
            String message = (isError) ? "category.edit.failed" : "category.edit.success";
            putToSession(req, message, isError, LOG);
        } catch (ServiceException e) {
            LOG.error(e);
            throw new CommandException(e.getMessage(), e);
        } catch (CategoryException e) {
            putToSession(req, e.getMessage(), true, LOG);
        }
        LOG.trace("Command finished");
        return req.getHeader("referer");
    }
}
