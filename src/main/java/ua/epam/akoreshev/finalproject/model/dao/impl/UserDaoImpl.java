package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String SQL_FIND_ALL_USERS_WITHOUT_ADMINS = "SELECT * FROM users WHERE role_id != ?";

    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";

    private static final String SQL_CREATE_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?)";

    private static final String SQL_GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String SQL_GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";

    private static final String SQL_UPDATE_USER_BY_ID =
            "UPDATE users SET login = ?, email = ?, password = ?, role_id = ? WHERE id = ?";

    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

    private static final String SQL_GET_NUMBER_USERS_WITHOUT_ADMINS =
            "SELECT COUNT(*) AS numRows FROM users WHERE role_id != ?";

    private final Mapper<User, PreparedStatement> mapRowToDB = (User user, PreparedStatement preparedStatement) -> {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setInt(4, user.getRoleId());
    };

    private final Mapper<ResultSet, User> mapRowFromDB = (ResultSet resultSet, User user) -> {
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setRoleId(resultSet.getInt("role_id"));
    };

    private final Connection connection;
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
        LOG.debug("Actual obtained connection for UserDao is: {}", this.connection);
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> usersList = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_USERS);
            LOG.trace("SQL query find all users to database has already been completed successfully");
            while (rs.next()) {
                User user = new User();
                mapRowFromDB.map(rs, user);
                usersList.add(user);
            }
            LOG.debug("The {} users has been found by query to database", usersList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find users", e, e.getErrorCode());
        }
        return usersList;
    }

    @Override
    public boolean create(User user) throws DaoException {
        LOG.debug("Obtained user entity to create it at database is: {}", user);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            mapRowToDB.map(user, pst);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to create user has already been completed successfully");
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
                LOG.debug("The source user entity has synchronized 'id' with created one at database and now represent the: {}", user);
            }
            if (rowCount == 0) {
                result = false;
                LOG.warn("The user wasn't created by query to database");
            }
            LOG.debug("The {} rows has been added to database to create user", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot create user at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public User read(Long userId) throws DaoException {
        LOG.debug("Obtained user 'id' to read it from database is: {}", userId);
        User user = new User();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_USER_BY_ID)) {
            pst.setLong(1, userId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an user from database has already been completed successfully");
            if (!rs.next()) return null;
            mapRowFromDB.map(rs, user);
            LOG.debug("The user: {} has been found by query to database", user);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read user by id", e, e.getErrorCode());
        }
        return user;
    }

    @Override
    public User read(String login) throws DaoException {
        LOG.debug("Obtained user 'login' to read it from database is: {}", login);
        User user = new User();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_USER_BY_LOGIN)) {
            pst.setString(1, login);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an user from database has already been completed successfully");
            if (!rs.next()) return null;
            mapRowFromDB.map(rs, user);
            LOG.debug("The user: {} has been found by query to database", user);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read user by login", e, e.getErrorCode());
        }
        return user;
    }

    @Override
    public long getNumberUsers() throws DaoException {
        long result = 0;
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_NUMBER_USERS_WITHOUT_ADMINS)) {
            pst.setInt(1, Role.getRoleId(Role.ADMIN));
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query find all 'users' to database has already been completed successfully");
            if (rs.next()) {
                result = rs.getLong("numRows");
            }
            LOG.debug("The {} rows has been found by query to database", result);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find 'users'", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public List<User> findAllUsers(int limit, int offset, String columnName, String sortOrder) throws DaoException {
        List<User> usersList = new LinkedList<>();
        String sqlParameters = " ORDER BY " + columnName
                .concat(" " + sortOrder)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        LOG.debug("SQL parameters are: {}", sqlParameters);
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_USERS_WITHOUT_ADMINS + sqlParameters)) {
            pst.setInt(1, Role.getRoleId(Role.ADMIN));
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query find all users to database has already been completed successfully");
            while (rs.next()) {
                User user = new User();
                mapRowFromDB.map(rs, user);
                usersList.add(user);
            }
            LOG.debug("The {} users has been found by query to database", usersList.size());
        } catch (SQLException e) {
            LOG.error("Cannot find users sorted by: {}", sqlParameters);
            throw new DaoException("Cannot find any users", e, e.getErrorCode());
        }
        return usersList;
    }

    @Override
    public boolean update(User user) throws DaoException {
        LOG.debug("Obtained user entity to update it at database is: {}", user);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_USER_BY_ID)) {
            mapRowToDB.map(user, pst);
            pst.setLong(5, user.getId());
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update an user from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The user wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to update user", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot update user at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean delete(Long userId) throws DaoException {
        LOG.debug("Obtained user 'id' to delete it from database is: {}", userId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_DELETE_USER_BY_ID)) {
            pst.setLong(1, userId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to delete an user from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The user wasn't deleted by query to database");
            }
            LOG.debug("The {} rows has been removed to delete user", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot delete user from database", e, e.getErrorCode());
        }
        return result;
    }
}
