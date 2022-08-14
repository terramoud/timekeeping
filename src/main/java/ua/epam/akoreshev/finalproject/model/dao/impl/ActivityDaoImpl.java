package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.sql.*;
import java.util.List;

public class ActivityDaoImpl implements ActivityDao {
    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";

    private static final String SQL_CREATE_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?)";

    private static final String SQL_GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String SQL_GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";

    private static final String SQL_UPDATE_USER_BY_ID =
            "UPDATE users SET login = ?, email = ?, password = ?, role_id = ? WHERE id = ?";

    private static final String SQL_DELETE_ACTIVITY_BY_ID = "DELETE FROM activities WHERE id = ?";

    private static final String SQL_FIND_ALL_USERS_BY_ACTIVITY_ID =
            "SELECT * FROM users INNER JOIN users_activities ON users.id = user_id WHERE activity_id = ?";

    private static final String SQL_FIND_ALL_USERS_BY_ACTIVITY_NAME = "SELECT * FROM users " +
            "INNER JOIN users_activities ON users.id = user_id " +
            "INNER JOIN activities ON users_activities.activity_id = activities.id " +
            "INNER JOIN translations ON activities.translation_id = translations.id " +
            "WHERE activities.name = ? OR translated_en = ? OR translated_uk = ?";

    private final Mapper<Activity, PreparedStatement> mapRowToDB = (Activity activity, PreparedStatement preparedStatement) -> {
        preparedStatement.setString(1, activity.getName());
        preparedStatement.setLong(2, activity.getTranslationId());
        preparedStatement.setLong(3, activity.getCategoryId());
    };

    private final Mapper<ResultSet, Activity> mapRowFromDB = (ResultSet resultSet, Activity activity) -> {
        activity.setId(resultSet.getLong("id"));
        activity.setName(resultSet.getString("name"));
        activity.setTranslationId(resultSet.getLong("translation_id"));
        activity.setCategoryId(resultSet.getLong("category_id"));
    };

    private final Connection connection;
    private static final Logger LOG = LogManager.getLogger(ActivityDaoImpl.class);

    public ActivityDaoImpl(Connection connection) {
        this.connection = connection;
        LOG.debug("Actual obtained connection for ActivityDao is: {}", this.connection);
    }

    @Override
    public List<Activity> findAllActivitiesByCategory(String categoryName) {
        return null;
    }

    @Override
    public List<Activity> findAllActivitiesByCategory(long categoryId) {
        return null;
    }

    @Override
    public List<Activity> findAllActivitiesByUser(String login) {
        return null;
    }

    @Override
    public List<Activity> findAllActivitiesByUser(long userId) {
        return null;
    }

    @Override
    public boolean removeActivityByUser(String login) {
        return false;
    }

    @Override
    public boolean removeActivityByUser(long id) {
        return false;
    }

    @Override
    public boolean remove(String... name) {
        return false;
    }

    @Override
    public List<Activity> findAll() throws DaoException {
        return null;
    }

    @Override
    public boolean create(Activity activity) throws DaoException {
        return false;
    }

    @Override
    public Activity read(Long aLong) throws DaoException {
        return null;
    }

    @Override
    public boolean update(Activity activity) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Long activityId) throws DaoException {
        LOG.debug("Obtained 'activity id' to delete it from database is: {}", activityId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_DELETE_ACTIVITY_BY_ID)) {
            pst.setLong(1, activityId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to delete an activity from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The activity wasn't deleted by query to database");
            }
            LOG.debug("The {} rows has been removed to delete activity", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to remove activity by id, because {}", e.getMessage());
            throw new DaoException("Cannot delete activity from database " + e.getMessage(), e);
        }
        return result;
    }
}
