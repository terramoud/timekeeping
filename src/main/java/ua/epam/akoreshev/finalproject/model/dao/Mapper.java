package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Entity;

import java.sql.SQLException;

/**
 * Functional interface used in DAO implementations for mapping
 * entities from/to database
 *
 * @param <S> source object for mapping {@link java.sql.ResultSet} or entity
 * @param <R> mapping resulting entity or creates database table row
 *            using the {@link java.sql.PreparedStatement}
 * @author Alexander Koreshev
 * @since 1.0
 */
@FunctionalInterface
public interface Mapper<S, R> {
    /**
     * Maps resulting entity from source {@link java.sql.ResultSet}
     * or adds row to database by source {@link Entity} entity
     *
     * @param source    can be {@link Entity} or {@link java.sql.ResultSet}
     * @param resulting can be {@link Entity} or {@link java.sql.PreparedStatement}
     * @throws DaoException if {@link SQLException} was thrown
     * @throws SQLException if the CRUD operation with the database fails
     */
    void map(S source, R resulting) throws DaoException, SQLException;
}
