package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
import ua.epam.akoreshev.finalproject.model.dao.UserActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.UserActivity;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserActivityDaoImplTest {
    private static UserActivityDao userActivityDao;
    private static Connection connection;

    @BeforeAll
    public static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
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
     * @see UserActivityDaoImpl#read(long, long)
     */
    @Test
    void testReadUserActivity() throws DaoException, SQLException {
        long[] generatedIds = createTestData();
        assertThrows(DaoException.class, () -> userActivityDao.read(generatedIds[0], generatedIds[1]));

        PreparedStatement pst = connection.prepareStatement("INSERT INTO users_activities VALUES (?, ?, false)");
        pst.setLong(1, generatedIds[0]);
        pst.setLong(2, generatedIds[1]);
        pst.executeUpdate();

        UserActivity expected = new UserActivity(generatedIds[0], generatedIds[1]);
        UserActivity actual = userActivityDao.read(generatedIds[0], generatedIds[1]);
        assertEquals(expected, actual);
    }

    /**
     * @see UserActivityDaoImpl#read(long, long)
     */
    @Test
    void testReadUserActivityShouldThrownExceptionWhenArgumentsAreWrong() {
        assertThrows(DaoException.class, () -> userActivityDao.read(0L, 0L));
        assertThrows(DaoException.class, () -> userActivityDao.read(-1L, -1L));
        assertThrows(DaoException.class, () -> userActivityDao.read(0L, -1L));
        assertThrows(DaoException.class, () -> userActivityDao.read(-1L, 0L));
    }

    /**
     * @see UserActivityDaoImpl#delete(long, long)
     */
    @Test
    void testDeleteUserActivity() throws DaoException, SQLException {
        long[] generatedIds = createTestData();
        PreparedStatement pst = connection.prepareStatement("INSERT INTO users_activities VALUES (?, ?, false)");
        pst.setLong(1, generatedIds[0]);
        pst.setLong(2, generatedIds[1]);
        pst.executeUpdate();

        long rowsBeforeDelete = getCountRowsFromTable();
        assertTrue(userActivityDao.delete(generatedIds[0], generatedIds[1]));
        long rowsAfterDelete = getCountRowsFromTable();
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
        assertFalse(userActivityDao.delete(generatedIds[0], generatedIds[1]));
    }

    /**
     * @see UserActivityDaoImpl#delete(long, long)
     */
    @Test
    void testDeleteUserActivityShouldReturnNullWhenRowIsZeroOrNegative() throws DaoException {
        assertFalse(userActivityDao.delete(0L, 0L));
        assertFalse(userActivityDao.delete(-1L, -1L));
        assertFalse(userActivityDao.delete(0L, -1L));
        assertFalse(userActivityDao.delete(-1L, 0L));
    }

    /**
     * @see UserActivityDaoImpl#delete(long, long)
     */
    @Test
    void testDeleteUserActivityShouldReturnFalseWhenRowIsMissing() throws DaoException {
        assertFalse(userActivityDao.delete(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    /**
     * @see UserActivityDaoImpl#create(UserActivity)
     */
    @Test
    void testCreateUserActivity() throws DaoException, SQLException {
        long[] generatedIds = createTestData();
        assertThrows(DaoException.class, () -> userActivityDao.read(generatedIds[0], generatedIds[1]));
        UserActivity expected = new UserActivity(generatedIds[0], generatedIds[1]);
        long rowsBefore = getCountRowsFromTable();
        assertTrue(userActivityDao.create(expected));
        long rowsAfter = getCountRowsFromTable();
        UserActivity actual = userActivityDao.read(generatedIds[0], generatedIds[1]);
        assertEquals(expected, actual);
        assertEquals(rowsBefore + 1, rowsAfter);
    }

    /**
     * @see UserActivityDaoImpl#create(UserActivity)
     */
    @Test
    void testCreateUserActivityShouldThrowExceptionWhenUserActivityIsNotUniqueRowInTable() throws DaoException, SQLException {
        long[] generatedIds = createTestData();
        UserActivity expected = new UserActivity(generatedIds[0], generatedIds[1]);
        assertTrue(userActivityDao.create(expected));
        UserActivity duplicated = new UserActivity(expected.getUserId(), expected.getActivityId());
        assertThrows(DaoException.class, () -> userActivityDao.create(duplicated));
    }

    /**
     * @see UserActivityDaoImpl#update(UserActivity)
     */
    @Test
    void testUpdateUserActivity() throws DaoException, SQLException {
        long[] generatedIds = createTestData();
        UserActivity source = new UserActivity(generatedIds[0], generatedIds[1], false);
        assertTrue(userActivityDao.create(source));

        source.setIsActive(true);
        assertTrue(userActivityDao.update(source));
    }

    /**
     * @see UserActivityDaoImpl#findAll()
     */
    @Test
    void testFindAllUserActivity() throws DaoException, SQLException {
        long[] generatedIds = createTestData();
        List<UserActivity> activityListBeforeAddedNewRow = userActivityDao.findAll();
        PreparedStatement pst = connection.prepareStatement("INSERT INTO users_activities VALUES (?, ?, false)");
        pst.setLong(1, generatedIds[0]);
        pst.setLong(2, generatedIds[1]);
        pst.executeUpdate();
        UserActivity expectedUserActivity = new UserActivity(generatedIds[0], generatedIds[1]);

        List<UserActivity> activityListAfterAddedNewRow = userActivityDao.findAll();
        LinkedList<UserActivity> differences = new LinkedList<>(activityListAfterAddedNewRow);
        differences.removeAll(activityListBeforeAddedNewRow);
        assertEquals(1, differences.size());
        assertEquals(expectedUserActivity, differences.getLast());
    }

    private long[] createTestData() throws SQLException {
        try {
            PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO users VALUES (DEFAULT, 'User1', 'test1@test.com', 'passtest', 2)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (!rs.next()) {
                throw new UnsupportedOperationException("Fatal: Cannot create the test for UserActivityDao. The test is crashed");
            }

            pst = connection.prepareStatement(
                    "INSERT INTO activities VALUES (DEFAULT, 'Activity1', 'Активність1', 1)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.executeUpdate();
            ResultSet rsActivities = pst.getGeneratedKeys();
            if (!rsActivities.next()) {
                throw new UnsupportedOperationException("Fatal: Cannot create the test for UserActivityDao. The test is crashed");
            }

            long userId = rs.getLong(1);
            long activityId = rsActivities.getLong(1);
            pst.close();
            return new long[]{userId, activityId};
        } catch (SQLException e) {
            connection.rollback();
            throw new UnsupportedOperationException("Fatal: Cannot create the test for UserActivityDao. The test is crashed");
        }
    }

    private long getCountRowsFromTable() {
        long result = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM users_activities");
            if (rs.next()) {
                result = rs.getLong("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
