package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Creates base dao methods to work with database
 *
 * @param <T> any entity that represents table from database
 * @param <L> commonly represents the primary key value of entity
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface BaseDao<T extends Entity, L> {

    /**
     * Finds all entities from table from database
     *
     * @return list of all found entities
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<T> findAll() throws DaoException;

    /**
     * Creates new row in database table
     *
     * @param t any entity that extends the base entity {@link Entity}
     * @return {@code true} if new row was successfully added to database
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean create(T t) throws DaoException;

    /**
     * Reads entity from database by identifier (Primary key)
     *
     * @param id the primary key of the entity
     * @return the have been read entity or {@code null} if one isn't exists
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    T read(L id) throws DaoException;

    /**
     * Changes one or more properties of entity at database
     *
     * @param t any entity that extends the base entity {@link Entity}
     * @return {@code true} if new row was successfully changed
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean update(T t) throws DaoException;

    /**
     * Deletes entity from database by identifier (Primary key)
     *
     * @param id the primary key of the entity
     * @return {@code true} if new row was successfully deleted
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean delete(L id) throws DaoException;

    /**
     * Sets auto commit
     *
     * @param connection the database connection
     * @throws DaoException if {@link SQLException} was thrown by method
     */
    default void setAutoCommit(Connection connection) throws DaoException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throw new DaoException("Cannot finish transaction");
        }
    }

    /**
     * Sets rollback commit
     *
     * @param connection the database connection
     * @throws DaoException if {@link SQLException} was thrown by method
     */
    default void rollback(Connection connection) throws DaoException {
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throw new DaoException("Cannot rollback changes in db!");
        }
    }
}
