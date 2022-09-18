package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.CategoryException;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.CategoryDao;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.web.service.CategoryService;
import ua.epam.akoreshev.finalproject.web.utils.CategoryValidator;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOG = LogManager.getLogger(CategoryServiceImpl.class);
    private final CategoryDao categoryDao;
    private final CategoryValidator categoryValidator;

    public CategoryServiceImpl(CategoryDao categoryDao, CategoryValidator categoryValidator) {
        this.categoryDao = categoryDao;
        this.categoryValidator = categoryValidator;
    }

    @Override
    public long getNumberCategories() throws ServiceException {
        try {
            return categoryDao.getNumberCategories();
        } catch (DaoException e) {
            LOG.error("Cannot count categories");
            throw new ServiceException("Cannot count categories", e);
        }
    }

    @Override
    public List<Category> getCategories(int limit, int offset, String columnName, String sortOrder) throws ServiceException {
        try {
            return categoryDao.findAll(limit, offset, columnName, sortOrder);
        } catch (DaoException e) {
            LOG.error("Cannot find any categories");
            throw new ServiceException("Cannot find any categories", e);
        }
    }

    @Override
    public boolean removeCategory(long categoryId) throws ServiceException {
        try {
            return categoryValidator.validateId(categoryId) && categoryDao.delete(categoryId);
        } catch (DaoException e) {
            LOG.error("Cannot remove category by id: {}", categoryId);
            throw new ServiceException("Cannot remove category", e);
        }
    }

    @Override
    public boolean editCategory(Category category) throws CategoryException, ServiceException {
        if (!categoryValidator.validate(category)) {
            LOG.warn("Invalid category names: {}", category);
            throw new CategoryException("category.error.invalid.names");
        }
        try {
            Category categoryToEdit = categoryDao.read(category.getId());
            LOG.debug("Category to edit is: {}", categoryToEdit);
            if (categoryToEdit == null)
                throw new CategoryException("category.error.not_exists");
            return categoryDao.update(category);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                LOG.warn("Duplicate category error code is: {}", e.getSqlErrorCode());
                throw new CategoryException("category.error.duplicate.name");
            }
            LOG.error("Cannot edit category {}", category);
            throw new ServiceException("Cannot edit category", e);
        }
    }

    @Override
    public boolean createCategory(Category category) throws CategoryException, ServiceException {
        if (!categoryValidator.validate(category)) {
            LOG.warn("Invalid category names: {}", category);
            throw new CategoryException("category.error.invalid.names");
        }
        try {
            LOG.debug("Category to create it is: {}", category);
            return categoryDao.create(category);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                LOG.warn("Category has duplicated name: {}", category);
                throw new CategoryException("category.error.duplicate.name");
            }
            LOG.error("Cannot create new category {}", category);
            throw new ServiceException("Cannot create new category", e);
        }
    }
}
