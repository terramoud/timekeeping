package ua.epam.akoreshev.finalproject.model.dao;

import java.sql.Connection;

public interface DaoFactory {
    UserDao getUserDao(Connection connection);

    ActivityDao getActivityDao(Connection connection);

    IntervalDao getIntervalDao(Connection connection);

    RequestDao getRequestDao(Connection connection);

    CategoryDao getCategoryDao(Connection connection);

    UserActivityDao getUserActivityDao(Connection connection);

    static DaoFactory getDaoFactory() {
        return new MySQLDaoFactory();
    }
}
