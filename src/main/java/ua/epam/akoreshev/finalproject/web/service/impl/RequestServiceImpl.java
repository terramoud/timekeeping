package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import java.util.Objects;

public class RequestServiceImpl implements RequestService {
    private static final Logger LOG = LogManager.getLogger(RequestServiceImpl.class);
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
}
