package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.model.dao.impl.*;

import java.sql.Connection;

public class MySQLDaoFactory implements DaoFactory {
    @Override
    public UserDao getUserDao(Connection connection) {
        return new UserDaoImpl(connection);
    }

    @Override
    public ActivityDao getActivityDao(Connection connection) {
        return new ActivityDaoImpl(connection);
    }

    @Override
    public IntervalDao getIntervalDao(Connection connection) {
        return new IntervalDaoImpl(connection);
    }

    @Override
    public RequestDao getRequestDao(Connection connection) {
        return new RequestDaoImpl(connection);
    }

    @Override
    public CategoryDao getCategoryDao(Connection connection) {
        return new CategoryDaoImpl(connection);
    }

    @Override
    public UserActivityDao getUserActivityDao(Connection connection) {
        return new UserActivityDaoImpl(connection);
    }
}
