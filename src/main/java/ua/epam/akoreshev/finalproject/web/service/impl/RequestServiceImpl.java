package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import java.util.List;
import java.util.Objects;

public class RequestServiceImpl implements RequestService {
    private static final Logger LOG = LogManager.getLogger(RequestServiceImpl.class);
    public static final int ADD_ACTIVITY_CODE = 1;
    public static final int APPROVE_STATUS_CODE = 2;
    public static final int DENY_STATUS_CODE = 3;
    private final RequestDao requestDao;

    public RequestServiceImpl(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @Override
    public boolean createRequest(Request req) throws ServiceException {
        try {
            Request request = requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId());
            LOG.debug("Obtained 'request entity' is: {}", request);
            if (Objects.equals(req, request)) {
                LOG.warn("Request is forbidden. Entries are duplicated");
                return false;
            }
            return requestDao.create(req);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot add request for activity " + e.getMessage());
        }
    }

    @Override
    public List<UserActivityRequest> findRequestsByStatuses(int limit, int offset, int[] statuses) throws ServiceException {
        try {
            return requestDao.findAllRequestsByStatuses(limit, offset, statuses);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find requests by statuses " + e.getMessage());
        }
    }

    @Override
    public int getCountRequestsByStatuses(int... statuses) throws ServiceException {
        try {
            return requestDao.getCountRowsByStatuses(statuses);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot get count requests " + e.getMessage());
        }
    }

    @Override
    public boolean approveRequest(long requestId, long userId, long activityId, long typeId) throws ServiceException {
        try {
            if (typeId == ADD_ACTIVITY_CODE) {
                return requestDao.approveAddActivityRequest(requestId, APPROVE_STATUS_CODE, userId, activityId);
            }
            return requestDao.approveRemovingActivityRequest(requestId, APPROVE_STATUS_CODE, userId, activityId);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot approve request" + e.getMessage());
        }
    }

    @Override
    public boolean rejectRequest(long requestId) throws ServiceException {
        try {
            return requestDao.updateRequestStatus(requestId, DENY_STATUS_CODE);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot reject request" + e.getMessage());
        }
    }
}
