package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.UserActivity;

import java.util.List;

public interface UserActivityDao extends BaseDao<UserActivity, Long> {
    boolean removeAllUsersActivitiesByUser(long userId) throws DaoException;
    List<UserActivity> findAllUsersActivitiesByUser(long userId) throws DaoException;
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
}

