package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
import ua.epam.akoreshev.finalproject.model.entity.Activity;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ActivityDaoImplTest {
    private static ActivityDao activityDao;
    private static Connection connection;

    @BeforeAll
    public static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
        activityDao = new ActivityDaoImpl(connection);
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
     * @see ActivityDaoImpl#read(Long)
     */
    @Test
    void testReadActivity() throws DaoException, SQLException {
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO activities VALUES (DEFAULT, 'Activity1', 'Активність1', 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new UnsupportedOperationException("Fatal: Cannot create the test for activityDao. The test is crashed");
        }
        expectedActivity.setId(rs.getLong(1));
        Activity actualActivity = activityDao.read(expectedActivity.getId());
        assertEquals(expectedActivity, actualActivity);
    }

    /**
     * @see ActivityDaoImpl#read(Long)
     */
    @Test
    void testReadActivityShouldThrownExceptionWhenActivityIsMissing() {
        assertThrows(DaoException.class, () -> activityDao.read(0L));
        assertThrows(DaoException.class, () -> activityDao.read(-1L));
    }

    /**
     * @see ActivityDaoImpl#create(Activity)
     */
    @Test
    void testCreateActivity() throws DaoException {
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        long rowsBeforeCreate = getCountRowsFromTable();
        activityDao.create(expectedActivity); // And now expected has synchronized 'id' with db
        Activity actualActivity = activityDao.read(expectedActivity.getId());
        assertEquals(expectedActivity, actualActivity);
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate + 1, rowsAfterCreate);
    }

    /**
     * @see ActivityDaoImpl#create(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenActivityIsNotUniqueRowInTable")
    void testCreateActivityShouldThrowExceptionWhenActivityIsDuplicated(Activity duplicatedActivity) throws DaoException {
        Activity testActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(testActivity);
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> activityDao.create(duplicatedActivity));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see ActivityDaoImpl#create(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenActivityIsNull")
    void testCreateActivityShouldThrowExceptionWhenActivityIsNull(Activity expectedActivity) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> activityDao.create(expectedActivity));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see ActivityDaoImpl#create(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenActivityHasNegativeOrZeroField")
    void testCreateActivityShouldThrowExceptionWhenActivityHasNegativeOrZeroField(Activity testActivity) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> activityDao.create(testActivity));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see ActivityDaoImpl#update(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenActivityIsChanged")
    void testUpdateActivity(Activity changedActivity) throws DaoException {
        Activity sourceActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(sourceActivity); // And now source has synchronized 'id' with db
        sourceActivity.setNameEn(changedActivity.getNameEn());
        sourceActivity.setNameUk(changedActivity.getNameUk());
        sourceActivity.setCategoryId(changedActivity.getCategoryId());

        activityDao.update(sourceActivity);
        Activity actualActivity = activityDao.read(sourceActivity.getId());
        assertEquals(sourceActivity, actualActivity);
    }

    /**
     * @see ActivityDaoImpl#update(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUpdatedActivityIsNotUniqueRowInTable")
    void testUpdateActivityShouldThrowExceptionWhenActivityIsDuplicated(Activity duplicatedActivity) throws DaoException {
        activityDao.create(new Activity("Activity2", "Активність2", 1)); // fill additional test row
        Activity sourceActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(sourceActivity);
        sourceActivity.setNameEn(duplicatedActivity.getNameEn());
        sourceActivity.setNameUk(duplicatedActivity.getNameUk());
        sourceActivity.setCategoryId(duplicatedActivity.getCategoryId());
        assertThrows(DaoException.class, () -> activityDao.update(sourceActivity));
    }

    /**
     * @see ActivityDaoImpl#update(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenActivityIsNull")
    void testUpdateActivityShouldThrowExceptionWhenActivityIsNull(Activity activityHasNullField) throws DaoException {
        Activity sourceActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(sourceActivity);
        sourceActivity.setNameEn(activityHasNullField.getNameEn());
        sourceActivity.setNameUk(activityHasNullField.getNameUk());
        sourceActivity.setCategoryId(activityHasNullField.getCategoryId());
        assertThrows(DaoException.class, () -> activityDao.update(sourceActivity));
    }

    /**
     * @see ActivityDaoImpl#update(Activity)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenActivityHasNegativeOrZeroField")
    void testUpdateRequestShouldThrowExceptionWhenActivityHasNegativeOrZeroField(Activity wrongFieldActivity) throws DaoException {
        Activity sourceActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(sourceActivity);
        sourceActivity.setNameEn(wrongFieldActivity.getNameEn());
        sourceActivity.setNameUk(wrongFieldActivity.getNameUk());
        sourceActivity.setCategoryId(wrongFieldActivity.getCategoryId());
        assertThrows(DaoException.class, () -> activityDao.update(sourceActivity));
    }

    /**
     * @see ActivityDaoImpl#delete(Long)
     */
    @Test
    void testDeleteActivity() throws DaoException, SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO activities VALUES (DEFAULT, 'Activity1', 'Активність1', 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test for activityDao. The test is crashed");
        }
        long rowsBeforeDelete = getCountRowsFromTable();
        activityDao.delete(rs.getLong(1));
        long rowsAfterDelete = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> activityDao.read(rs.getLong(1)));
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    /**
     * @see ActivityDaoImpl#delete(Long)
     */
    @Test
    void testDeleteActivityShouldReturnFalseWhenActivityIsMissing() throws DaoException {
        assertFalse(activityDao.delete(0L));
        assertFalse(activityDao.delete(-1L));
    }

    /**
     * @see ActivityDaoImpl#findAll()
     */
    @Test
    void testFindAllActivities() throws DaoException, SQLException {
        List<Activity> activityListBeforeAddedActivity = activityDao.findAll();
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO activities VALUES (DEFAULT, 'Activity1', 'Активність1', 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test for activityDao. The test is crashed");
        }
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        expectedActivity.setId(rs.getLong(1));

        List<Activity> activityListAfterAddedActivity = activityDao.findAll();
        List<Activity> differences = new LinkedList<>(activityListAfterAddedActivity);
        differences.removeAll(activityListBeforeAddedActivity);
        assertEquals(1, differences.size());
        assertEquals(expectedActivity, differences.get(0));
    }

    /**
     * @see ActivityDaoImpl#findAllActivitiesByCategory(long)
     */
    @Test
    void testFindAllActivitiesByCategoryId() throws DaoException {
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(expectedActivity);
        List<Activity> activityListByCategory = activityDao.findAllActivitiesByCategory(1);
        assertTrue(activityListByCategory.contains(expectedActivity));
        activityDao.delete(expectedActivity.getId());
        List<Activity> activityListByCategoryAfterDelete = activityDao.findAllActivitiesByCategory(1);
        assertFalse(activityListByCategoryAfterDelete.contains(expectedActivity));
        assertEquals(activityListByCategory.size() - 1, activityListByCategoryAfterDelete.size());
    }

    /**
     * @see ActivityDaoImpl#findAllActivitiesByCategory(String)
     */
    @Test
    void testFindAllActivitiesByCategoryName() throws DaoException, SQLException {
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(expectedActivity);

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT name_en FROM categories WHERE id = 1");
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test for activityDao. The test is crashed");
        }
        String categoryName = rs.getString("name_en");

        List<Activity> activityListByCategory = activityDao.findAllActivitiesByCategory(categoryName);
        assertTrue(activityListByCategory.contains(expectedActivity));
        activityDao.delete(expectedActivity.getId());
        List<Activity> activityListByCategoryAfterDelete = activityDao.findAllActivitiesByCategory(categoryName);
        assertFalse(activityListByCategoryAfterDelete.contains(expectedActivity));
        assertEquals(activityListByCategory.size() - 1, activityListByCategoryAfterDelete.size());
    }

    /**
     * @see ActivityDaoImpl#findAllActivitiesByUser(long)
     */
    @Test
    void testFindAllActivitiesByUserId() throws DaoException, SQLException {
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(expectedActivity);

        PreparedStatement pst = connection.prepareStatement("INSERT INTO users_activities VALUES (?, ?, false)");
        pst.setLong(1, 1);
        pst.setLong(2, expectedActivity.getId());
        pst.executeUpdate();

        List<Activity> activityListByUser = activityDao.findAllActivitiesByUser(1);
        assertTrue(activityListByUser.contains(expectedActivity));
        activityDao.delete(expectedActivity.getId());
        List<Activity> activityListByUserAfterDelete = activityDao.findAllActivitiesByUser(1);
        assertFalse(activityListByUserAfterDelete.contains(expectedActivity));
        assertEquals(activityListByUser.size() - 1, activityListByUserAfterDelete.size());
    }

    /**
     * @see ActivityDaoImpl#findAllActivitiesByUser(String)
     */
    @Test
    void testFindAllActivitiesByUserName() throws DaoException, SQLException {
        Activity expectedActivity = new Activity("Activity1", "Активність1", 1);
        activityDao.create(expectedActivity);

        PreparedStatement pst = connection.prepareStatement("INSERT INTO users_activities VALUES (?, ?, false)");
        pst.setLong(1, 1);
        pst.setLong(2, expectedActivity.getId());
        pst.executeUpdate();

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT login FROM users WHERE id = 1");
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test for activityDao. The test is crashed");
        }
        String login = rs.getString("login");

        List<Activity> activityListByCategory = activityDao.findAllActivitiesByUser(login);
        assertTrue(activityListByCategory.contains(expectedActivity));
        activityDao.delete(expectedActivity.getId());
        List<Activity> activityListByCategoryAfterDelete = activityDao.findAllActivitiesByUser(login);
        assertFalse(activityListByCategoryAfterDelete.contains(expectedActivity));
        assertEquals(activityListByCategory.size() - 1, activityListByCategoryAfterDelete.size());
    }

    private static Stream<Arguments> testCasesWhenActivityIsNotUniqueRowInTable() {
        return Stream.of(
                Arguments.of(new Activity("Activity1", "Активність1", 1)),
                Arguments.of(new Activity("Activity2", "Активність1", 1)),
                Arguments.of(new Activity("Activity1", "Активність2", 1))
        );
    }

    private static Stream<Arguments> testCasesWhenUpdatedActivityIsNotUniqueRowInTable() {
        return Stream.of(
                Arguments.of(new Activity("Activity2", "Активність1", 1)),
                Arguments.of(new Activity("Activity1", "Активність2", 1))
        );
    }

    private static Stream<Arguments> testCasesWhenActivityIsChanged() {
        return Stream.of(
                Arguments.of(new Activity("Activity1", "Активність1", 1)), // update cloned activity
                Arguments.of(new Activity("Activity1", "Активність1", 2)),
                Arguments.of(new Activity("updated_Activity1", "обновлена_Активність1", 1)),
                Arguments.of(new Activity("updated_Activity1", "обновлена_Активність1", 2)),
                Arguments.of(new Activity("updated_Activity1", "Активність1", 1)),
                Arguments.of(new Activity("Activity1", "обновлена_Активність1", 1))
        );
    }

    private static Stream<Arguments> testCasesWhenActivityIsNull() {
        return Stream.of(
                Arguments.of(new Activity(null, null, 1)),
                Arguments.of(new Activity("Активність1", null, 1)),
                Arguments.of(new Activity(null, "Активність1", 1))
        );
    }

    private static Stream<Arguments> testCasesWhenActivityHasNegativeOrZeroField() {
        return Stream.of(
                Arguments.of(new Activity("Activity2", "Активність2", 0)),
                Arguments.of(new Activity("Activity2", "Активність2", -1))
        );
    }

    private long getCountRowsFromTable() {
        long result = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM activities");
            if (rs.next()) {
                result = rs.getLong("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
