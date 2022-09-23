package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Creates additional dao methods to work with 'users' table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface UserDao extends BaseDao<User, Long> {

    /**
     * Reads {@link User} entity from database by login
     *
     * @param login the user unique login
     * @return the {@link User} entity or {@code null} if one isn't exists
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    User read(String login) throws DaoException;


    /**
     * Counts the number of rows in 'users' table where user isn't admin
     *
     * @return the number users without admins
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    int getNumberUsers() throws DaoException;

    /**
     * Reads the rows in 'users' table where {@link User}
     * isn't admin and returns list these users have been sorted them
     * by table column name and by sort order
     *
     * @param limit      number of records to find
     * @param offset     table row number which represents start point for limit
     * @param columnName table column name
     * @param sortOrder  descending or ascending order switcher
     * @return the sorted and truncated by limit list of {@link User} entities
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<User> findAllUsers(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

