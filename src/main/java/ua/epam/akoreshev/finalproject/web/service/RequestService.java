package ua.epam.akoreshev.finalproject.web.service;

import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;

import java.util.List;

public interface RequestService {
    boolean createRequest(Request request) throws ServiceException, RequestException;

    List<UserActivityRequest> findRequestsByStatuses(int limit, int offset, String column, String order, int[] statuses) throws ServiceException;

    int getCountRequestsByStatuses(int... statuses) throws ServiceException;

    boolean approveRequest(long requestId, long userId, long activityId, long typeId) throws ServiceException, RequestException;

    boolean rejectRequest(long requestId) throws ServiceException, RequestException;
}
