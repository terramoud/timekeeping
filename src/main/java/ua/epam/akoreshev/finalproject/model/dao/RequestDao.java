package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;

import java.util.List;

public interface RequestDao extends BaseDao<Request, Long> {
    List<Request> findAllRequestsByStatus(long statusId) throws DaoException;

    List<Request> findAllRequestsByStatus(String statusName) throws DaoException;

    boolean updateRequestStatus(long requestId, long statusId) throws DaoException;

    boolean updateRequestStatus(long requestId, String status) throws DaoException;

    Request read(long userId, long activityId, long typeId, long statusId) throws DaoException;

    List<UserActivityRequest> findAllRequestsByStatuses(int limit, int offset, int[] statuses) throws DaoException;

    int getCountRowsByStatuses(int... statuses) throws DaoException;

    boolean approveAddActivityRequest(long requestId, int statusId, long userId, long activityId) throws DaoException;

    boolean approveRemovingActivityRequest(long requestId, int statusId, long userId, long activityId) throws DaoException;
}

