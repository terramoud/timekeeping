package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Request;

import java.util.List;

public interface RequestDao extends BaseDao<Request, Long> {
    List<Request> findAllRequestsByStatus(long statusId) throws DaoException;

    List<Request> findAllRequestsByStatus(String statusName) throws DaoException;

    boolean updateRequestStatus(long requestId, long statusId) throws DaoException;

    boolean updateRequestStatus(long requestId, String status) throws DaoException;

}

