package ua.epam.akoreshev.finalproject.web.service;

import ua.epam.akoreshev.finalproject.exceptions.IntervalException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IntervalService {
    Map<Activity, Interval> findIntervalsByUserAndActivities(User user, List<Activity> userActivities) throws ServiceException;

    boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws ServiceException, IntervalException;

    boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime stopTime) throws ServiceException, IntervalException;

    long getNumberUsersActivities() throws ServiceException;

    List<UserStatistic> getStatisticsByUsers(int limit, int offset, String columnName, String sortOrder) throws ServiceException;
}
