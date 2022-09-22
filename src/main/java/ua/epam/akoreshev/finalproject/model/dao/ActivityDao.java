package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Creates additional dao methods to work with the 'activities'
 * and the 'categories' tables
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface ActivityDao extends BaseDao<Activity, Long> {

    /**
     * Groups all rows in 'activities' table by {@link Category} primary key
     *
     * @param categoryId the primary key of {@link Category} entity
     * @return the list of {@link Activity} entities within one category
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<Activity> findAllActivitiesByCategory(long categoryId) throws DaoException;


    /**
     * Finds all rows in the 'activities' table by {@link User} primary key
     *
     * @param userId the primary key of {@link User} entity
     * @return the list {@link Activity} entities that have obtained user
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<Activity> findAllActivitiesByUser(long userId) throws DaoException;

    /**
     * Finds all rows in table 'categories'
     *
     * @return the list of all the {@link Category} entities in the table
     * @throws DaoException with the {@link SQLException#getErrorCode()}
     *                      if the {@link SQLException} was thrown by method
     */
    List<Category> findAllCategories() throws DaoException;

    /**
     * Counts the number of rows in the 'activities' table
     *
     * @return the number rows in the table
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    int getNumberActivities() throws DaoException;

    /**
     * Finds all rows in the 'activities' and the 'categories' tables
     * Creates the list of java bean that contains pairs
     * {@link Activity} entity and corresponding {@link Category} entity
     * and returns those beans have been sorted them by table column
     * name and by sort order
     *
     * @param limit number of records to find
     * @param offset table row number which represents start point for limit
     * @param columnName table column name
     * @param sortOrder descending or ascending order switcher
     * @return the sorted and truncated by limit list of
     *         the {@link ActivityCategoryBean} beans
     * @throws DaoException with the {@link SQLException#getErrorCode()}
     *                      if the {@link SQLException} was thrown by method
     */
    List<ActivityCategoryBean> findAllActivities(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

