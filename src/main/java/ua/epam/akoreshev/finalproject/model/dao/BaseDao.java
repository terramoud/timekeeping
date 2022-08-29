package ua.epam.akoreshev.finalproject.model.dao;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Entity;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T extends Entity, ID> {
    // List<?> findByPage(T entity, Pagination pagination, int offset, int limit);
    List<T> findAll() throws DaoException;

    boolean create(T t) throws DaoException;

    T read(ID id) throws DaoException;

    boolean update(T t) throws DaoException;

    boolean delete(ID id) throws DaoException;

    default void setAutoCommit(Connection connection) throws DaoException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throw new DaoException("Cannot finish transaction");
        }
    }

    default void rollback(Connection connection) throws DaoException {
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throw new DaoException("Cannot rollback changes in db!");
        }
    }

}
