package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.*;
import ua.epam.akoreshev.finalproject.model.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestDaoImpl implements RequestDao {
    private static final String SQL_GET_REQUEST_BY_ID = "SELECT * FROM requests WHERE id = ?";

    private static final String SQL_GET_REQUEST_BY_USER_ACTIVITY_TYPE_STATUS_IDS =
            "SELECT * FROM requests WHERE user_id = ? AND activity_id = ? AND type_id = ? AND status_id = ?";

    private static final String SQL_DELETE_REQUEST_BY_ID = "DELETE FROM requests WHERE id=?";

    private static final String SQL_CREATE_REQUEST =
            "INSERT INTO requests VALUES (DEFAULT, ?, ?, ?, ?)";

    private static final String SQL_FIND_ALL_REQUESTS = "SELECT * FROM requests";

    private static final String SQL_UPDATE_REQUEST_BY_ID =
            "UPDATE requests SET user_id = ?, activity_id = ?, type_id = ?, status_id = ? WHERE id = ?";

    private static final String SQL_UPDATE_REQUEST_BY_STATUS_ID =
            "UPDATE requests SET status_id = ? WHERE id = ?";

    public static final String FIND_ALL_REQUESTS_FROM_USERS_BY_STATUSES =
            "SELECT * FROM requests " +
                    "INNER JOIN users u ON requests.user_id = u.id " +
                    "INNER JOIN activities a ON requests.activity_id = a.id " +
                    "INNER JOIN statuses s ON requests.status_id = s.id " +
                    "INNER JOIN types t ON requests.type_id = t.id " +
                    "WHERE status_id IN ";

    private static final String GET_NUM_REQUESTS_BY_STATUSES =
            "SELECT COUNT(*) AS numRows FROM requests WHERE status_id IN";

    private static final String SQL_SET_FINISH_TIME_BY_USER_AND_ACTIVITY_IDS =
            "UPDATE intervals SET finish = ? " +
                    "WHERE user_id = ? AND activity_id = ? AND start IS NOT NULL AND finish IS NULL";

    public static final String REMOVE_WAS_NOT_STARTED_INTERVAL =
            "DELETE FROM intervals WHERE user_id = ? AND activity_id = ? AND start IS NULL AND finish IS NULL";

    private final Mapper<Request, PreparedStatement> mapRowToDB = (Request request,
                                                                   PreparedStatement preparedStatement) -> {
        preparedStatement.setLong(1, request.getUserId());
        preparedStatement.setLong(2, request.getActivityId());
        preparedStatement.setLong(3, request.getTypeId());
        preparedStatement.setLong(4, request.getStatusId());
    };

    private final Mapper<ResultSet, Request> mapRowFromDB = (ResultSet resultSet, Request request) -> {
        request.setId(resultSet.getLong("id"));
        request.setUserId(resultSet.getLong("user_id"));
        request.setActivityId(resultSet.getLong("activity_id"));
        request.setTypeId(resultSet.getLong("type_id"));
        request.setStatusId(resultSet.getLong("status_id"));
    };

    private final Connection connection;
    private static final Logger LOG = LogManager.getLogger(RequestDaoImpl.class);

    public RequestDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Request> findAll() throws DaoException {
        List<Request> requestsList = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_REQUESTS);
            LOG.trace("SQL query find all 'requests for activities from user' to database has " +
                    "already been completed successfully");
            while (rs.next()) {
                Request request = new Request();
                mapRowFromDB.map(rs, request);
                requestsList.add(request);
            }
            LOG.debug("The {} 'requests for activities from user' has already been found" +
                    " by query to database", requestsList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find 'requests for activities from user'", e, e.getErrorCode());
        }
        return requestsList;
    }

    @Override
    public boolean updateRequestStatus(long requestId, long statusId) throws DaoException {
        LOG.debug("Obtained 'request id' and 'status id' to read it from database are: {} and {}",
                requestId, statusId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_REQUEST_BY_STATUS_ID)) {
            pst.setLong(1, statusId);
            pst.setLong(2, requestId);
            LOG.trace("SQL query to update the 'request status by id' at " +
                    "database has already been completed successfully");
            int rowCount = pst.executeUpdate();
            LOG.debug("The {} rows has already been changed to update " +
                    "'request for activities from user'", rowCount);
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request status' wasn't updated by query to database");
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot update request for " +
                    "activities from user at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean create(Request request) throws DaoException {
        LOG.debug("Obtained request entity to create it at database is: {}", request);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(
                SQL_CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS)) {
            mapRowToDB.map(request, pst);
            int rowCount = pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                request.setId(rs.getLong(1));
                LOG.debug("The source request entity has synchronized 'id' " +
                        "with created one at database and now represent the: {}", request);
            }
            LOG.trace("SQL query to create request has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request for activities from user' wasn't created by query to database");
            }
            LOG.debug("The {} rows has already been added to database to create request", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot create 'request for activities from user' at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public Request read(long userId, long activityId, long typeId, long statusId) throws DaoException {
        LOG.debug("Obtained user id, activity id, type id and status id are: {}, {}, {}, {}",
                userId, activityId, typeId, statusId);
        Request request = new Request();
        try (PreparedStatement pst = connection.prepareStatement(
                SQL_GET_REQUEST_BY_USER_ACTIVITY_TYPE_STATUS_IDS)) {
            pst.setLong(1, userId);
            pst.setLong(2, activityId);
            pst.setLong(3, typeId);
            pst.setLong(4, statusId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read the request from database has already been completed successfully");
            if (!rs.next()) return null;
            mapRowFromDB.map(rs, request);
            LOG.debug("The request: {} has already been found by query to database", request);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read 'request for activities from user'", e, e.getErrorCode());
        }
        return request;
    }

    @Override
    public List<UserActivityRequest> findAllRequestsByStatuses(int limit,
                                                               int offset,
                                                               String columnName,
                                                               String sortOrder,
                                                               int[] statuses) throws DaoException {
        String arrStatuses = Arrays.toString(statuses);
        LOG.debug("Obtained limit, offset and statuses to find them at database are: {}, {} and {}",
                limit, offset, arrStatuses);
        List<UserActivityRequest> requestsList = new LinkedList<>();
        String sqlParameters = Arrays.stream(statuses)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"))
                .concat(" ORDER BY " + columnName)
                .concat(" " + sortOrder)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        LOG.debug("SQL parameters are: {}", sqlParameters);
        try (PreparedStatement pst = connection.prepareStatement(
                FIND_ALL_REQUESTS_FROM_USERS_BY_STATUSES + sqlParameters)) {
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                UserActivityRequest userActivityRequest = new UserActivityRequest();
                userActivityRequest.setRequestId(rs.getLong("requests.id"));
                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                user.setEmail(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                userActivityRequest.setUser(user);
                Activity activity = new Activity();
                activity.setId(rs.getLong("activity_id"));
                activity.setCategoryId(rs.getLong("category_id"));
                activity.setNameEn(rs.getString("a.name_en"));
                activity.setNameUk(rs.getString("a.name_uk"));
                userActivityRequest.setActivity(activity);
                Type type = new Type();
                type.setId(rs.getLong("type_id"));
                type.setNameEn(rs.getString("t.name_en"));
                type.setNameUk(rs.getString("t.name_uk"));
                userActivityRequest.setType(type);
                Status status = new Status();
                status.setId(rs.getLong("status_id"));
                status.setNameEn(rs.getString("s.name_en"));
                status.setNameUk(rs.getString("s.name_uk"));
                userActivityRequest.setStatus(status);
                requestsList.add(userActivityRequest);
            }
            LOG.debug("The {} 'requests for activities from user' has already " +
                    "been found by query to database", requestsList.size());
        } catch (SQLException e) {
            LOG.error("Cannot find requests for activities from user by statuses sorted by: {}", sqlParameters);
            throw new DaoException("Cannot find requests for activities from user by statuses", e, e.getErrorCode());
        }
        return requestsList;
    }

    @Override
    public int getCountRowsByStatuses(int... statuses) throws DaoException {
        String arrStatuses = Arrays.toString(statuses);
        LOG.debug("Obtained statuses to find them at database are: {}", arrStatuses);
        int result = 0;
        String sqlParameters = Arrays.stream(statuses)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        try (PreparedStatement pst = connection.prepareStatement(
                GET_NUM_REQUESTS_BY_STATUSES + sqlParameters)) {
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            if (rs.next()) {
                result = rs.getInt("numRows");
            }
            LOG.debug("The {} 'requests found by statuses", result);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find requests for activities from user by statuses", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean approveAddActivityRequest(long requestId,
                                             int statusId,
                                             long userId,
                                             long activityId) throws DaoException {
        boolean result;
        try {
            connection.setAutoCommit(false);
            UserActivityDao userActivityDao = new UserActivityDaoImpl(connection);
            result = userActivityDao.create(new UserActivity(userId, activityId));
            if (result) {
                IntervalDao intervalDao = new IntervalDaoImpl(connection);
                result = intervalDao.create(new Interval(userId, activityId));
            }
            if (result) {
                result = this.updateRequestStatus(requestId, statusId);
            }
            if (!result)
                rollback(connection);
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error(e);
            throw new DaoException("Cannot approve adding new activity", e, e.getErrorCode());
        } finally {
            setAutoCommit(connection);
        }
        return result;
    }

    @Override
    public boolean approveRemovingActivityRequest(long requestId,
                                                  int statusId,
                                                  long userId,
                                                  long activityId) throws DaoException {
        boolean result;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement pst = connection.prepareStatement(
                    SQL_SET_FINISH_TIME_BY_USER_AND_ACTIVITY_IDS)) {
                pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                pst.setLong(2, userId);
                pst.setLong(3, activityId);
                result = pst.executeUpdate() != 0;
            }
            if (!result) {
                LOG.warn("Cannot set finish time, because start time wasn't set");
                try (PreparedStatement pst = connection.prepareStatement(REMOVE_WAS_NOT_STARTED_INTERVAL)) {
                    pst.setLong(1, userId);
                    pst.setLong(2, activityId);
                    int numberRows = pst.executeUpdate();
                    result = numberRows != 0;
                    LOG.debug("The {} rows was deleted by query to db", numberRows);
                }
            }
            if (result) {
                DaoFactory daoFactory = DaoFactory.getDaoFactory();
                UserActivityDao userActivityDao = daoFactory.getUserActivityDao(connection);
                result = userActivityDao.delete(userId, activityId);
            }
            if (result) {
                result = this.updateRequestStatus(requestId, statusId);
            }
            if (!result)
                rollback(connection);
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            LOG.error(e);
            throw new DaoException("Cannot approve request for removing activity", e, e.getErrorCode());
        } finally {
            setAutoCommit(connection);
        }
        return result;
    }

    @Override
    public Request read(Long requestId) throws DaoException {
        LOG.debug("Obtained request 'id' to read it from database is: {}", requestId);
        Request request = new Request();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_REQUEST_BY_ID)) {
            pst.setLong(1, requestId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read the request from database has already been completed successfully");
            if (!rs.next()) throw new SQLException();
            mapRowFromDB.map(rs, request);
            LOG.debug("The request: {} has already been found by query to database", request);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read 'request for activity from user' by id", e, e.getErrorCode());
        }
        return request;
    }

    @Override
    public boolean update(Request request) throws DaoException {
        LOG.debug("Obtained request entity to update it at database is: {}", request);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_REQUEST_BY_ID)) {
            mapRowToDB.map(request, pst);
            pst.setLong(5, request.getId());
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update the request at database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request for activities from user' wasn't updated by query to database");
            }
            LOG.debug("The {} rows has already been changed to " +
                    "update 'request for activities from user'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot update 'request for activity from user' at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean delete(Long requestId) throws DaoException {
        LOG.debug("Obtained request 'id' to delete it from database is: {}", requestId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_DELETE_REQUEST_BY_ID)) {
            pst.setLong(1, requestId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to delete an user from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request for activities from user' wasn't deleted by query to database");
            }
            LOG.debug("The {} rows has already been removed to delete the " +
                    "'request for activities from user'", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot delete 'request for activity from user'", e, e.getErrorCode());
        }
        return result;
    }
}
