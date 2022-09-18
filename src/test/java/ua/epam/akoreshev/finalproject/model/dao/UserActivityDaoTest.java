package ua.epam.akoreshev.finalproject.model.dao;

import org.junit.jupiter.api.*;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.impl.UserActivityDaoImpl;
import ua.epam.akoreshev.finalproject.model.entity.UserActivity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UserActivityDaoTest {
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
     * @see UserActivityDaoImpl#delete(Long)
     */
    @Test
    void testDelete() {
        long randomLong = new Random().nextLong();
        assertThrows(UnsupportedOperationException.class, () -> userActivityDao.delete(randomLong));
    }

    /**
     * @see UserActivityDaoImpl#create(UserActivity)
     */
    @Test
    void testRead() {
        long randomLong = new Random().nextLong();
        assertThrows(UnsupportedOperationException.class, () -> userActivityDao.delete(randomLong));
    }
}