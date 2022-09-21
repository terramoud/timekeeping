package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.*;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import java.time.LocalDateTime;
import java.util.*;

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
                    throw new ServiceException("There isn't interval in database");
                result.put(activity, interval);
            }
        } catch (DaoException e) {
            LOG.error("Cannot find 'intervals' by user and activity");
            throw new ServiceException("Cannot find 'intervals' by user and activity", e);
        }
        return result;
    }

    @Override
    public boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws ServiceException, IntervalException {
        try {
            Interval interval = intervalDao.readIntervalByUserActivity(userId, activityId);
            if (interval == null)
                throw new DaoException("User has not interval yet");
            if (interval.getStart() != null)
                throw new IntervalException("interval.error.already_started");
            return intervalDao.setStartTimeForUserActivity(userId, activityId, startTime);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE) {
                LOG.warn("Interval has an out-of-range values");
                throw new IntervalException("interval.error.out_of_range");
            }
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2) {
                LOG.warn("Interval has wrong fields");
                throw new IntervalException("interval.error.foreign_key_constraint");
            }
            LOG.error("User with id: '{}' cannot start timekeeping", userId);
            throw new ServiceException("User cannot start timekeeping", e);
        }
    }

    @Override
    public boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime stopTime) throws ServiceException, IntervalException {
        try {
            Interval interval = intervalDao.readIntervalByUserActivity(userId, activityId);
            if (interval == null)
                throw new DaoException("User has not interval yet");
            if (interval.getStart() == null)
                throw new IntervalException("interval.error.has_not_started_yet");
            return intervalDao.setFinishTimeForUserActivity(userId, activityId, stopTime);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE) {
                LOG.warn("Interval has an out-of-range values");
                throw new IntervalException("interval.error.out_of_range");
            }
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2) {
                LOG.warn("Interval has wrong fields");
                throw new IntervalException("interval.error.foreign_key_constraint");
            }
            LOG.error("User with id: '{}' cannot stop timekeeping", userId);
            throw new ServiceException("User cannot stop timekeeping", e);
        }
    }

    @Override
    public int getNumberUsersActivities() throws ServiceException {
        try {
            return intervalDao.getNumberIntervals();
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<UserStatistic> getStatisticsByUsers(int limit, int offset, String columnName, String sortOrder) throws ServiceException {
        try {
            return intervalDao.findUserStatistics(limit, offset, columnName, sortOrder);
        } catch (DaoException e) {
            LOG.error("Cannot get timekeeping statistics for users");
            throw new ServiceException("Cannot get timekeeping statistics for users", e);
        }
    }
}
