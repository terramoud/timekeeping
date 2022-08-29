package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IntervalServiceImpl implements IntervalService {
    private static final Logger LOG = LogManager.getLogger(IntervalServiceImpl.class);
    private final IntervalDao intervalDao;

    public IntervalServiceImpl(IntervalDao intervalDao) {
        this.intervalDao = intervalDao;
    }

    @Override
    public Map<Activity, Interval> findIntervalsByUserAndActivities(User user, List<Activity> userActivities) throws ServiceException {
        Map<Activity, Interval> result = new LinkedHashMap<>();
        try {
            for (Activity activity : userActivities) {
                Interval interval = intervalDao.readIntervalByUserActivity(user.getId(), activity.getId());
                if (interval == null)
                    throw new DaoException("There isn't interval in database");
                result.put(activity, interval);
            }
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Check obtained parameters " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws ServiceException {
        try {
            Interval interval = intervalDao.readIntervalByUserActivity(userId, activityId);
            if (interval == null)
                throw new DaoException("User has not interval yet");
            if (interval.getStart() != null) {
                return false;
            }
            return intervalDao.setStartTimeForUserActivity(userId, activityId, startTime);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Check obtained parameters " + e.getMessage());
        }
    }

    @Override
    public boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime stopTime) throws ServiceException {
        try {
            return intervalDao.setFinishTimeForUserActivity(userId, activityId, stopTime);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Check obtained parameters " + e.getMessage());
        }
    }

}
