package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Interval;

import java.time.LocalDateTime;
import java.util.List;

public interface IntervalDao extends BaseDao<Interval, Long> {
    boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws DaoException;

    boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime finishTime) throws DaoException;

    List<Interval> findAllIntervalsByUserActivity(long userId, long activityId) throws DaoException;
    Interval readIntervalByUserActivity(long userId, long activityId) throws DaoException;
}

