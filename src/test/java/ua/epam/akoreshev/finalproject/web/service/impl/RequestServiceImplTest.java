package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.RequestException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.RequestDao;
import ua.epam.akoreshev.finalproject.model.entity.Request;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityRequest;
import ua.epam.akoreshev.finalproject.web.service.RequestService;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceImplTest {
    public static final long ADD_ACTIVITY_CODE = 1;
    public static final int APPROVE_STATUS_CODE = 2;
    public static final int DENY_STATUS_CODE = 3;
    private RequestService requestService;
    private RequestDao requestDao;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        requestDao = Mockito.mock(RequestDao.class);
        requestService = new RequestServiceImpl(requestDao);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see RequestServiceImpl#createRequest(Request)
     */
    @Test
    void testCreateRequestShouldReturnTrueWhenRequestSuccessfullyCreated() throws DaoException, ServiceException, RequestException {
        Request req = new Request(1L, 1L, 1L, 1L, 1L);
        Mockito.when(requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId()))
                .thenReturn(null);
        Mockito.when(requestDao.create(req)).thenReturn(true);
        assertTrue(requestService.createRequest(req));
    }

    /**
     * @see RequestServiceImpl#createRequest(Request)
     */
    @Test
    void testCreateRequestShouldReturnFalseWhenCannotCreateRequest() throws DaoException, ServiceException, RequestException {
        Request req = new Request(1L, 1L, 1L, 1L, 1L);
        Mockito.when(requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId()))
                .thenReturn(null);
        Mockito.when(requestDao.create(req)).thenReturn(false);
        assertFalse(requestService.createRequest(req));
    }

    /**
     * @see RequestServiceImpl#createRequest(Request)
     */
    @Test
    void testCreateRequestShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Request req = new Request(1L, 1L, 1L, 1L, 1L);
        Mockito.when(requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId()))
                .thenReturn(null);
        Mockito.when(requestDao.create(req)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> requestService.createRequest(req));
    }


    /**
     * @see RequestServiceImpl#createRequest(Request)
     */
    @Test
    void testCreateRequestShouldThrowExceptionWhenRequestIsInvalidOrDuplicate() throws DaoException {
        Request req = new Request(1L, 1L, 1L, 1L, 1L);
        Mockito.when(requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId()))
                .thenReturn(req);
        Mockito.when(requestDao.create(req)).thenReturn(true);
        assertThrows(RequestException.class, () -> requestService.createRequest(req));

        Mockito.when(requestDao.read(req.getUserId(), req.getActivityId(), req.getTypeId(), req.getStatusId()))
                .thenReturn(null);
        Mockito.when(requestDao.create(req))
                .thenThrow(new DaoException("foreign key constraint",
                        new SQLException(),
                        MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2));
        assertThrows(RequestException.class, () -> requestService.createRequest(req));
    }


    /**
     * @see RequestServiceImpl#findRequestsByStatuses(int, int, String, String, int[])
     */
    @Test
    void testFindRequestsByStatusesShouldReturnSortedList() throws DaoException, ServiceException {
        List<UserActivityRequest> userActivityRequests = new LinkedList<>();
        Mockito.when(requestDao.findAllRequestsByStatuses(5, 0, "status.id", "DESC", new int[5]))
                .thenReturn(userActivityRequests);
        assertEquals(userActivityRequests,
                requestService.findRequestsByStatuses(5, 0, "status.id", "DESC", new int[5]));
    }

    /**
     * @see RequestServiceImpl#findRequestsByStatuses(int, int, String, String, int[])
     */
    @Test
    void testFindRequestsByStatusesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(requestDao.findAllRequestsByStatuses(5, 0, "status.id", "DESC", new int[5]))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () ->
                requestService.findRequestsByStatuses(5, 0, "status.id", "DESC", new int[5]));
    }

    /**
     * @see RequestServiceImpl#getCountRequestsByStatuses(int...)
     */
    @Test
    void testGetCountRequestsByStatusesShouldReturnNumber() throws DaoException, ServiceException {
        int[] statuses = new int[5];
        int expected = 5;
        Mockito.when(requestDao.getCountRowsByStatuses(statuses)).thenReturn(expected);
        assertEquals(expected, requestService.getCountRequestsByStatuses(statuses));
    }

    /**
     * @see RequestServiceImpl#getCountRequestsByStatuses(int...)
     */
    @Test
    void testGetCountRequestsByStatusesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        int[] statuses = new int[5];
        Mockito.when(requestDao.getCountRowsByStatuses(statuses)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> requestService.getCountRequestsByStatuses(statuses));
    }

    /**
     * @see RequestServiceImpl#approveRequest(long, long, long, long)
     */
    @Test
    void testApproveRequestShouldReturnTrueWhenRequestSuccessfullyApproved() throws DaoException, ServiceException, RequestException {
        Mockito.when(requestDao.approveAddActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenReturn(true);
        assertTrue(requestService.approveRequest(1L, 0L, 1L, ADD_ACTIVITY_CODE));

        Mockito.when(requestDao.approveRemovingActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenReturn(true);
        assertTrue(requestService.approveRequest(1L, 0L, 1L, ADD_ACTIVITY_CODE - 1L));
    }

    /**
     * @see RequestServiceImpl#approveRequest(long, long, long, long)
     */
    @Test
    void testApproveRequestShouldReturnFalseWhenCannotApproveRequest() throws DaoException, ServiceException, RequestException {
        Mockito.when(requestDao.approveAddActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L)).thenReturn(false);
        assertFalse(requestService.approveRequest(1L, 0, 1L, ADD_ACTIVITY_CODE));

        Mockito.when(requestDao.approveRemovingActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenReturn(false);
        assertFalse(requestService.approveRequest(1L, 0, 1L, ADD_ACTIVITY_CODE - 1L));
    }

    /**
     * @see RequestServiceImpl#approveRequest(long, long, long, long)
     */
    @Test
    void testApproveRequestShouldThrowExceptionWhenRequestDuplicated() throws DaoException {
        Mockito.when(requestDao.approveAddActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenThrow(new DaoException("duplicate exception", new SQLException(), MysqlErrorNumbers.ER_DUP_ENTRY));
        assertThrows(RequestException.class,
                () -> requestService.approveRequest(1L, 0L, 1L, ADD_ACTIVITY_CODE));
    }

    /**
     * @see RequestServiceImpl#approveRequest(long, long, long, long)
     */
    @Test
    void testApproveRequestShouldThrowExceptionWhenRequestHasOutOfRangeField() throws DaoException {
        Mockito.when(requestDao.approveAddActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenThrow(new DaoException("out-of-range exception",
                        new SQLException(),
                        MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE));
        assertThrows(RequestException.class,
                () -> requestService.approveRequest(1L, 0L, 1L, ADD_ACTIVITY_CODE));
    }

    /**
     * @see RequestServiceImpl#approveRequest(long, long, long, long)
     */
    @Test
    void testApproveRequestShouldThrowExceptionWhenRequestIsInvalid() throws DaoException {
        Mockito.when(requestDao.approveAddActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenThrow(new DaoException("foreign key constraint",
                        new SQLException(),
                        MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2));
        assertThrows(RequestException.class,
                () -> requestService.approveRequest(1L, 0L, 1L, ADD_ACTIVITY_CODE));
    }

    /**
     * @see RequestServiceImpl#approveRequest(long, long, long, long)
     */
    @Test
    void testApproveRequestShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(requestDao.approveAddActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> requestService.approveRequest(1L, 0L, 1L, ADD_ACTIVITY_CODE));

        Mockito.reset(requestDao);
        Mockito.when(requestDao.approveRemovingActivityRequest(1L, APPROVE_STATUS_CODE, 0L, 1L))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> requestService.approveRequest(1L, 0L, 1L, 0L));
    }

    /**
     * @see RequestServiceImpl#rejectRequest(long)
     */
    @Test
    void testRejectRequestShouldReturnTrueWhenRequestSuccessfullyRejected() throws DaoException, ServiceException, RequestException {
        Mockito.when(requestDao.updateRequestStatus(0L, DENY_STATUS_CODE)).thenReturn(true);
        assertTrue(requestService.rejectRequest(0L));
    }

    /**
     * @see RequestServiceImpl#rejectRequest(long)
     */
    @Test
    void testRejectRequestShouldReturnFalseWhenCannotRejectRequest() throws DaoException, ServiceException, RequestException {
        Mockito.when(requestDao.updateRequestStatus(0L, DENY_STATUS_CODE)).thenReturn(false);
        assertFalse(requestService.rejectRequest(0L));
    }

    /**
     * @see RequestServiceImpl#rejectRequest(long)
     */
    @Test
    void testRejectRequestShouldThrowExceptionWhenRequestIsInvalid() throws DaoException {
        Mockito.when(requestDao.updateRequestStatus(0L, DENY_STATUS_CODE))
                .thenThrow(new DaoException("foreign key constraint",
                        new SQLException(),
                        MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2));
        assertThrows(RequestException.class, () -> requestService.rejectRequest(0L));
    }

    /**
     * @see RequestServiceImpl#rejectRequest(long)
     */
    @Test
    void testRejectRequestShouldThrowExceptionWhenRequestHasOutOfRangeField() throws DaoException {
        Mockito.when(requestDao.updateRequestStatus(0L, DENY_STATUS_CODE))
                .thenThrow(new DaoException("out-of-range exception",
                        new SQLException(),
                        MysqlErrorNumbers.ER_WARN_DATA_OUT_OF_RANGE));
        assertThrows(RequestException.class, () -> requestService.rejectRequest(0L));
    }

    /**
     * @see RequestServiceImpl#rejectRequest(long)
     */
    @Test
    void testRejectRequestShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(requestDao.updateRequestStatus(0L, DENY_STATUS_CODE)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> requestService.rejectRequest(0L));
    }
}