package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.*;
import ua.epam.akoreshev.finalproject.model.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class IntervalDaoImplTest {
    private static IntervalDao intervalDao;
    private static Connection connection;
    private static ActivityDao activityDao;
    private static UserDao userDao;
    private static UserActivityDao userActivityDao;

    @BeforeAll
    public static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
        intervalDao = new IntervalDaoImpl(connection);
        activityDao = new ActivityDaoImpl(connection);
        userDao = new UserDaoImpl(connection);
        userActivityDao = new UserActivityDaoImpl(connection);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @AfterAll
    public static void tearDownBeforeAll() throws SQLException {
        connection.close();
    }

    /**
     * @see IntervalDaoImpl#read(Long)
     */
    @Test
    void testReadInterval() throws DaoException, SQLException {
        Interval expectedInterval = new Interval(null, null, 1, 1);
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO intervals VALUES (DEFAULT, null, null, 1, 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new UnsupportedOperationException("Fatal: Cannot create the test for IntervalDao. The test is crashed");
        }
        expectedInterval.setId(rs.getLong(1));
        Interval actualInterval = intervalDao.read(expectedInterval.getId());
        assertEquals(expectedInterval, actualInterval);
    }

    /**
     * @see IntervalDaoImpl#read(Long)
     */
    @Test
    void testReadIntervalShouldThrownExceptionWhenIntervalIsMissing() {
        assertThrows(DaoException.class, () -> intervalDao.read(0L));
        assertThrows(DaoException.class, () -> intervalDao.read(-1L));
    }

    /**
     * @see IntervalDaoImpl#create(Interval)
     */
    @Test
    void testCreateInterval() throws DaoException {
        Interval expectedInterval = new Interval(null, null, 1, 1);
        long rowsBeforeCreate = getCountRowsFromTable();
        assertTrue(intervalDao.create(expectedInterval)); // And now expected has synchronized 'id' with db
        Interval actualInterval = intervalDao.read(expectedInterval.getId());
        assertEquals(expectedInterval, actualInterval);
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate + 1, rowsAfterCreate);
    }

    /**
     * @see IntervalDaoImpl#create(Interval)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenIntervalHasNegativeOrZeroField")
    void testCreateIntervalShouldThrowExceptionWhenIntervalHasNegativeOrZeroField(Interval testInterval) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> intervalDao.create(testInterval));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }


    /**
     * @see IntervalDaoImpl#update(Interval)
     */
    @Test
    void testUpdateActivity() throws DaoException {
        Activity activity1 = new Activity("TestActivity1", "Тестова діяльність1", 1);
        Activity activity2 = new Activity("TestActivity2", "Тестова діяльність2", 1);
        activityDao.create(activity1);
        activityDao.create(activity2);
        User user1 = new User(0, "testUser1!", "test@mail.com1!", "*", 2);
        User user2 = new User(0, "testUser2!", "test@mail.com2!", "*", 2);
        userDao.create(user1);
        userDao.create(user2);

        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Interval> requestList = new LinkedList<>();
        requestList.add(new Interval(null, null, user1.getId(), activity1.getId()));
        requestList.add(new Interval(null, null, user2.getId(), activity1.getId()));
        requestList.add(new Interval(null, null, user2.getId(), activity2.getId()));
        requestList.add(new Interval(time, null, user1.getId(), activity1.getId()));
        requestList.add(new Interval(time, time, user1.getId(), activity1.getId()));
        requestList.add(new Interval(time, null, user2.getId(), activity2.getId()));

        Interval sourceInterval = new Interval(null, null, user1.getId(), activity1.getId());
        assertTrue(intervalDao.create(sourceInterval)); // And now source has synchronized 'id' with db
        for (Interval changedInterval : requestList) {
            sourceInterval.setStart(changedInterval.getStart());
            sourceInterval.setFinish(changedInterval.getFinish());
            sourceInterval.setUserId(changedInterval.getUserId());
            sourceInterval.setActivityId(changedInterval.getActivityId());

            assertTrue(intervalDao.update(sourceInterval));
            Interval actualInterval = intervalDao.read(sourceInterval.getId());
            assertEquals(sourceInterval, actualInterval);
        }
    }


    /**
     * @see IntervalDaoImpl#update(Interval)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenIntervalHasNegativeOrZeroField")
    void testUpdateRequestShouldThrowExceptionWhenIntervalHasNegativeOrZeroField(Interval wrongFieldInterval) throws DaoException {
        Interval sourceInterval = new Interval(null, null, 1, 1);
        assertTrue(intervalDao.create(sourceInterval));
        sourceInterval.setUserId(wrongFieldInterval.getUserId());
        sourceInterval.setActivityId(wrongFieldInterval.getActivityId());
        assertThrows(DaoException.class, () -> intervalDao.update(sourceInterval));
    }

    /**
     * @see IntervalDaoImpl#delete(Long)
     */
    @Test
    void testDeleteInterval() throws DaoException, SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO intervals VALUES (DEFAULT, null, null, 1, 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test for IntervalDao. The test is crashed");
        }
        long rowsBeforeDelete = getCountRowsFromTable();
        assertTrue(intervalDao.delete(rs.getLong(1)));
        long rowsAfterDelete = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> intervalDao.read(rs.getLong(1)));
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    /**
     * @see IntervalDaoImpl#delete(Long)
     */
    @Test
    void testDeleteIntervalShouldReturnFalseWhenIntervalIsMissing() throws DaoException {
        assertFalse(intervalDao.delete(0L));
        assertFalse(intervalDao.delete(-1L));
        assertFalse(intervalDao.delete(Long.MAX_VALUE));
    }

    /**
     * @see IntervalDaoImpl#findAll()
     */
    @Test
    void testFindAllIntervals() throws DaoException, SQLException {
        List<Interval> intervalListBeforeAddedNewInterval = intervalDao.findAll();
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO intervals VALUES (DEFAULT, null, null, 1, 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test for IntervalDao. The test is crashed");
        }
        Interval expectedInterval = new Interval(null, null, 1, 1);
        expectedInterval.setId(rs.getLong(1));

        List<Interval> intervalListAfterAddedInterval = intervalDao.findAll();
        LinkedList<Interval> differences = new LinkedList<>(intervalListAfterAddedInterval);
        differences.removeAll(intervalListBeforeAddedNewInterval);
        assertEquals(1, differences.size());
        assertEquals(expectedInterval, differences.getFirst());
    }

    /**
     * @see IntervalDaoImpl#setStartTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetStartTimeForUserActivity() throws DaoException, SQLException {
        Activity activity1 = new Activity("TestActivity1", "Тестова діяльність1", 1);
        assertTrue(activityDao.create(activity1));
        User user1 = new User(0, "testUser1!", "test@mail.com1!", "*", 2);
        assertTrue(userDao.create(user1));
        assertTrue(userActivityDao.create(new UserActivity(user1.getId(), activity1.getId(), false)));

        Interval sourceInterval = new Interval(null, null, user1.getId(), activity1.getId());
        assertTrue(intervalDao.create(sourceInterval));

        LocalDateTime expectedTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        assertTrue(intervalDao.setStartTimeForUserActivity(user1.getId(), activity1.getId(), expectedTime));
        LocalDateTime actualStartTime = intervalDao.read(sourceInterval.getId()).getStart();
        assertEquals(expectedTime, actualStartTime);

        assertTrue(userDao.delete(user1.getId()));
        assertTrue(activityDao.delete(activity1.getId()));
        connection.setAutoCommit(false);
    }


    /**
     * @see IntervalDaoImpl#setFinishTimeForUserActivity(long, long, LocalDateTime)
     */
    @Test
    void testSetFinishTimeForUserActivity() throws DaoException, SQLException {
        Activity activity1 = new Activity("TestActivity1", "Тестова діяльність1", 1);
        assertTrue(activityDao.create(activity1));
        User user1 = new User(0, "testUser1!", "test@mail.com1!", "*", 2);
        assertTrue(userDao.create(user1));
        assertTrue(userActivityDao.create(new UserActivity(user1.getId(), activity1.getId(), true)));

        LocalDateTime startTime = LocalDateTime.of(2022, 7, 19, 14, 5);
        Interval sourceInterval = new Interval(startTime, null, user1.getId(), activity1.getId());
        assertTrue(intervalDao.create(sourceInterval));

        LocalDateTime expectedTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        assertTrue(intervalDao.setFinishTimeForUserActivity(user1.getId(), activity1.getId(), expectedTime));
        LocalDateTime actualFinishTime = intervalDao.read(sourceInterval.getId()).getFinish();
        assertEquals(expectedTime, actualFinishTime);

        assertTrue(userDao.delete(user1.getId()));
        assertTrue(activityDao.delete(activity1.getId()));
        connection.setAutoCommit(false);
    }

    private static Stream<Arguments> testCasesWhenIntervalHasNegativeOrZeroField() {
        return Stream.of(
                Arguments.of(new Interval(null, null, 0, 0)),
                Arguments.of(new Interval(null, null, 0, -1)),
                Arguments.of(new Interval(null, null, -1, 0)),
                Arguments.of(new Interval(null, null, -1, -1))
        );
    }

    private long getCountRowsFromTable() {
        long result = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM intervals");
            if (rs.next()) {
                result = rs.getLong("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
