package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.entity.Request;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RequestDaoImplTest {
    private static RequestDao requestDao;
    private static Connection connection;

    @BeforeAll
    static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
        requestDao = new RequestDaoImpl(connection);
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
    @ParameterizedTest
    @MethodSource("testCasesWhenRequestIsChanged")
    void testUpdateRequest(Request changedRequest) throws DaoException {
        Request sourceRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(sourceRequest);
        sourceRequest.setUserId(changedRequest.getUserId());
        sourceRequest.setActivityId(changedRequest.getActivityId());
        sourceRequest.setTypeId(changedRequest.getTypeId());
        sourceRequest.setStatusId(changedRequest.getStatusId());

        requestDao.update(sourceRequest);
        Request actualRequest = requestDao.read(sourceRequest.getId());
        assertEquals(sourceRequest, actualRequest);
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
     * @see RequestDaoImpl#updateRequestStatus(long, String)
     */
    @Test
    void testUpdateRequestStatusName() throws DaoException, SQLException {
        Request sourceRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(sourceRequest);
        sourceRequest.setStatusId(2); // expected updated status
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT statuses.name_en FROM requests " +
                        "INNER JOIN statuses ON requests.status_id = statuses.id " +
                        "WHERE status_id = 2");
        if (!resultSet.next()) {
            throw new SQLException("Fatal: Cannot read status name from db. The test is crashed");
        }
        String newStatusName = resultSet.getString("name_en");
        requestDao.updateRequestStatus(sourceRequest.getId(), newStatusName);
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
     * @see RequestDaoImpl#updateRequestStatus(long, String)
     */
    @Test
    void updateRequestStatusShouldThrowExceptionWhenStatusIsNull() throws DaoException {
        Request sourceRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(sourceRequest);
        assertThrows(DaoException.class, () -> requestDao.updateRequestStatus(sourceRequest.getId(), null));
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

    /**
     * @see RequestDaoImpl#findAllRequestsByStatus(long)
     */
    @Test
    void testFindAllRequestsByStatusId() throws DaoException, SQLException {
        Request expectedRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(expectedRequest);

        List<Request> requestsListByStatusId = requestDao.findAllRequestsByStatus(1);
        assertTrue(requestsListByStatusId.contains(expectedRequest));

        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM requests WHERE status_id = 1 ORDER BY id DESC LIMIT 1");

        List<Request> requestsListByStatusIdAfterDelete = requestDao.findAllRequestsByStatus(1);
        assertFalse(requestsListByStatusIdAfterDelete.contains(expectedRequest));
        assertEquals(requestsListByStatusId.size() - 1, requestsListByStatusIdAfterDelete.size());
    }

    /**
     * @see RequestDaoImpl#findAllRequestsByStatus(String)
     */
    @Test
    void testFindAllRequestsByStatusName() throws DaoException, SQLException {
        Request expectedRequest = new Request(0, 1, 1, 1, 1);
        requestDao.create(expectedRequest);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT statuses.name_en FROM requests " +
                        "INNER JOIN statuses ON requests.status_id = statuses.id " +
                        "WHERE status_id = 1");
        if (!resultSet.next()) {
            throw new SQLException("Fatal: Cannot read status name from db. The test is crashed");
        }
        String statusName = resultSet.getString("name_en");
        List<Request> requestsListByStatusName = requestDao.findAllRequestsByStatus(statusName);
        assertTrue(requestsListByStatusName.contains(expectedRequest));

        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM requests WHERE status_id = 1 ORDER BY id DESC LIMIT 1");

        List<Request> requestsListByStatusNameAfterDelete = requestDao.findAllRequestsByStatus(1);
        assertFalse(requestsListByStatusNameAfterDelete.contains(expectedRequest));
        assertEquals(requestsListByStatusName.size() - 1, requestsListByStatusNameAfterDelete.size());
    }

    static Stream<Arguments> testCasesWhenRequestIsChanged() {
        return Stream.of(
                Arguments.of(new Request(0, 1, 1, 1, 1)), // update cloned request
                Arguments.of(new Request(0, 2, 1, 1, 1)),
                Arguments.of(new Request(0, 1, 2, 1, 1)),
                Arguments.of(new Request(0, 1, 1, 2, 1)),
                Arguments.of(new Request(0, 1, 1, 1, 2)),
                Arguments.of(new Request(0, 2, 2, 2, 2))
        );
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
