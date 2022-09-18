package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.util.List;

public interface CategoryDao extends BaseDao<Category, Long> {
    long getNumberCategories() throws DaoException;

    List<Category> findAll(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

