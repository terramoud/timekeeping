package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Creates additional dao methods to work with the 'categories' table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface CategoryDao extends BaseDao<Category, Long> {

    /**
     * Counts the number of rows in the 'categories' table
     *
     * @return the number rows in the table
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    int getNumberCategories() throws DaoException;

    /**
     * Finds all rows in the 'categories' table
     * Creates the list of all {@link Category} entities and
     * returns those activities have been sorted them by table
     * column name and by sort order
     *
     * @param limit      number of records to find
     * @param offset     table row number which represents start point for limit
     * @param columnName table column name
     * @param sortOrder  descending or ascending order switcher
     * @return the sorted and truncated by limit list of {@link Category} entities
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<Category> findAll(int limit,
                           int offset,
                           String columnName,
                           String sortOrder) throws DaoException;
}

