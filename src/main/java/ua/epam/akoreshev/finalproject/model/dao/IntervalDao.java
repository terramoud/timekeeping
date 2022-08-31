package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.UserActivity;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityBean;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IntervalDao extends BaseDao<Interval, Long> {
    boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws DaoException;

    boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime finishTime) throws DaoException;

    List<Interval> findAllIntervalsByUserActivity(long userId, long activityId) throws DaoException;
    Interval readIntervalByUserActivity(long userId, long activityId) throws DaoException;

    long getCountUserActivities() throws DaoException;

    Map<UserActivityBean, List<Interval>> findUserStatistics(int limit, int offset) throws DaoException;
}

