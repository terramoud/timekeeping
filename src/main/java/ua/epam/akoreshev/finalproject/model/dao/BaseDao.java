package ua.epam.akoreshev.finalproject.model.dao;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Entity;


import java.util.List;

public interface BaseDao<T extends Entity, ID> {
    // List<?> findByPage(T entity, Pagination pagination, int offset, int limit);
    List<T> findAll() throws DaoException;

    boolean create(T t) throws DaoException;

    T read(ID id) throws DaoException;

    boolean update(T t) throws DaoException;

    boolean delete(ID id) throws DaoException;

}
