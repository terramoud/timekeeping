package ua.epam.akoreshev.finalproject.web.service;

import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IntervalService {
    Map<Activity, Interval> findIntervalsByUserAndActivities(User user, List<Activity> userActivities) throws ServiceException;

    boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws ServiceException;

    boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime stopTime) throws ServiceException;
}
