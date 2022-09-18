package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.IntervalException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.IntervalDao;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class IntervalServiceImplTest {
    private IntervalService intervalService;
    private IntervalDao intervalDao;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        intervalDao = Mockito.mock(IntervalDao.class);
        intervalService = new IntervalServiceImpl(intervalDao);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see IntervalServiceImpl#findIntervalsByUserAndActivities(User, List)
     */
    @Test
    void testFindIntervalsByUserAndActivitiesShouldReturnMap() throws DaoException, ServiceException {
        User user = new User(0L, "testUserLogin", "testUser@email.com", "Password111", 2);
        Interval interval = new Interval();
        List<Activity> activities = LongStream.range(0, 5L)
                .mapToObj(i -> new Activity(i, "activity" + i, i + "activityUk", i))
                .collect(Collectors.toList());
        Map<Activity, Interval> expected = activities.stream()
                .collect(Collectors.toMap(Function.identity(), i -> interval));
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        assertEquals(expected, intervalService.findIntervalsByUserAndActivities(user, activities));
        assertEquals(expected.size(), intervalService.findIntervalsByUserAndActivities(user, activities).size());
    }

    /**
     * @see IntervalServiceImpl#findIntervalsByUserAndActivities(User, List)
     */
    @Test
    void testFindIntervalsByUserAndActivitiesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        User user = new User(0L, "testUserLogin", "testUser@email.com", "Password111", 2);
        List<Activity> activities = LongStream.range(0, 5L)
                .mapToObj(i -> new Activity(i, "activity" + i, i + "activityUk", i))
                .collect(Collectors.toList());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong()))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> intervalService.findIntervalsByUserAndActivities(user, activities));
    }

    /**
     * @see IntervalServiceImpl#findIntervalsByUserAndActivities(User, List)
     */
    @Test
    void testFindIntervalsByUserAndActivitiesShouldThrowExceptionWhenIntervalIsNotExists() throws DaoException {
        User user = new User(0L, "testUserLogin", "testUser@email.com", "Password111", 2);
        List<Activity> activities = LongStream.range(0, 5L)
                .mapToObj(i -> new Activity(i, "activity" + i, i + "activityUk", i))
                .collect(Collectors.toList());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(null);
        assertThrows(ServiceException.class, () -> intervalService.findIntervalsByUserAndActivities(user, activities));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldReturnTrueWhenStartTimeSuccessfullySet() throws DaoException, ServiceException, IntervalException {
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(new Interval());
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any())).thenReturn(true);
        assertTrue(intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldReturnFalseWhenCannotSetStartTime() throws DaoException, ServiceException, IntervalException {
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(new Interval());
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any())).thenReturn(false);
        assertFalse(intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(new Interval());
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldThrowExceptionWhenIntervalIsNotExists() throws DaoException {
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(new Interval());
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldThrowExceptionWhenIntervalIsAlreadyStarted() throws DaoException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any())).thenReturn(true);
        assertThrows(IntervalException.class,
                () -> intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldThrowExceptionWhenIntervalIsInvalid() throws DaoException {
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(new Interval());
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any()))
                .thenThrow(new DaoException("foreign key constraint",
                        new SQLException(),
                        MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2));
        assertThrows(IntervalException.class,
                () -> intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivityShouldThrowExceptionWhenHasOutOfRangeField() throws DaoException {
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(new Interval());
        Mockito.when(intervalDao.setStartTimeForUserActivity(anyLong(), anyLong(), any()))
                .thenThrow(new DaoException("out-of-range exception",
                        new SQLException(),
                        MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE));
        assertThrows(IntervalException.class,
                () -> intervalService.setStartTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }


    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldReturnTrueWhenFinishTimeSuccessfullySet() throws DaoException, ServiceException, IntervalException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any())).thenReturn(true);
        assertTrue(intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldReturnFalseWhenCannotSetFinishTime() throws DaoException, ServiceException, IntervalException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any())).thenReturn(true);
        assertTrue(intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldThrowExceptionWhenIntervalHasNotStartedYet() throws DaoException {
        Interval interval = new Interval();
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any())).thenReturn(true);
        assertThrows(IntervalException.class,
                () -> intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldThrowExceptionWhenIntervalIsNotExists() throws DaoException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldThrowExceptionWhenIntervalIsInvalid() throws DaoException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any()))
                .thenThrow(new DaoException("foreign key constraint",
                        new SQLException(),
                        MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2));
        assertThrows(IntervalException.class,
                () -> intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void setFinishTimeForUserActivityShouldThrowExceptionWhenIntervalHasOutOfRangeField() throws DaoException {
        Interval interval = new Interval();
        interval.setStart(LocalDateTime.now());
        Mockito.when(intervalDao.readIntervalByUserActivity(anyLong(), anyLong())).thenReturn(interval);
        Mockito.when(intervalDao.setFinishTimeForUserActivity(anyLong(), anyLong(), any()))
                .thenThrow(new DaoException("out-of-range exception",
                        new SQLException(),
                        MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE));
        assertThrows(IntervalException.class,
                () -> intervalService.setFinishTimeForUserActivity(0L, 0L, LocalDateTime.now()));
    }

    /**
     * @see IntervalServiceImpl#getNumberUsersActivities()
     */
    @Test
    void testGetNumberUsersActivitiesShouldReturnNumber() throws ServiceException, DaoException {
        long expected = 1L;
        Mockito.when(intervalDao.getNumberIntervals()).thenReturn(expected);
        assertEquals(expected, intervalService.getNumberUsersActivities());
    }

    /**
     * @see ActivityServiceImpl#getNumberActivities()
     */
    @Test
    void testGetNumberActivitiesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(intervalDao.getNumberIntervals()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> intervalService.getNumberUsersActivities());
    }

    /**
     * @see IntervalServiceImpl#getStatisticsByUsers(int, int, String, String)
     */
    @Test
    void testGetStatisticsByUsersShouldReturnList() throws DaoException, ServiceException {
        int limit = 5;
        List<UserStatistic> expected = IntStream.range(0, limit)
                .mapToObj(i -> new UserStatistic(
                        new User(i, "Login" + i, "email" + i, "Password" + i, i),
                        new Activity(i, "activity" + i, "activity_uk" + i, i),
                        new Time((int) i),
                        i))
                .collect(Collectors.toList());
        Mockito.when(intervalDao.findUserStatistics(limit, 0, "activities.name_en", "ASC"))
                .thenReturn(expected);
        assertEquals(expected,
                intervalService.getStatisticsByUsers(limit, 0, "activities.name_en", "ASC"));
    }

    /**
     * @see IntervalServiceImpl#getStatisticsByUsers(int, int, String, String)
     */
    @Test
    void testGetStatisticsByUsersShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        int limit = 5;
        Mockito.when(intervalDao.findUserStatistics(limit, 0, "activities.name_en", "ASC"))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () ->
                intervalService.getStatisticsByUsers(limit, 0, "activities.name_en", "ASC"));
    }
}