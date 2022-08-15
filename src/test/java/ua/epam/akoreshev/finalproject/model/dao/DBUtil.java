package ua.epam.akoreshev.finalproject.model.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {
    static final String URL = "jdbc:mysql://localhost:3306/timekeeping?useSSL=false&serverTimezone=Europe/Kiev";
    static final String USER = "root";
    static final String PASSWORD = "ffff";

    public static Connection getConnection() throws DaoException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            throw new DaoException("Cannot set connection", throwables);
        }
    }
}
