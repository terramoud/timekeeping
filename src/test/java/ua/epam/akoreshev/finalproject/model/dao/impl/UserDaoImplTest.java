package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest {
    private static UserDao userDao;
    private static Connection connection;

    @BeforeAll
    public static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
        userDao = new UserDaoImpl(connection);
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
     * @see UserDaoImpl#read(Long)
     */
    @Test
    void testReadUser() throws DaoException, SQLException {
        User expectedUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, 'User1', 'test1@test.com', 'passtest', 2)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            expectedUser.setId(rs.getLong(1));
        }
        User actualUser = userDao.read(expectedUser.getId());
        assertEquals(expectedUser, actualUser);
    }

    /**
     * @see UserDaoImpl#read(String)
     */
    @Test
    void testReadUserByLogin() throws DaoException, SQLException {
        User expectedUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, 'User1', 'test1@test.com', 'passtest', 2)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            expectedUser.setId(rs.getLong(1));
        }
        User actualUser = userDao.read(expectedUser.getLogin());
        assertEquals(expectedUser, actualUser);
    }

    /**
     * @see UserDaoImpl#read(String)
     */
    @Test
    void testReadUserShouldReturnNullWhenUserIsWrong() throws DaoException {
        assertNull(userDao.read(-1L));
        assertNull(userDao.read(Long.MAX_VALUE));
    }

    /**
     * @see UserDaoImpl#read(String)
     */
    @Test
    void testReadUserShouldReturnNullWhenUserIsMissing() throws DaoException {
        assertNull(userDao.read((String) null));
        assertNull(userDao.read(""));
        assertNull(userDao.read(0L));
    }

    /**
     * @see UserDaoImpl#create(User)
     */
    @Test
    void testCreateUser() throws DaoException {
        User expectedUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        long rowsBeforeCreate = getCountRowsFromTable();
        userDao.create(expectedUser); // And now expected has synchronized 'id' with db
        User actualUser = userDao.read(expectedUser.getId());
        assertEquals(expectedUser, actualUser);
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate + 1, rowsAfterCreate);
    }

    /**
     * @see UserDaoImpl#create(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserIsNotUniqueRowInTable")
    void testCreateUserShouldThrowExceptionWhenUserIsDuplicated(User duplicatedUser) throws DaoException {
        User testUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        userDao.create(testUser);
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> userDao.create(duplicatedUser));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see UserDaoImpl#create(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserIsNull")
    void testCreateUserShouldThrowExceptionWhenUserIsNull(User expectedUser) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> userDao.create(expectedUser));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see UserDaoImpl#create(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserHasNegativeOrZeroField")
    void testCreateUserShouldThrowExceptionWhenUserHasNegativeOrZeroField(User testUser) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> userDao.create(testUser));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see UserDaoImpl#update(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserIsChanged")
    void testUpdateUser(User changedUser) throws DaoException {
        User sourceUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        userDao.create(sourceUser); // And now source has synchronized 'id' with db
        sourceUser.setLogin(changedUser.getLogin());
        sourceUser.setEmail(changedUser.getEmail());
        sourceUser.setPassword(changedUser.getPassword());
        sourceUser.setRoleId(changedUser.getRoleId());

        userDao.update(sourceUser);
        User actualUser = userDao.read(sourceUser.getId());
        assertEquals(sourceUser, actualUser);
    }

    /**
     * @see UserDaoImpl#update(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUpdatedUserIsNotUniqueRowInTable")
    void testUpdateUserShouldThrowExceptionWhenUserIsDuplicated(User duplicatedUser) throws DaoException {
        User sourceUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        User testUser = new User(0, "User2", "test2@test.com", "passtest", 2);
        userDao.create(sourceUser);
        userDao.create(testUser);
        sourceUser.setLogin(duplicatedUser.getLogin());
        sourceUser.setEmail(duplicatedUser.getEmail());
        sourceUser.setPassword(duplicatedUser.getPassword());
        sourceUser.setRoleId(duplicatedUser.getRoleId());
        assertThrows(DaoException.class, () -> userDao.update(sourceUser));
    }

    /**
     * @see UserDaoImpl#update(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserIsNull")
    void testUpdateUserShouldThrowExceptionWhenUserIsNull(User userHasNullField) throws DaoException {
        User sourceUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        userDao.create(sourceUser);
        sourceUser.setLogin(userHasNullField.getLogin());
        sourceUser.setEmail(userHasNullField.getEmail());
        sourceUser.setPassword(userHasNullField.getPassword());
        sourceUser.setRoleId(userHasNullField.getRoleId());
        assertThrows(DaoException.class, () -> userDao.update(sourceUser));
    }

    /**
     * @see UserDaoImpl#update(User)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUserHasNegativeOrZeroField")
    void testUpdateRequestShouldThrowExceptionWhenUserHasNegativeOrZeroField(User testUser) throws DaoException {
        User sourceUser = new User(0, "User1", "test1@test.com", "passtest", 2);
        userDao.create(sourceUser);
        sourceUser.setLogin(testUser.getLogin());
        sourceUser.setEmail(testUser.getEmail());
        sourceUser.setPassword(testUser.getPassword());
        sourceUser.setRoleId(testUser.getRoleId());
        assertThrows(DaoException.class, () -> userDao.update(sourceUser));
    }

    /**
     * @see UserDaoImpl#delete(Long)
     */
    @Test
    void testDeleteUser() throws DaoException, SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, 'User1', 'test1@test.com', 'passtest', 2)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: Cannot create the test user in db. The test is crashed");
        }
        long rowsBeforeDelete = getCountRowsFromTable();
        userDao.delete(rs.getLong(1));
        long rowsAfterDelete = getCountRowsFromTable();
        assertNull(userDao.read(rs.getLong(1)));
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    /**
     * @see UserDaoImpl#delete(Long)
     */
    @Test
    void testDeleteUserShouldReturnFalseWhenUserIsMissing() throws DaoException {
        assertFalse(userDao.delete(0L));
        assertFalse(userDao.delete(-1L));
    }

    /**
     * @see UserDaoImpl#findAll()
     */
    @Test
    void testFindAllUsers() throws DaoException, SQLException {
        List<User> userListBeforeAddedUser = userDao.findAll();
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, 'User1', 'test1@test.com', 'passtest', 2)",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        long generatedId = 0;
        if (rs.next()) {
            generatedId = rs.getLong(1);
        }
        User expectedUser = new User(generatedId, "User1", "test1@test.com", "passtest", 2);
        List<User> userListAfterAddedUser = userDao.findAll();
        List<User> differences = new LinkedList<>(userListAfterAddedUser);
        differences.removeAll(userListBeforeAddedUser);
        assertEquals(1, differences.size());
        assertEquals(expectedUser, differences.get(0));
    }

    private static Stream<Arguments> testCasesWhenUserIsNotUniqueRowInTable() {
        return Stream.of(
                Arguments.of(new User(0, "User1", "test1@test.com", "passtest", 2)),
                Arguments.of(new User(0, "User1", "test2@test.com", "passtest", 2)),
                Arguments.of(new User(0, "User2", "test1@test.com", "passtest", 2))
        );
    }

    private static Stream<Arguments> testCasesWhenUpdatedUserIsNotUniqueRowInTable() {
        return Stream.of(
                Arguments.of(new User(0, "User2", "test1@test.com", "passtest", 2)),
                Arguments.of(new User(0, "User1", "test2@test.com", "passtest", 2))
        );
    }

    private static Stream<Arguments> testCasesWhenUserIsChanged() {
        return Stream.of(
                Arguments.of(new User(0, "User1", "test1@test.com", "passtest", 2)), // update cloned user
                Arguments.of(new User(0, "updated_User1", "test1@test.com", "passtest", 2)),
                Arguments.of(new User(0, "User1", "updated_test1@test.com", "passtest", 2)),
                Arguments.of(new User(0, "User1", "test1@test.com", "updated_passtest", 2)),
                Arguments.of(new User(0, "User1", "test1@test.com", "passtest", 1)),
                Arguments.of(new User(0, "updated_User1", "updated_test1@test.com", "updated_passtest", 1))
        );
    }

    private static Stream<Arguments> testCasesWhenUserIsNull() {
        return Stream.of(
                Arguments.of(new User(0, null, "test1@test.com", "passtest1", 2)),
                Arguments.of(new User(0, "User2", null, "passtest2", 2)),
                Arguments.of(new User(0, "User3", "test3@test.com", null, 2))
        );
    }

    private static Stream<Arguments> testCasesWhenUserHasNegativeOrZeroField() {
        return Stream.of(
                Arguments.of(new User(0, "User2", "test1@test.com", "passtest1", 0)),
                Arguments.of(new User(0, "User2", "test1@test.com", "passtest1", -1))
        );
    }

    private long getCountRowsFromTable() {
        long result = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM users");
            if (rs.next()) {
                result = rs.getLong("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
