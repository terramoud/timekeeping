package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IntervalServiceImpl implements IntervalService {
    private static final Logger LOG = LogManager.getLogger(IntervalServiceImpl.class);
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
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

    @Override
    public long getCountUsersActivities() throws ServiceException {
        try {
            return intervalDao.getCountUserActivities();
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<UserStatistic> getStatisticsByUsers(int limit, int offset) throws ServiceException {
        List<UserStatistic> result = new LinkedList<>();
        try {
            Map<UserActivityBean, List<Interval>> userStatistics = intervalDao.findUserStatistics(limit, offset);
            for (Entry<UserActivityBean, List<Interval>> entry : userStatistics.entrySet()) {
                UserStatistic userStatistic = new UserStatistic();
                userStatistic.setUser(entry.getKey().getUser());
                userStatistic.setActivity(entry.getKey().getActivity());
                userStatistic.setTotal(getTotalSpentTime(entry.getValue()));
                userStatistic.setAttempts(entry.getValue().size());
                result.add(userStatistic);
            }
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException(e.getMessage());
        }
        LOG.debug("Obtained statistic by getStatisticsByUsers are {}", result);
        return result;
    }

    private Time getTotalSpentTime(List<Interval> intervals) {
        long seconds = intervals.stream()
                .filter(i -> i.getFinish() != null && i.getStart() != null)
                .mapToLong(i -> seconds(i.getFinish()) - seconds(i.getStart()))
                .sum();
        long hours = seconds / SECONDS_PER_HOUR;
        long remainderSeconds = seconds % SECONDS_PER_HOUR;
        long minutes = remainderSeconds / SECONDS_PER_MINUTE;
        long secs = remainderSeconds % SECONDS_PER_MINUTE;
        return new Time(hours, minutes, secs);
    }

    private long seconds(LocalDateTime time) {
        return time.toEpochSecond(OffsetDateTime.now().getOffset());
    }

}
