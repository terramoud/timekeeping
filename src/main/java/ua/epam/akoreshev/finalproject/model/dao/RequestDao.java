package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Creates additional dao methods to work with 'requests' table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public interface RequestDao extends BaseDao<Request, Long> {


    /**
     * Finds the {@link Request} entity by primary key and
     * changes 'status' of {@link Request} entity
     * that represents foreign key
     *
     * @param requestId the {@link Request} entity primary key
     * @param statusId  the {@link Status} entity primary key
     * @return {@code true} if row in the table was successfully changed
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean updateRequestStatus(long requestId, long statusId) throws DaoException;

    /**
     * Reads the {@link Request} entity from database
     *
     * @param userId     the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @param typeId     the {@link Type} entity primary key
     * @param statusId   the {@link Status} entity primary key
     * @return the {@link Request} entity or {@code null} if one isn't exists
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    Request read(long userId, long activityId, long typeId, long statusId) throws DaoException;


    /**
     * Creates list of {@link UserActivityRequest} beans that contains
     * the {@link User} entity and corresponding {@link Activity} entity and
     * {@link Status} entity and {@link Type} entity by obtained array
     * of identifiers of statuses
     * List has been sorted by table's column name and by sort order
     *
     * @param limit    number of records to find
     * @param offset   table row number which represents start point for limit
     * @param column   table column name
     * @param order    descending or ascending order switcher
     * @param statuses varargs identifiers of {@link Status} entity
     * @return the sorted and truncated by limit list of
     *         the {@link UserActivityRequest} beans
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    List<UserActivityRequest> findAllRequestsByStatuses(int limit,
                                                        int offset,
                                                        String column,
                                                        String order,
                                                        int[] statuses) throws DaoException;


    /**
     * Counts rows in 'requests' table by identifiers of {@link Status} entity
     *
     * @param statuses varargs identifiers of {@link Status} entity
     * @return the number the {@link Request} entities with obtained statuses
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    int getCountRowsByStatuses(int... statuses) throws DaoException;

    /**
     * Creates transaction that creates new the {@link UserActivity}
     * entity, creates new interval for user with {@code null} value for
     * start time and {@code null} value for finish time and changes the
     * {@link Request} entity status to 'approved'
     *
     * @param requestId  the {@link Request} entity primary key
     * @param statusId   the {@link Status} entity primary key
     * @param userId     the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @return {@code true} if transaction is successfully
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean approveAddActivityRequest(long requestId,
                                      int statusId,
                                      long userId,
                                      long activityId) throws DaoException;

    /**
     * Runs transaction that removes by obtained identifiers
     * {@link UserActivity} entity, set stop time for interval if user
     * had already started timekeeping of current activity and changes
     * the {@link Request} entity status to 'approved'
     *
     * @param requestId  the {@link Request} entity primary key
     * @param statusId   the {@link Status} entity primary key
     * @param userId     the {@link User} entity primary key
     * @param activityId the {@link Activity} entity primary key
     * @return {@code true} if transaction is successfully
     * @throws DaoException with {@link SQLException#getErrorCode()}
     *                      if {@link SQLException} was thrown by method
     */
    boolean approveRemovingActivityRequest(long requestId,
                                           int statusId,
                                           long userId,
                                           long activityId) throws DaoException;
}

