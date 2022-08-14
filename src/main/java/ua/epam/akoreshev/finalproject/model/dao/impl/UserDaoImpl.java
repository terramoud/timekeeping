package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";

    private static final String SQL_CREATE_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?)";

    private static final String SQL_GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String SQL_GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";

    private static final String SQL_UPDATE_USER_BY_ID =
            "UPDATE users SET login = ?, email = ?, password = ?, role_id = ? WHERE id = ?";

    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

    private static final String SQL_FIND_ALL_USERS_BY_ACTIVITY_ID =
            "SELECT * FROM users INNER JOIN users_activities ON users.id = user_id WHERE activity_id = ?";

    private static final String SQL_FIND_ALL_USERS_BY_ACTIVITY_NAME = "SELECT * FROM users " +
            "INNER JOIN users_activities ON users.id = user_id " +
            "INNER JOIN activities ON users_activities.activity_id = activities.id " +
            "INNER JOIN translations ON activities.translation_id = translations.id " +
            "WHERE activities.name = ? OR translated_en = ? OR translated_uk = ?";

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
            LOG.error("DAO exception has been thrown to find all users from findAll() method, because {}", e.getMessage());
            throw new DaoException("Cannot find users " + e.getMessage(), e);
        }
        if (usersList.isEmpty()) {
            LOG.warn("Empty list users has been returned by findAll() method");
        }
        return usersList;
    }

    @Override
    public List<User> findAllUsersByActivity(long activityId) throws DaoException {
        LOG.debug("Obtained user's 'activity id' to find it at database is: {}", activityId);
        List<User> usersList = new LinkedList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_USERS_BY_ACTIVITY_ID)) {
            pst.setLong(1, activityId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                User user = new User();
                mapRowFromDB.map(rs, user);
                usersList.add(user);
            }
            LOG.debug("The {} users has been found by query to database", usersList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to find all users by activity id, because {}", e.getMessage());
            throw new DaoException("Cannot find users by activity id " + e.getMessage(), e);
        }
        return usersList;
    }

    @Override
    public List<User> findAllUsersByActivity(String activityName) throws DaoException {
        LOG.debug("Obtained user's 'activity name' to find it at database is: {}", activityName);
        List<User> usersList = new LinkedList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_USERS_BY_ACTIVITY_NAME)) {
            pst.setString(1, activityName);
            pst.setString(2, activityName);
            pst.setString(3, activityName);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                User user = new User();
                mapRowFromDB.map(rs, user);
                usersList.add(user);
            }
            LOG.debug("The {} users has been found by query to database", usersList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to find all users by activity name, because {}", e.getMessage());
            throw new DaoException("Cannot find users by activity name " + e.getMessage(), e);
        }
        if (usersList.isEmpty()) {
            LOG.warn("Empty list users has been returned by find all users by activity name");
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
            }
            LOG.debug("The {} rows has been added to database to create user", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to database to create user, because {}", e.getMessage());
            throw new DaoException("Cannot create user at database " + e.getMessage(), e);
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
            if (!rs.next()) throw new SQLException("There is not id: '" + userId + "' in table");
            mapRowFromDB.map(rs, user);
            LOG.debug("The user: {} has been found by query to database", user);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to get user by id, because {}", e.getMessage());
            throw new DaoException("Cannot read user by id " + e.getMessage(), e);
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
            if (!rs.next()) throw new SQLException("There is not login: '" + login + "' in table");
            mapRowFromDB.map(rs, user);
            LOG.debug("The user: {} has been found by query to database", user);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to get user by login, because {} ", e.getMessage());
            throw new DaoException("Cannot read user by login " + e.getMessage(), e);
        }
        return user;
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
            LOG.error("DAO exception has been thrown to update user, because {}", e.getMessage());
            throw new DaoException("Cannot update user at database " + e.getMessage(), e);
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
            LOG.error("DAO exception has been thrown to remove user by id, because {}", e.getMessage());
            throw new DaoException("Cannot delete user from database " + e.getMessage(), e);
        }
        return result;
    }
}
