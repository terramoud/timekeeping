package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.dao.UserActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.model.entity.UserActivity;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityBean;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserActivityDaoImpl implements UserActivityDao {

    private static final String SQL_CREATE_USERS_ACTIVITIES = "INSERT INTO users_activities VALUES (?, ?, ?)";

    private static final String SQL_GET_USER_ACTIVITY_BY_USER_ID_AND_ACTIVITY_ID =
            "SELECT * FROM users_activities WHERE user_id = ? AND activity_id = ? LIMIT 1";

    private static final String SQL_UPDATE_USER_ACTIVITY =
            "UPDATE users_activities SET user_id = ?, activity_id = ?, is_active = ? " +
                    "WHERE user_id = ? AND activity_id = ?";

    private static final String SQL_DELETE_USER_ACTIVITY =
            "DELETE FROM users_activities WHERE user_id = ? AND activity_id = ?";

    private static final String SQL_FIND_ALL_USERS_ACTIVITIES =
            "SELECT * FROM users_activities " +
                    "INNER JOIN users u on users_activities.user_id = u.id " +
                    "INNER JOIN activities a on users_activities.activity_id = a.id";

    private final Mapper<UserActivity, PreparedStatement> mapRowToDB = (UserActivity userActivity,
                                                                        PreparedStatement preparedStatement) -> {
        preparedStatement.setLong(1, userActivity.getUserId());
        preparedStatement.setLong(2, userActivity.getActivityId());
        preparedStatement.setBoolean(3, userActivity.isActive());
    };

    private final Mapper<ResultSet, UserActivity> mapRowFromDB = (ResultSet resultSet, UserActivity userActivity) -> {
        userActivity.setUserId(resultSet.getLong("user_id"));
        userActivity.setActivityId(resultSet.getLong("activity_id"));
        userActivity.setIsActive(resultSet.getBoolean("is_active"));
    };

    private final Connection connection;
    private static final Logger LOG = LogManager.getLogger(UserActivityDaoImpl.class);

    public UserActivityDaoImpl(Connection connection) {
        this.connection = connection;
        LOG.debug("Actual obtained connection for UserActivityDao is: {}", this.connection);
    }

    @Override
    public List<UserActivity> findAll() throws DaoException {
        List<UserActivity> userActivities = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_USERS_ACTIVITIES);
            LOG.trace("SQL query find all 'user_activities' has already been completed successfully");
            while (rs.next()) {
                UserActivity userActivity = new UserActivity();
                mapRowFromDB.map(rs, userActivity);
                userActivities.add(userActivity);
            }
            LOG.debug("The {} 'user_activities' has been found by query to database", userActivities.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find activities", e, e.getErrorCode());
        }
        return userActivities;
    }

    @Override
    public List<UserActivityBean> findAll(int limit, int offset, String columnName, String sortOrder) throws DaoException {
        List<UserActivityBean> userActivities = new LinkedList<>();
        String sqlParameters = " ORDER BY " + columnName
                .concat(" " + sortOrder)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_USERS_ACTIVITIES + sqlParameters)) {
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query find all 'user_activities' has already been completed successfully");
            while (rs.next()) {
                UserActivityBean userActivityBean = new UserActivityBean();
                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                user.setEmail(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                userActivityBean.setUser(user);
                Activity activity = new Activity();
                activity.setId(rs.getLong("activity_id"));
                activity.setCategoryId(rs.getLong("category_id"));
                activity.setNameEn(rs.getString("a.name_en"));
                activity.setNameUk(rs.getString("a.name_uk"));
                userActivityBean.setActivity(activity);
                userActivities.add(userActivityBean);
            }
            LOG.debug("The {} 'user_activities' has been found by query to database", userActivities.size());
        } catch (SQLException e) {
            LOG.error("Cannot find all 'user_activities' sorted by: {}", sqlParameters);
            throw new DaoException("Cannot find activities with offset and limit. " + e.getMessage(), e);
        }
        return userActivities;
    }

    @Override
    public boolean create(UserActivity userActivity) throws DaoException {
        LOG.debug("Obtained 'user_activity' entity to create it at database is: {}", userActivity);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_CREATE_USERS_ACTIVITIES)) {
            mapRowToDB.map(userActivity, pst);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to create 'user_activity' has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'user_activity' wasn't created by query to database");
            }
            LOG.debug("The {} rows has been added to database to create 'user_activity'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot create 'user_activity' at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public UserActivity read(long userId, long activityId) throws DaoException {
        LOG.debug("Obtained 'user id' and 'activity id' to read 'user_activity' from database are: {}, {}",
                userId, activityId);
        UserActivity userActivity = new UserActivity();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_USER_ACTIVITY_BY_USER_ID_AND_ACTIVITY_ID)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an 'user_activity' from database has already been completed successfully");
            if (!rs.next()) throw new SQLException("There is not pair 'user id' and 'activity id' in db table");
            mapRowFromDB.map(rs, userActivity);
            LOG.debug("The 'user_activity': {} has been found by query to database", userActivity);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read 'user_activity' by 'user id' and 'activity id'", e, e.getErrorCode());
        }
        return userActivity;
    }

    @Override
    public boolean update(UserActivity userActivity) throws DaoException {
        LOG.debug("Obtained 'user_activity' entity to update it at database is: {}", userActivity);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_USER_ACTIVITY)) {
            mapRowToDB.map(userActivity, pst);
            pst.setLong(4, userActivity.getUserId());
            pst.setLong(5, userActivity.getActivityId());
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update an 'user_activity' from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'user_activity' wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to update 'user_activity'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot update 'user_activity' at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean delete(long userId, long activityId) throws DaoException {
        LOG.debug("Obtained 'user id' and 'activity id' to delete it from database are: {}, {}", userId, activityId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_DELETE_USER_ACTIVITY)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to delete an 'user_activity' from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'user_activity' wasn't deleted by query to database");
            }
            LOG.debug("The {} rows has been removed to delete 'user_activity'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot delete 'user_activity' from database", e, e.getErrorCode());
        }
        return result;
    }
}
