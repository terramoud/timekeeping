package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.entity.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RequestDaoImpl implements RequestDao {
    private static final String SQL_GET_REQUEST_BY_ID = "SELECT * FROM requests WHERE id=?";

    private static final String SQL_DELETE_REQUEST_BY_ID = "DELETE FROM requests WHERE id=?";

    private static final String SQL_CREATE_REQUEST =
            "INSERT INTO requests VALUES (DEFAULT, ?, ?, ?, ?)";

    private static final String SQL_FIND_ALL_REQUESTS = "SELECT * FROM requests";

    private static final String SQL_FIND_ALL_REQUESTS_BY_STATUS_ID = "SELECT * FROM requests WHERE status_id = ?";

    private static final String SQL_FIND_ALL_REQUESTS_BY_STATUS_NAME =
            "SELECT * FROM requests " +
                    "INNER JOIN statuses ON requests.status_id = statuses.id " +
                    "INNER JOIN translations ON statuses.translation_id = translations.id " +
                    "WHERE translated_uk = ? OR translated_en = ? OR statuses.name = ?";

    private static final String SQL_UPDATE_REQUEST_BY_ID =
            "UPDATE requests SET user_id = ?, activity_id = ?, type_id = ?, status_id = ? WHERE id = ?";

    private static final String SQL_UPDATE_REQUEST_BY_STATUS_ID =
            "UPDATE requests SET status_id = ? WHERE id = ?";

    private static final String SQL_UPDATE_REQUEST_BY_STATUS_NAME =
            "UPDATE requests " +
                    "SET status_id = (" +
                        "SELECT statuses.id FROM statuses " +
                        "INNER JOIN translations ON statuses.translation_id = translations.id " +
                        "WHERE translated_uk = ? OR translated_en = ? OR statuses.name = ?) " +
                    "WHERE id = ?";

    private final Mapper<Request, PreparedStatement> mapRowToDB = (Request request, PreparedStatement preparedStatement) -> {
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
            LOG.trace("SQL query find all 'requests for activities from user' to database has already been completed successfully");
            while (rs.next()) {
                Request request = new Request();
                mapRowFromDB.map(rs, request);
                requestsList.add(request);
            }
            LOG.debug("The {} 'requests for activities from user' has already been found by query to database", requestsList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to find all 'requests for activities from user'");
            throw new DaoException("Cannot find 'requests for activities from user'", e);
        }
        if (requestsList.isEmpty()) {
            LOG.warn("Warning: empty list 'requests for activities from user' has already been returned  by findAll() method");
        }
        return requestsList;
    }

    @Override
    public List<Request> findAllRequestsByStatus(long statusId) throws DaoException {
        LOG.debug("Obtained request's 'status id' to find it at database is: {}", statusId);
        List<Request> requestsList = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_REQUESTS_BY_STATUS_ID)) {
            pst.setLong(1, statusId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                Request request = new Request();
                mapRowFromDB.map(rs, request);
                requestsList.add(request);
            }
            LOG.debug("The {} 'requests for activities from user' has already been found by query to database", requestsList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to find all 'requests for activities' by status id");
            throw new DaoException("Cannot find requests for activities from user by status id", e);
        }
        if (requestsList.isEmpty()) {
            LOG.warn("Empty list 'requests for activities from user' has already been returned by find all requests by status id");
        }
        return requestsList;
    }

    @Override
    public List<Request> findAllRequestsByStatus(String statusName) throws DaoException {
        LOG.debug("Obtained request's 'status name' to find it at database is: {}", statusName);
        List<Request> requestsList = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_FIND_ALL_REQUESTS_BY_STATUS_NAME)) {
            pst.setString(1, statusName);
            pst.setString(2, statusName);
            pst.setString(3, statusName);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to database has already been completed successfully");
            while (rs.next()) {
                Request request = new Request();
                mapRowFromDB.map(rs, request);
                requestsList.add(request);
            }
            LOG.debug("The {} 'requests for activities from user' has already been found by query to database", requestsList.size());
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to find all 'requests for activities' by status name");
            throw new DaoException("Cannot find requests for activities from user by status name", e);
        }
        if (requestsList.isEmpty()) {
            LOG.warn("Empty list 'requests for activities from user' has already been returned by find all requests by status name");
        }
        return requestsList;
    }

    @Override
    public boolean updateRequestStatus(long requestId, long statusId) throws DaoException {
        LOG.debug("Obtained 'request id' and 'status id' to read it from database are: {} and {}", requestId, statusId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_REQUEST_BY_STATUS_ID)) {
            pst.setLong(1, statusId);
            pst.setLong(2, requestId);
            LOG.trace("SQL query to update the 'request status by id' at database has already been completed successfully");
            int rowCount = pst.executeUpdate();
            LOG.debug("The {} rows has already been changed to update 'request for activities from user'", rowCount);
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request status' wasn't updated by query to database");
            }
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to change 'request status'");
            throw new DaoException("Cannot update request for activities from user at database", e);
        }
        return result;
    }

    @Override
    public boolean updateRequestStatus(long requestId, String statusName) throws DaoException {
        LOG.debug("Obtained 'request id' and 'status name' to read it from database are: {} and {}", requestId, statusName);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_REQUEST_BY_STATUS_NAME)) {
            pst.setString(1, statusName);
            pst.setString(2, statusName);
            pst.setString(3, statusName);
            pst.setLong(4, requestId);
            LOG.trace("SQL query to update the 'request status' at database has already been completed successfully");
            int rowCount = pst.executeUpdate();
            LOG.debug("The {} rows has already been changed to update 'request status'", rowCount);
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request status' wasn't updated by query to database");
            }
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to change 'request status'");
            throw new DaoException("Cannot update 'request status' at database", e);
        }
        return result;
    }

    @Override
    public boolean create(Request request) throws DaoException {
        LOG.debug("Obtained request entity to create it at database is: {}", request);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS)) {
            mapRowToDB.map(request, pst);
            int rowCount = pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                request.setId(rs.getLong(1));
                LOG.debug("The source request entity has synchronized 'id' with created one at database and now represent the: {}", request);
            }
            LOG.trace("SQL query to create request has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The 'request for activities from user' wasn't created by query to database");
            }
            LOG.debug("The {} rows has already been added to database to create request", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to create 'request for activities from user'");
            throw new DaoException("Cannot create 'request for activities from user' at database", e);
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
        } catch (SQLException ex) {
            LOG.error("DAO exception has already been thrown by sql query to get request by id");
            throw new DaoException("Cannot read 'request for activities from user' by id", ex);
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
            LOG.debug("The {} rows has already been changed to update 'request for activities from user'", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has been thrown by sql query to update 'request for activities from user'");
            throw new DaoException("Cannot update 'request for activities from user' at database", e);
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
            LOG.debug("The {} rows has already been removed to delete the 'request for activities from user'", rowCount);
        } catch (SQLException e) {
            LOG.error("DAO exception has already been thrown by sql query to remove 'request' by id");
            throw new DaoException("Cannot delete 'request for activities from user' from database", e);
        }
        return result;
    }
}
