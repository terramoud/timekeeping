package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.entity.Activity;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ActivityDaoImpl implements ActivityDao {
    private static final String SQL_FIND_ALL_ACTIVITIES = "SELECT * FROM activities";

    private static final String SQL_CREATE_ACTIVITY = "INSERT INTO activities VALUES (DEFAULT, ?, ?, ?)";

    private static final String SQL_GET_ACTIVITY_BY_ID = "SELECT * FROM activities WHERE id = ?";

    private static final String SQL_UPDATE_ACTIVITY_BY_ID =
            "UPDATE activities SET name = ?, translation_id = ?, category_id = ? WHERE id = ?";

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
    public List<Activity> findAll() throws DaoException {
        List<Activity> activitiesList = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_ACTIVITIES);
            LOG.trace("SQL query find all activities to database has already been completed successfully");
            while (rs.next()) {
                Activity activity = new Activity();
                mapRowFromDB.map(rs, activity);
                activitiesList.add(activity);
            }
            LOG.debug("The {} activities has been found by query to database", activitiesList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to find all activities, because {}", e.getMessage());
            throw new DaoException("Cannot find activities " + e.getMessage(), e);
        }
        if (activitiesList.isEmpty()) {
            LOG.warn("Empty list activities has been returned by findAll() method");
        }
        return activitiesList;
    }

    @Override
    public boolean create(Activity activity) throws DaoException {
        LOG.debug("Obtained activity entity to create it at database is: {}", activity);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_CREATE_ACTIVITY, Statement.RETURN_GENERATED_KEYS)) {
            mapRowToDB.map(activity, pst);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to create activity has already been completed successfully");
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                activity.setId(rs.getLong(1));
                LOG.debug("The source activity entity has synchronized 'id' with database and now represent the: {}", activity);
            }
            if (rowCount == 0) {
                result = false;
                LOG.warn("The activity wasn't created by query to database");
            }
            LOG.debug("The {} rows has been added to database to create user", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown by database to create activity, because {}", e.getMessage());
            throw new DaoException("Cannot create usactivityt database " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Activity read(Long activityId) throws DaoException {
        LOG.debug("Obtained 'activity id' to read it from database is: {}", activityId);
        Activity activity = new Activity();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_ACTIVITY_BY_ID)) {
            pst.setLong(1, activityId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an activity from database has already been completed successfully");
            if (!rs.next()) throw new SQLException("There is not id: '" + activityId + "' in db table");
            mapRowFromDB.map(rs, activity);
            LOG.debug("The activity: {} has been found by query to database", activity);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to get activity by id, because {}", e.getMessage());
            throw new DaoException("Cannot read activity by id " + e.getMessage(), e);
        }
        return activity;
    }

    @Override
    public boolean update(Activity activity) throws DaoException {
        LOG.debug("Obtained activity entity to update it at database is: {}", activity);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_ACTIVITY_BY_ID)) {
            mapRowToDB.map(activity, pst);
            pst.setLong(4, activity.getId());
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update an activity from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The activity wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to update activity", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to update activity, because {}", e.getMessage());
            throw new DaoException("Cannot update activity at database " + e.getMessage(), e);
        }
        return result;
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
