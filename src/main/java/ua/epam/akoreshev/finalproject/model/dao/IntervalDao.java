package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Interval;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.model.entity.UserStatistic;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Creates additional dao methods to work with the 'intervals' table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface IntervalDao extends BaseDao<Interval, Long> {


    /**
     * Sets start time for user's activity.
     *
     * <p>Returns false when user has already started another activity.
     * Returns false when user hasn't activity yet<p/>
     *
     * @param userId the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @param startTime time when user has started timekeeping of the activity
     * @return {@code true} if start time for activity was successfully set
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean setStartTimeForUserActivity(long userId, long activityId, LocalDateTime startTime) throws DaoException;

    /**
     * Sets finish time for user's activity.
     *
     * <p>Returns false when user has already started timekeeping another activity.
     * Returns false when user hasn't activity yet
     * Returns false when user hasn't started timekeeping activity yet
     * Returns false when user has already finished timekeeping of
     * current activity<p/>
     *
     * @param userId the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @param finishTime time when user has finished timekeeping of the activity
     * @return {@code true} if start time for activity was successfully set
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean setFinishTimeForUserActivity(long userId, long activityId, LocalDateTime finishTime) throws DaoException;

    /**
     * Gets timekeeping for user's activity
     *
     * @param userId the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @return the {@link Interval} entity that represent start and finish time
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    Interval readIntervalByUserActivity(long userId, long activityId) throws DaoException;


    /**
     * Determines the number users' activities where
     * users have completed at least one timekeeping
     * interval
     *
     * @return the number completes {@link Interval} entities for
     *         all {@link User} entities
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    int getNumberIntervals() throws DaoException;

    /**
     * Determines full statistics for all {@link User} entities that represents
     * list of {@link UserStatistic} beans that contains from {@link User}
     * entity and corresponding {@link Activity} entity and total number
     * attempts of timekeeping for every users's activity and summary spent
     * time for every users's activity
     * List has been sorted by table's column name and by sort order
     *
     * @param limit number of records to find
     * @param offset table row number which represents start point for limit
     * @param columnName table column name
     * @param sortOrder descending or ascending order switcher
     * @return the sorted and truncated by limit list of
     *         the {@link UserStatistic} beans
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<UserStatistic> findUserStatistics(int limit, int offset, String columnName, String sortOrder) throws DaoException;
}

