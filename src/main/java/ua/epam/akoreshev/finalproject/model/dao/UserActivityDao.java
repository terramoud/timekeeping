package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Creates additional dao methods to work with 'users_activities' table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface UserActivityDao extends BaseDao<UserActivity, Long> {

    /**
     * Removes the {@link UserActivity} entity by obtained identifiers
     *
     * @param userId     the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @return {@code true} if row in the table was successfully changed
     * @throws DaoException with the {@link SQLException#getErrorCode()}
     *                      if the {@link SQLException} was thrown by method
     */
    boolean delete(long userId, long activityId) throws DaoException;

    /**
     * Reads the {@link UserActivity} entity by obtained identifiers
     *
     * @param userId     the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @return the {@link UserActivity} entity or {@code null} if one
     *         isn't exists
     * @throws DaoException with the {@link SQLException#getErrorCode()}
     *                      if the {@link SQLException} was thrown by method
     */
    UserActivity read(long userId, long activityId) throws DaoException;

    /**
     * Finds all rows in the 'activities' and the 'users' and the 'users_activities' tables
     * Creates the list of of {@link UserActivityBean} beans that contains pairs
     * {@link User} entity and corresponding {@link Activity} entity
     * and returns those beans have been sorted them by table column
     * name and by sort order and uses offset like start row number in tables
     *
     * @param limit      number of records to find
     * @param offset     table row number which represents start point for limit
     * @param columnName table column name
     * @param sortOrder  descending or ascending order switcher
     * @return the sorted and truncated by limit list of
     *         the {@link UserActivityBean} beans
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<UserActivityBean> findAll(int limit,
                                   int offset,
                                   String columnName,
                                   String sortOrder) throws DaoException;

    @Override
    default boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    default UserActivity read(Long id) {
        throw new UnsupportedOperationException();
    }
}

