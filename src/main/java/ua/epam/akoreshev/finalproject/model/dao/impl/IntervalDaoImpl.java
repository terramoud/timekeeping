package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.entity.Interval;

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
            "UPDATE intervals SET start = ? WHERE user_id = ? AND activity_id = ? ORDER BY id DESC";

    private static final String SQL_SET_FINISH_TIME_FOR_INTERVAL =
            "UPDATE intervals SET finish = ? WHERE user_id = ? AND activity_id = ? ORDER BY id DESC";

    private static final String SQL_DELETE_INTERVAL_BY_ID = "DELETE FROM intervals WHERE id = ?";

    private static final String SQL_FIND_ALL_INTERVALS = "SELECT * FROM intervals";

    private static final String SQL_FIND_ALL_INTERVALS_BY_USER_ACTIVITY =
            "SELECT * FROM intervals WHERE user_id = ? AND activity_id = ?";

    private final Mapper<Interval, PreparedStatement> mapEntityToDB = (Interval interval, PreparedStatement preparedStatement) -> {
        preparedStatement.setTimestamp(1, Timestamp.valueOf(interval.getStart()));
        preparedStatement.setTimestamp(2, Timestamp.valueOf(interval.getFinish()));
        preparedStatement.setLong(3, interval.getUserId());
        preparedStatement.setLong(4, interval.getActivityId());
    };

    private final Mapper<ResultSet, Interval> mapEntityFromDB = (ResultSet resultSet, Interval interval) -> {
        interval.setId(resultSet.getLong("id"));
        interval.setStart(resultSet.getTimestamp("start").toLocalDateTime());
        interval.setFinish(resultSet.getTimestamp("finish").toLocalDateTime());
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
    public boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws DaoException {
        LOG.debug("Obtained 'user id', 'activity id' and 'time' to set interval's start time are: {}, {}, {}",
                userId, activityId, startTime);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_SET_START_TIME_FOR_INTERVAL)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            pst.setTimestamp(3, Timestamp.valueOf(startTime));
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update a start time of 'interval' from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The start time of 'interval' wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to set start time of 'interval'", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to set start time of 'interval', because {}", e.getMessage());
            throw new DaoException("Cannot set start time of 'interval'. " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime finishTime) throws DaoException {
        LOG.debug("Obtained 'user id', 'activity id' and 'time' to set interval's finish time are: {}, {}, {}",
                userId, activityId, finishTime);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_SET_FINISH_TIME_FOR_INTERVAL)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            pst.setTimestamp(3, Timestamp.valueOf(finishTime));
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update a finish time of 'interval' from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The finish time of 'interval' wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to set finish time of 'interval'", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to set finish time of 'interval', because {}", e.getMessage());
            throw new DaoException("Cannot set finish time of 'interval'. " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Interval> findAllIntervalsByUserActivity(long userId, long activityId) throws DaoException {
        LOG.debug("Obtained 'user id' and 'activity id' to find all 'intervals' are: {}, {}", userId, activityId);
        List<Interval> intervalList = new LinkedList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_INTERVALS_BY_USER_ACTIVITY)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                Interval interval = new Interval();
                mapEntityFromDB.map(rs, interval);
                intervalList.add(interval);
            }
            LOG.debug("The {} 'intervals' has been found by query to database", intervalList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown to find all 'intervals' by user_activity , because {}", e.getMessage());
            throw new DaoException("Cannot find 'intervals' by user_activity. " + e.getMessage(), e);
        }
        if (intervalList.isEmpty()) {
            LOG.warn("Empty list 'intervals' has been returned by find all 'intervals' by user_activity");
        }
        return intervalList;
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
            LOG.error("DAO exception has been thrown to find all 'intervals', because {}", e.getMessage());
            throw new DaoException("Cannot find 'intervals'. " + e.getMessage(), e);
        }
        if (intervalList.isEmpty()) {
            LOG.warn("Empty list 'intervals' has been returned by findAll() method");
        }
        return intervalList;
    }

    @Override
    public boolean create(Interval interval) throws DaoException {
        LOG.debug("Obtained 'interval' entity to create it at database is: {}", interval);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_CREATE_INTERVAL, Statement.RETURN_GENERATED_KEYS)) {
            mapEntityToDB.map(interval, pst);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to create 'interval' has already been completed successfully");
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                interval.setId(rs.getLong(1));
                LOG.debug("The source 'interval' entity has synchronized 'id' with database and now represent the: {}", interval);
            }
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'interval' wasn't created by query to database");
            }
            LOG.debug("The {} rows has been added to database to create 'interval'", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown by database to create 'interval', because {}", e.getMessage());
            throw new DaoException("Cannot create 'interval' database. " + e.getMessage(), e);
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
            LOG.error("DAO exception has been thrown to get 'interval' by id, because {}", e.getMessage());
            throw new DaoException("Cannot read 'interval' by id. " + e.getMessage(), e);
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
            LOG.error("DAO exception has been thrown to update 'interval', because {}", e.getMessage());
            throw new DaoException("Cannot update 'interval' at database. " + e.getMessage(), e);
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
            LOG.error("DAO exception has been thrown to remove 'interval' by id, because {}", e.getMessage());
            throw new DaoException("Cannot delete 'interval' from database. " + e.getMessage(), e);
        }
        return result;
    }
}
