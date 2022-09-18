package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import java.util.List;
import java.util.Objects;

public class RequestServiceImpl implements RequestService {
    private static final Logger LOG = LogManager.getLogger(RequestServiceImpl.class);
    public static final long ADD_ACTIVITY_CODE = 1;
    public static final int APPROVE_STATUS_CODE = 2;
    public static final int DENY_STATUS_CODE = 3;
    public static final String REQUEST_INVALID_MESSAGE = "request.error.foreign_key_constraint";
    private final RequestDao requestDao;

    public RequestServiceImpl(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @Override
    public boolean createRequest(Request req) throws ServiceException, RequestException {
        try {
            Request request = requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId());
            LOG.debug("Obtained 'request entity' is: {}", request);
            if (Objects.equals(req, request)) {
                LOG.warn("Request is forbidden. Entries are duplicated");
                throw new RequestException("request.error.duplicated");
            }
            return requestDao.create(req);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2) {
                LOG.warn("Entity of request has wrong fields");
                throw new RequestException(REQUEST_INVALID_MESSAGE);
            }
            LOG.error("Cannot add request: {}", req);
            throw new ServiceException("Cannot add request for activity", e);
        }
    }

    @Override
    public List<UserActivityRequest> findRequestsByStatuses(int limit, int offset, String column, String order, int[] statuses) throws ServiceException {
        try {
            return requestDao.findAllRequestsByStatuses(limit, offset, column, order, statuses);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find requests by statuses", e);
        }
    }

    @Override
    public int getCountRequestsByStatuses(int... statuses) throws ServiceException {
        try {
            return requestDao.getCountRowsByStatuses(statuses);
        } catch (DaoException e) {
            LOG.error("Cannot count requests by statuses");
            throw new ServiceException("Cannot count requests by statuses", e);
        }
    }

    @Override
    public boolean approveRequest(long requestId, long userId, long activityId, long typeId) throws ServiceException, RequestException {
        try {
            if (typeId == ADD_ACTIVITY_CODE) {
                return requestDao.approveAddActivityRequest(requestId, APPROVE_STATUS_CODE, userId, activityId);
            }
            return requestDao.approveRemovingActivityRequest(requestId, APPROVE_STATUS_CODE, userId, activityId);
        } catch (DaoException e) {
            Request request = new Request(requestId, userId, activityId, typeId, APPROVE_STATUS_CODE);
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2) {
                LOG.warn("Entity of request has wrong fields: {}", request);
                throw new RequestException(REQUEST_INVALID_MESSAGE);
            }
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE) {
                LOG.warn("Entity of request has an out-of-range id: {}", requestId);
                throw new RequestException("request.error.out_of_range");
            }
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                LOG.warn("Request is forbidden. Entries are duplicated");
                throw new RequestException("request.error.duplicated");
            }
            LOG.error("Cannot approve request with id: {}", requestId);
            throw new ServiceException("Cannot approve request", e);
        }
    }

    @Override
    public boolean rejectRequest(long requestId) throws ServiceException, RequestException {
        try {
            return requestDao.updateRequestStatus(requestId, DENY_STATUS_CODE);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2) {
                LOG.warn("Entity of request has wrong id: {}", requestId);
                throw new RequestException(REQUEST_INVALID_MESSAGE);
            }
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE) {
                LOG.warn("Entity of request has an out-of-range id: {}", requestId);
                throw new RequestException("request.error.out_of_range");
            }
            LOG.error("Cannot reject request with id: {}", requestId);
            throw new ServiceException("Cannot reject request", e);
        }
    }
}
