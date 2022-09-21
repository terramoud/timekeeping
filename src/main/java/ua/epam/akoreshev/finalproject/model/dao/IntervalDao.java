package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;

import java.time.LocalDateTime;
import java.util.List;

public interface IntervalDao extends BaseDao<Interval, Long> {
    boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws DaoException;

    boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime finishTime) throws DaoException;

    Interval readIntervalByUserActivity(long userId, long activityId) throws DaoException;

    int getNumberIntervals() throws DaoException;

    List<UserStatistic> findUserStatistics(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

