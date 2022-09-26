package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RequestDaoImplTest {
    private static RequestDao requestDao;
    private static Connection connection;
    private static ActivityDao activityDao;
    private static UserDao userDao;

    @BeforeAll
    static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
        requestDao = new RequestDaoImpl(connection);
        activityDao = new ActivityDaoImpl(connection);
        userDao = new UserDaoImpl(connection);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @AfterAll
    static void tearDownBeforeAll() throws SQLException {
        connection.close();
    }

    /**
     * @see RequestDaoImpl#read(Long)
     */
    @Test
    void testReadRequest() throws DaoException, SQLException {
        Request expected = new Request(0, 1, 1, 1, 1);
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO requests VALUES (DEFAULT, 1, 1, 1, 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            expected.setId(rs.getLong(1));
        }
        Request actual = requestDao.read(expected.getId());
        assertEquals(expected, actual);
    }

    /**
     * @see RequestDaoImpl#read(Long)
     */
    @Test
    void testReadRequestShouldThrownExceptionWhenRequestIsMissing() {
        assertThrows(DaoException.class, () -> requestDao.read(0L));
        assertThrows(DaoException.class, () -> requestDao.read(-1L));
    }

    /**
     * @see RequestDaoImpl#create(Request)
     */
    @Test
    void testCreateRequest() throws DaoException {
        Request expected = new Request(0, 1, 1, 1, 1);
        long rowsBeforeCreate = getCountRowsFromTable();
        requestDao.create(expected); // And now expected has synchronized 'id' with db
        Request actual = requestDao.read(expected.getId());
        assertEquals(expected, actual);
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate + 1, rowsAfterCreate);
    }

    /**
     * @see RequestDaoImpl#create(Request)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenRequestHasNegativeOrZeroField")
    void testCreateRequestShouldThrowExceptionWhenRequestHasNegativeOrZeroField(Request testRequest) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> requestDao.create(testRequest));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }


    /**
     * @see RequestDaoImpl#update(Request)
     */
    @Test
    void testUpdateRequest() throws DaoException {
        Activity activity1 = new Activity("TestActivity1", "Тестова діяльність1", 1);
        Activity activity2 = new Activity("TestActivity2", "Тестова діяльність2", 1);
        activityDao.create(activity1);
        activityDao.create(activity2);
        User user1 = new User(0, "testUser1!", "test@mail.com1!", "*", 2);
        User user2 = new User(0, "testUser2!", "test@mail.com2!", "*", 2);
        userDao.create(user1);
        userDao.create(user2);

        List<Request> requestList = new LinkedList<>();
        requestList.add(new Request(0, user1.getId(), activity1.getId(), 1, 1));
        requestList.add(new Request(0, user2.getId(), activity1.getId(), 1, 1));
        requestList.add(new Request(0, user1.getId(), activity2.getId(), 1, 1));
        requestList.add(new Request(0, user1.getId(), activity1.getId(), 2, 1));
        requestList.add(new Request(0, user1.getId(), activity1.getId(), 1, 2));
        requestList.add(new Request(0, user2.getId(), activity2.getId(), 2, 2));

        Request sourceRequest = new Request(0, user1.getId(), activity1.getId(), 1, 1);
        requestDao.create(sourceRequest);
        for (Request changedRequest : requestList) {
            sourceRequest.setUserId(changedRequest.getUserId());
            sourceRequest.setActivityId(changedRequest.getActivityId());
            sourceRequest.setTypeId(changedRequest.getTypeId());
            sourceRequest.setStatusId(changedRequest.getStatusId());

            assertTrue(requestDao.update(sourceRequest));
            Request actualRequest = requestDao.read(sourceRequest.getId());
            assertEquals(sourceRequest, actualRequest);
        }
    }

    /**
     * @see RequestDaoImpl#update(Request)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenRequestHasNegativeOrZeroField")
    void testUpdateRequestShouldThrowExceptionWhenRequestHasNegativeOrZeroField(Request updatedRequest) throws DaoException {
        Request sourceRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(sourceRequest);
        sourceRequest.setUserId(updatedRequest.getUserId());
        sourceRequest.setActivityId(updatedRequest.getActivityId());
        sourceRequest.setTypeId(updatedRequest.getTypeId());
        sourceRequest.setStatusId(updatedRequest.getStatusId());
        assertThrows(DaoException.class, () -> requestDao.update(sourceRequest));
    }

    /**
     * @see RequestDaoImpl#updateRequestStatus(long, long)
     */
    @Test
    void testUpdateRequestStatus() throws DaoException {
        Request sourceRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(sourceRequest);
        requestDao.updateRequestStatus(sourceRequest.getId(), 2);
        sourceRequest.setStatusId(2); // expected updated status
        Request actualRequest = requestDao.read(sourceRequest.getId());
        assertEquals(sourceRequest, actualRequest);
    }

    /**
     * @see RequestDaoImpl#updateRequestStatus(long, long)
     */
    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    void updateRequestStatusShouldThrowExceptionWhenStatusLessThanZero(long wrongId) throws DaoException {
        Request sourceRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(sourceRequest);
        assertThrows(DaoException.class, () -> requestDao.updateRequestStatus(sourceRequest.getId(), wrongId));
    }

    /**
     * @see RequestDaoImpl#delete(Long)
     */
    @Test
    void testDeleteRequest() throws DaoException, SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO requests VALUES (DEFAULT, 1, 1, 1, 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the 'test request' in db. The test is crashed");
        }
        long rowsBeforeDelete = getCountRowsFromTable();
        requestDao.delete(rs.getLong(1));
        long rowsAfterDelete = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> requestDao.read(rs.getLong(1)));
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    /**
     * @see RequestDaoImpl#delete(Long)
     */
    @Test
    void testDeleteRequestShouldReturnFalseWhenRequestIsMissing() throws DaoException {
        assertFalse(requestDao.delete(0L));
        assertFalse(requestDao.delete(-1L));
    }

    /**
     * @see RequestDaoImpl#findAll()
     */
    @Test
    void testFindAllRequests() throws DaoException, SQLException {
        List<Request> requestsListBeforeAddedRequest = requestDao.findAll();
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO requests VALUES (DEFAULT, 1, 1, 1, 1)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        long generatedId = 0;
        if (rs.next()) {
            generatedId = rs.getLong(1);
        }

        Request expectedRequest = new Request(generatedId, 1, 1, 1, 1);
        List<Request> requestsListAfterAddedRequest = requestDao.findAll();
        List<Request> differences = new LinkedList<>(requestsListAfterAddedRequest);
        differences.removeAll(requestsListBeforeAddedRequest);
        assertEquals(1, differences.size());
        assertEquals(expectedRequest, differences.get(0));
    }

    static Stream<Arguments> testCasesWhenRequestHasNegativeOrZeroField() {
        return Stream.of(
                Arguments.of(new Request(0, 0, 1, 1, 1)),
                Arguments.of(new Request(0, 1, 0, 1, 1)),
                Arguments.of(new Request(0, 1, 1, 0, 1)),
                Arguments.of(new Request(0, 1, 1, 1, 0)),
                Arguments.of(new Request(0, 0, 0, 0, 0)),

                Arguments.of(new Request(0, -1, 1, 1, 1)),
                Arguments.of(new Request(0, 1, -1, 1, 1)),
                Arguments.of(new Request(0, 1, 1, -1, 1)),
                Arguments.of(new Request(0, 1, 1, 1, -1)),
                Arguments.of(new Request(0, -1, -1, -1, -1))
        );
    }

    private long getCountRowsFromTable() {
        long result = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM requests");
            if (rs.next()) {
                result = rs.getLong("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
