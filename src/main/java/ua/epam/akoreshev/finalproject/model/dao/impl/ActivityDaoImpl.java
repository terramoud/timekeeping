package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ActivityDaoImpl implements ActivityDao {

    private static final String SQL_CREATE_ACTIVITY = "INSERT INTO activities VALUES (DEFAULT, ?, ?, ?)";

    private static final String SQL_GET_ACTIVITY_BY_ID = "SELECT * FROM activities WHERE id = ?";

    private static final String SQL_UPDATE_ACTIVITY_BY_ID =
            "UPDATE activities SET name_en = ?, name_uk = ?, category_id = ? WHERE id = ?";

    private static final String SQL_DELETE_ACTIVITY_BY_ID = "DELETE FROM activities WHERE id = ?";

    private static final String SQL_FIND_ALL_ACTIVITIES = "SELECT * FROM activities";

    private static final String SQL_FIND_ALL_ACTIVITIES_CATEGORIES =
            "SELECT * FROM activities INNER JOIN categories c ON activities.category_id = c.id";

    private static final String SQL_FIND_ALL_CATEGORIES = "SELECT * FROM categories";

    private static final String SQL_FIND_ALL_ACTIVITIES_BY_CATEGORY_ID =
            "SELECT * FROM activities WHERE category_id = ?";

    private static final String SQL_FIND_ALL_ACTIVITIES_BY_USER_ID =
            "SELECT * FROM activities " +
                    "INNER JOIN users_activities ON activities.id = activity_id " +
                    "WHERE user_id = ? ORDER BY is_active DESC";

    private static final String SQL_GET_NUMBER_ACTIVITIES =
            "SELECT COUNT(*) AS numRows FROM activities";

    public static final String QUERY_COMPLETED_SUCCESSFULLY =
            "SQL query find all activities at database has already been completed successfully";
    public static final String ACTIVITIES_HAS_BEEN_FOUND = "The {} activities has been found by query to database";

    private final Mapper<Activity, PreparedStatement> mapRowToDB = (Activity activity,
                                                                    PreparedStatement preparedStatement) -> {
        preparedStatement.setString(1, activity.getNameEn());
        preparedStatement.setString(2, activity.getNameUk());
        preparedStatement.setLong(3, activity.getCategoryId());
    };

    private final Mapper<ResultSet, Activity> mapRowFromDB = (ResultSet resultSet, Activity activity) -> {
        activity.setId(resultSet.getLong("id"));
        activity.setNameEn(resultSet.getString("name_en"));
        activity.setNameUk(resultSet.getString("name_uk"));
        activity.setCategoryId(resultSet.getLong("category_id"));
    };

    private final Connection connection;
    private static final Logger LOG = LogManager.getLogger(ActivityDaoImpl.class);

    public ActivityDaoImpl(Connection connection) {
        this.connection = connection;
        LOG.debug("Actual obtained connection for ActivityDao is: {}", this.connection);
    }

    @Override
    public List<Activity> findAllActivitiesByCategory(long categoryId) throws DaoException {
        LOG.debug("Obtained 'category id' to find activity is: {}", categoryId);
        List<Activity> activitiesList = new LinkedList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_ACTIVITIES_BY_CATEGORY_ID)) {
            pst.setLong(1, categoryId);
            ResultSet rs = pst.executeQuery();
            LOG.trace(QUERY_COMPLETED_SUCCESSFULLY);
            while (rs.next()) {
                Activity activity = new Activity();
                mapRowFromDB.map(rs, activity);
                activitiesList.add(activity);
            }
            LOG.debug(ACTIVITIES_HAS_BEEN_FOUND, activitiesList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find all activities by category id", e, e.getErrorCode());
        }
        return activitiesList;
    }

    @Override
    public List<Activity> findAllActivitiesByUser(long userId) throws DaoException {
        LOG.debug("Obtained 'user id' to find activity is: {}", userId);
        List<Activity> activitiesList = new LinkedList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_ACTIVITIES_BY_USER_ID)) {
            pst.setLong(1, userId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                Activity activity = new Activity();
                mapRowFromDB.map(rs, activity);
                activitiesList.add(activity);
            }
            LOG.debug(ACTIVITIES_HAS_BEEN_FOUND, activitiesList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find all activities by user id", e, e.getErrorCode());
        }
        return activitiesList;
    }

    @Override
    public List<Category> findAllCategories() throws DaoException {
        List<Category> categoryList = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_CATEGORIES);
            LOG.trace("SQL query find all categories to database has already been completed successfully");
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setNameEn(rs.getString("name_en"));
                category.setNameUk(rs.getString("name_uk"));
                categoryList.add(category);
            }
            LOG.debug("The {} categories has been found by query to database", categoryList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find all categories", e, e.getErrorCode());
        }
        return categoryList;
    }

    @Override
    public int getNumberActivities() throws DaoException {
        int result = 0;
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_GET_NUMBER_ACTIVITIES);
            LOG.trace("SQL query find all 'activities' to database has already been completed successfully");
            if (rs.next()) {
                result = rs.getInt("numRows");
            }
            LOG.debug("The {} rows has been found by query to database", result);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot count activities", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public List<ActivityCategoryBean> findAllActivities(int limit, int offset, String columnName, String sortOrder) throws DaoException {
        LOG.debug("Obtained columnName is: {}", columnName);
        LOG.debug("Obtained sort order is: {}", sortOrder);
        List<ActivityCategoryBean> activityCategoryBeans = new LinkedList<>();
        String sqlParameters = " ORDER BY " + columnName
                .concat(" " + sortOrder)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        try (PreparedStatement pst = connection.prepareStatement(
                SQL_FIND_ALL_ACTIVITIES_CATEGORIES + sqlParameters)) {
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query find all activities to database has already been completed successfully");
            while (rs.next()) {
                ActivityCategoryBean activityCategoryBean = new ActivityCategoryBean();
                Activity activity = new Activity();
                mapRowFromDB.map(rs, activity);
                activityCategoryBean.setActivity(activity);
                Category category = new Category();
                category.setId(rs.getLong("category_id"));
                category.setNameEn(rs.getString("c.name_en"));
                category.setNameUk(rs.getString("c.name_uk"));
                activityCategoryBean.setCategory(category);
                activityCategoryBeans.add(activityCategoryBean);
            }
            LOG.debug("The {} categories has been found by query to database", activityCategoryBeans.size());
        } catch (SQLException e) {
            LOG.error("Cannot find activities sorted by: {}", sqlParameters);
            throw new DaoException("Cannot find sorted activities", e, e.getErrorCode());
        }
        return activityCategoryBeans;
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
            LOG.debug(ACTIVITIES_HAS_BEEN_FOUND, activitiesList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find any activities", e, e.getErrorCode());
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
            LOG.debug("The {} rows has been added to database to create activity", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot create activity", e, e.getErrorCode());
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
            if (!rs.next()) return null;
            mapRowFromDB.map(rs, activity);
            LOG.debug("The activity: {} has been found by query to database", activity);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read activity by id", e, e.getErrorCode());
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
            LOG.error(e);
            throw new DaoException("Cannot update activity at database", e, e.getErrorCode());
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
            LOG.error(e);
            throw new DaoException("Cannot delete activity from database", e, e.getErrorCode());
        }
        return result;
    }
}
