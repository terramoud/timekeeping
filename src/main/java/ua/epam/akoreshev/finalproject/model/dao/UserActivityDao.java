package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.UserActivity;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityBean;

import java.util.List;

public interface UserActivityDao extends BaseDao<UserActivity, Long> {
    boolean delete(long userId, long activityId) throws DaoException;

    UserActivity read(long userId, long activityId) throws DaoException;

    @Override
    default boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    default UserActivity read(Long id) {
        throw new UnsupportedOperationException();
    }

    List<UserActivityBean> findAll(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

