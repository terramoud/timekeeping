package ua.epam.akoreshev.finalproject.model.dao;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;

import java.sql.SQLException;

/**
 * Functional interface used in DAO implementations for mapping
 * entities from/to Database
 * @param <S> - source object for mapping ResultSet or entity
 * @param <R> - mapping resulting entity
 */
@FunctionalInterface
public interface Mapper <S, R> {
    void map(S source, R resulting) throws DaoException, SQLException;
}
