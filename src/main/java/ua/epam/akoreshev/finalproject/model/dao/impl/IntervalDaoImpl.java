package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.DaoFactory;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.dao.UserActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.model.entity.Time;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class IntervalDaoImpl implements IntervalDao {

    private static final String SQL_CREATE_INTERVAL = "INSERT INTO intervals VALUES (DEFAULT, ?, ?, ?, ?)";

    private static final String SQL_GET_INTERVAL_BY_ID = "SELECT * FROM intervals WHERE id = ?";

    private static final String SQL_UPDATE_INTERVAL_BY_ID =
            "UPDATE intervals SET start = ?, finish = ?, user_id = ?, activity_id = ? WHERE id = ?";

    private static final String SQL_SET_START_TIME_FOR_INTERVAL =
            "UPDATE intervals AS i INNER JOIN users_activities AS ua " +
                    "ON i.user_id = ua.user_id AND i.activity_id = ua.activity_id " +
                    "SET start = ? " +
                    "WHERE i.user_id = ? AND i.activity_id = ? AND i.start IS NULL " +
                    "AND i.finish IS NULL AND ua.is_active = TRUE";

    public static final String SET_ACTIVE_STATUS_FOR_USER_ACTIVITY =
            "UPDATE users_activities SET is_active = TRUE WHERE user_id = ? AND activity_id = ?";

    private static final String SQL_SET_FINISH_TIME_FOR_INTERVAL =
            "UPDATE intervals AS i INNER JOIN users_activities AS ua " +
                    "ON i.user_id = ua.user_id AND i.activity_id = ua.activity_id " +
                    "SET i.finish = ? " +
                    "WHERE i.user_id = ? AND i.activity_id = ? AND i.finish IS NULL " +
                    "AND i.start IS NOT NULL AND ua.is_active = TRUE";

    private static final String SQL_DELETE_INTERVAL_BY_ID = "DELETE FROM intervals WHERE id = ?";

    private static final String SQL_FIND_ALL_INTERVALS = "SELECT * FROM intervals";

    private static final String SQL_GET_NUMBER_INTERVALS = "SELECT COUNT(*) AS numRows FROM (" +
            "SELECT COUNT(*) " +
            "FROM users_activities " +
            "INNER JOIN intervals i ON users_activities.activity_id = i.activity_id " +
            "AND users_activities.user_id = i.user_id " +
            "WHERE start IS NOT NULL AND finish IS NOT NULL " +
            "GROUP BY i.user_id, i.activity_id" +
            ") AS userActivitiesIntervals";

    private static final String SQL_GET_INTERVAL_BY_USER_ACTIVITY =
            "SELECT * FROM intervals WHERE user_id = ? AND activity_id = ? ORDER BY id DESC LIMIT 1";

    private static final String FIND_USER_THAT_HAS_IS_ACTIVE_STATUS =
            "SELECT * FROM users_activities WHERE user_id = ? AND is_active = TRUE";

    private static final String FIND_SUMMARY_SPENT_TIME_AND_ATTEMPTS_FOR_ALL_USER_ACTIVITIES =
            "SELECT * " +
            "FROM " +
                    "(SELECT user_id, activity_id, COUNT(start) AS attempts " +
                        "FROM intervals " +
                        "WHERE start IS NOT NULL " +
                        "GROUP BY user_id, activity_id) AS numIntervals, " +
                    "(SELECT user_id, activity_id, " +
                            "SUM(TIMESTAMPDIFF(SECOND, intervals.start, intervals.finish)) AS summary " +
                        "FROM intervals " +
                        "WHERE start IS NOT NULL AND finish IS NOT NULL " +
                        "GROUP BY user_id, activity_id) AS totalIntervals, " +
                    "users_activities " +
            "INNER JOIN activities a ON users_activities.activity_id = a.id " +
            "INNER JOIN users u ON users_activities.user_id = u.id " +
            "WHERE numIntervals.user_id = u.id AND numIntervals.activity_id = a.id AND " +
                    "totalIntervals.user_id = u.id AND totalIntervals.activity_id = a.id AND u.role_id != ? " +
            "GROUP BY u.id, a.id ";

    private final Mapper<Interval, PreparedStatement> mapEntityToDB = (Interval interval,
                                                                       PreparedStatement preparedStatement) -> {
        LocalDateTime startTime = interval.getStart();
        LocalDateTime finishTime = interval.getFinish();
        preparedStatement.setTimestamp(1, (startTime == null) ? null : Timestamp.valueOf(startTime));
        preparedStatement.setTimestamp(2, (finishTime == null) ? null : Timestamp.valueOf(finishTime));
        preparedStatement.setLong(3, interval.getUserId());
        preparedStatement.setLong(4, interval.getActivityId());
    };

    private final Mapper<ResultSet, Interval> mapEntityFromDB = (ResultSet resultSet, Interval interval) -> {
        Timestamp startTime = resultSet.getTimestamp("start");
        Timestamp finishTime = resultSet.getTimestamp("finish");
        interval.setId(resultSet.getLong("id"));
        interval.setStart((startTime == null) ? null : startTime.toLocalDateTime());
        interval.setFinish((finishTime == null) ? null : finishTime.toLocalDateTime());
        interval.setUserId(resultSet.getLong("user_id"));
        interval.setActivityId(resultSet.getLong("activity_id"));
    };

    private final Connection connection;
    private static final Logger LOG = LogManager.getLogger(IntervalDaoImpl.class);

    public IntervalDaoImpl(Connection connection) {
        this.connection = connection;
        LOG.debug("Actual obtained connection for IntervalDao is: {}", this.connection);
    }

    @Override
    public boolean setStartTimeForUserActivity(long userId,
                                               long activityId,
                                               LocalDateTime startTime) throws DaoException {
        LOG.debug("Obtained 'user id', 'activity id' and 'time' to set interval's start time are: {}, {}, {}",
                userId, activityId, startTime);
        boolean result = true;
        int changedRowSetIsActive = 0;
        int changedRowSetTime = 0;
        try (PreparedStatement psts = connection.prepareStatement(FIND_USER_THAT_HAS_IS_ACTIVE_STATUS)) {
            connection.setAutoCommit(false);
            psts.setLong(1, userId);
            ResultSet rs = psts.executeQuery();
            if (rs.next()) {
                result = false;
            }
            if (result) {
                try (PreparedStatement ps = connection.prepareStatement(SET_ACTIVE_STATUS_FOR_USER_ACTIVITY)) {
                    ps.setLong(1, userId);
                    ps.setLong(2, activityId);
                    changedRowSetIsActive = ps.executeUpdate();
                }
                try (PreparedStatement pst = connection.prepareStatement(SQL_SET_START_TIME_FOR_INTERVAL)) {
                    pst.setTimestamp(1, Timestamp.valueOf(startTime));
                    pst.setLong(2, userId);
                    pst.setLong(3, activityId);
                    changedRowSetTime = pst.executeUpdate();
                }
                LOG.trace("SQL query to update a start time of 'interval' " +
                        "from database has been completed successfully");
            }
            if (changedRowSetIsActive == 0 || changedRowSetTime == 0) {
                result = false;
                LOG.warn("The start time for 'interval' cannot set by query to database");
                rollback(connection);
            }
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error(e);
            throw new DaoException("Cannot set start time for 'interval'", e, e.getErrorCode());
        } finally {
            setAutoCommit(connection);
        }
        return result;
    }

    @Override
    public boolean setFinishTimeForUserActivity(long userId,
                                                long activityId,
                                                LocalDateTime finishTime) throws DaoException {
        LOG.debug("Obtained 'user id', 'activity id' and 'time' to set interval's finish time are: {}, {}, {}",
                userId, activityId, finishTime);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_SET_FINISH_TIME_FOR_INTERVAL)) {
            connection.setAutoCommit(false);
            pst.setTimestamp(1, Timestamp.valueOf(finishTime));
            pst.setLong(2, userId);
            pst.setLong(3, activityId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update a finish time of 'interval' " +
                    "from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
            }
            if (result) {
                DaoFactory daoFactory = DaoFactory.getDaoFactory();
                UserActivityDao userActivityDao = daoFactory.getUserActivityDao(connection);
                UserActivity userActivity = new UserActivity(userId, activityId, false);
                result = userActivityDao.update(userActivity);
            }
            if (result) {
                result = this.create(new Interval(userId, activityId));
            }
            LOG.debug("The {} rows has been changed to set finish time of 'interval'", rowCount);
            if (!result)
                rollback(connection);
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error(e);
            throw new DaoException("Cannot set finish time for 'interval'", e, e.getErrorCode());
        } finally {
            setAutoCommit(connection);
        }
        return result;
    }

    @Override
    public Interval readIntervalByUserActivity(long userId, long activityId) throws DaoException {
        LOG.debug("Obtained 'user id' and 'activity id' to get 'interval' are: {}, {}", userId, activityId);
        Interval interval = new Interval();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_INTERVAL_BY_USER_ACTIVITY)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an 'interval' from database has already been completed successfully");
            if (!rs.next()) return null;
            mapEntityFromDB.map(rs, interval);
            LOG.debug("The 'interval': {} has been found by query to database", interval);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot get 'interval' by user_activity", e, e.getErrorCode());
        }
        return interval;
    }

    @Override
    public int getNumberIntervals() throws DaoException {
        int result = 0;
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_GET_NUMBER_INTERVALS);
            LOG.trace("SQL query find all 'intervals' to database has already been completed successfully");
            if (rs.next()) {
                result = rs.getInt("numRows");
            }
            LOG.debug("The {} rows has been found by query to database", result);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot count 'intervals'", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public List<UserStatistic> findUserStatistics(int limit,
                                                  int offset,
                                                  String columnName,
                                                  String sortOrder) throws DaoException {
        List<UserStatistic> statistics = new LinkedList<>();
        String sqlParameters = " ORDER BY " + columnName
                .concat(" " + sortOrder)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        try (PreparedStatement pst = connection.prepareStatement(
                FIND_SUMMARY_SPENT_TIME_AND_ATTEMPTS_FOR_ALL_USER_ACTIVITIES + sqlParameters)) {
            pst.setLong(1, Role.getRoleId(Role.ADMIN));
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("u.id"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                user.setEmail(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                Activity activity = new Activity();
                activity.setId(rs.getLong("a.id"));
                activity.setCategoryId(rs.getLong("category_id"));
                activity.setNameEn(rs.getString("a.name_en"));
                activity.setNameUk(rs.getString("a.name_uk"));
                UserStatistic userStatistic = new UserStatistic();
                userStatistic.setUser(user);
                userStatistic.setActivity(activity);
                userStatistic.setTotal(new Time(rs.getLong("summary")));
                userStatistic.setAttempts(rs.getLong("attempts"));
                statistics.add(userStatistic);
            }
            LOG.debug("Obtained statistic are {}", statistics);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find user timekeeping statistic", e, e.getErrorCode());
        }
        return statistics;
    }

    @Override
    public List<Interval> findAll() throws DaoException {
        List<Interval> intervalList = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_INTERVALS);
            LOG.trace("SQL query find all 'intervals' to database has already been completed successfully");
            while (rs.next()) {
                Interval interval = new Interval();
                mapEntityFromDB.map(rs, interval);
                intervalList.add(interval);
            }
            LOG.debug("The {} 'intervals' has been found by query to database", intervalList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find any 'intervals'", e, e.getErrorCode());
        }
        return intervalList;
    }

    @Override
    public boolean create(Interval interval) throws DaoException {
        LOG.debug("Obtained 'interval' entity to create it at database is: {}", interval);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(
                SQL_CREATE_INTERVAL, Statement.RETURN_GENERATED_KEYS)) {
            mapEntityToDB.map(interval, pst);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to create 'interval' has already been completed successfully");
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                interval.setId(rs.getLong(1));
                LOG.debug("The source 'interval' entity has synchronized 'id' " +
                        "with database and now represent the: {}", interval);
            }
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'interval' wasn't created by query to database");
            }
            LOG.debug("The {} rows has been added to database to create 'interval'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot create 'interval' at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public Interval read(Long intervalId) throws DaoException {
        LOG.debug("Obtained 'interval id' to read it from database is: {}", intervalId);
        Interval interval = new Interval();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_INTERVAL_BY_ID)) {
            pst.setLong(1, intervalId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an 'interval' from database has already been completed successfully");
            if (!rs.next()) throw new SQLException("There is not id: '" + intervalId + "' in db table");
            mapEntityFromDB.map(rs, interval);
            LOG.debug("The 'interval': {} has been found by query to database", interval);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read 'interval' by id", e, e.getErrorCode());
        }
        return interval;
    }

    @Override
    public boolean update(Interval interval) throws DaoException {
        LOG.debug("Obtained 'interval' entity to update it at database is: {}", interval);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_INTERVAL_BY_ID)) {
            mapEntityToDB.map(interval, pst);
            pst.setLong(5, interval.getId());
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update an 'interval' from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'interval' wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to update 'interval'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot update 'interval'", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean delete(Long intervalId) throws DaoException {
        LOG.debug("Obtained 'interval id' to delete it from database is: {}", intervalId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_DELETE_INTERVAL_BY_ID)) {
            pst.setLong(1, intervalId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to delete an 'interval' from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'interval' wasn't deleted by query to database");
            }
            LOG.debug("The {} rows has been removed to delete 'interval'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot delete 'interval' from database", e, e.getErrorCode());
        }
        return result;
    }
}
