package ua.epam.akoreshev.finalproject.web.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.EditUserException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.exceptions.UserException;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.UserValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserService userService;
    private UserDao userDao;
    private UserValidator userValidator;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        userDao = Mockito.mock(UserDao.class);
        userValidator = Mockito.mock(UserValidator.class);
        userService = new UserServiceImpl(userDao, userValidator);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see UserServiceImpl#addUser(User, String)
     */
    @Test
    void testAddUserShouldReturnTrueWhenUserIsValid() throws DaoException, ServiceException, UserException {
        User testUser = new User(1000L, "testUser1", "testUser1@email.com", "testPass1", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(true);
        Mockito.when(userValidator.validateRoleId(testUser.getRoleId())).thenReturn(true);
        Mockito.when(userValidator.validate(testUser)).thenReturn(true);
        Mockito.when(userDao.create(testUser)).thenReturn(true);
        assertTrue(userService.addUser(testUser, testUser.getPassword()));
    }

    /**
     * @see UserServiceImpl#addUser(User, String)
     */
    @Test
    void testAddUserShouldThrowExceptionWhenConfirmPasswordIsInvalid() throws DaoException {
        User testUser = new User(1000L, "testUser1", "testUser1@email.com", "testPass1", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(true);
        Mockito.when(userValidator.validateRoleId(testUser.getRoleId())).thenReturn(true);
        Mockito.when(userValidator.validate(testUser)).thenReturn(true);
        Mockito.when(userDao.create(testUser)).thenReturn(true);
        assertThrows(UserException.class, () -> userService.addUser(testUser, null));
        assertThrows(UserException.class, () -> userService.addUser(testUser, "testPass2"));
    }

    /**
     * @see UserServiceImpl#addUser(User, String)
     */
    @Test
    void testAddUserShouldReturnFalseWhenUserInvalid() throws DaoException, ServiceException, UserException {
        User testUser = new User();
        testUser.setPassword("testPassword");
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(false);
        Mockito.when(userValidator.validateRoleId(testUser.getRoleId())).thenReturn(false);
        Mockito.when(userValidator.validate(testUser)).thenReturn(false);
        Mockito.when(userDao.create(testUser)).thenReturn(true);
        assertFalse(userService.addUser(testUser, testUser.getPassword()));
    }

    /**
     * @see UserServiceImpl#addUser(User, String)
     */
    @Test
    void testAddUserShouldThrowExceptionWhenCannotCreateUserAtDatabase() throws DaoException {
        User testUser = new User();
        testUser.setPassword("testPassword");
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(true);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userValidator.validateRoleId(testUser.getRoleId())).thenReturn(true);
        Mockito.when(userValidator.validate(testUser)).thenReturn(true);
        Mockito.when(userDao.create(testUser)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.addUser(testUser, testUser.getPassword()));
    }

    /**
     * @see UserServiceImpl#findUserByLoginAndPassword(String, String)
     */
    @Test
    void testFindUserByLoginAndPasswordShouldReturnUser() throws DaoException, ServiceException, UserException {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getLogin())).thenReturn(testUser);
        assertEquals(testUser, userService.findUserByLoginAndPassword(testUser.getLogin(), testUser.getPassword()));
    }

    /**
     * @see UserServiceImpl#findUserByLoginAndPassword(String, String)
     */
    @Test
    void testFindUserByLoginAndPasswordShouldThrowExceptionWhenUserNotExists() throws DaoException {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getLogin())).thenReturn(null);
        assertThrows(UserException.class,
                () -> userService.findUserByLoginAndPassword(testUser.getLogin(), testUser.getPassword()));
    }

    /**
     * @see UserServiceImpl#findUserByLoginAndPassword(String, String)
     */
    @Test
    void testFindUserByLoginAndPasswordShouldThrowExceptionWhenWrongPairLoginAndPassword() throws DaoException {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getLogin()))
                .thenReturn(new User(1, "testUser1", "testUser1@mail.com", "Wrong_password", 2));
        assertThrows(UserException.class,
                () -> userService.findUserByLoginAndPassword(testUser.getLogin(), testUser.getPassword()));
        Mockito.when(userDao.read(testUser.getLogin())).thenReturn(testUser);
        assertThrows(UserException.class,
                () -> userService.findUserByLoginAndPassword(testUser.getLogin(), "Wrong_password"));
    }

    /**
     * @see UserServiceImpl#findUserByLoginAndPassword(String, String)
     */
    @Test
    void testFindUserByLoginAndPasswordShouldThrowExceptionWhenUserIsInvalid() throws DaoException {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(false);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getLogin())).thenReturn(testUser);
        assertThrows(UserException.class,
                () -> userService.findUserByLoginAndPassword(testUser.getLogin(), testUser.getPassword()));

        User testUser2 = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser2.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser2.getPassword())).thenReturn(false);
        Mockito.when(userDao.read(testUser2.getLogin())).thenReturn(testUser2);
        assertThrows(UserException.class,
                () -> userService.findUserByLoginAndPassword(testUser2.getLogin(), testUser2.getPassword()));
    }

    /**
     * @see UserServiceImpl#findUserByLoginAndPassword(String, String)
     */
    @Test
    void testFindUserByLoginAndPasswordShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validatePassword(testUser.getPassword())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getLogin())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> userService.findUserByLoginAndPassword(testUser.getLogin(), testUser.getPassword()));
    }

    /**
     * @see UserServiceImpl#getNumberUsers()
     */
    @Test
    void testGetNumberUsersShouldReturnLong() throws DaoException, ServiceException {
        Mockito.when(userDao.getNumberUsers()).thenReturn(10L);
        assertEquals(10L, userService.getNumberUsers());
    }

    /**
     * @see UserServiceImpl#getNumberUsers()
     */
    @Test
    void testGetNumberUsersShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(userDao.getNumberUsers()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getNumberUsers());
    }

    /**
     * @see UserServiceImpl#getUsers(int, int, String, String)
     */
    @Test
    void testGetUsersShouldReturnSortedListWithLimitedSize() throws DaoException, ServiceException {
        int limit = 5;
        List<User> users = IntStream.range(0, limit)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        Mockito.when(userDao.findAllUsers(limit, 0, "login", "ASC")).thenReturn(users);
        assertEquals(users, userService.getUsers(limit, 0, "login", "ASC"));

        users.sort(Comparator.comparing(User::getEmail).reversed());
        Mockito.when(userDao.findAllUsers(limit, 0, "email", "DESC")).thenReturn(users);
        assertEquals(users, userService.getUsers(limit, 0, "email", "DESC"));
    }

    /**
     * @see UserServiceImpl#getUsers(int, int, String, String)
     */
    @Test
    void testGetUsersShouldReturnThrowExceptionWhenDaoThrowException() throws DaoException {
        int limit = 5;
        Mockito.when(userDao.findAllUsers(limit, 0, "login", "ASC")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getUsers(limit, 0, "login", "ASC"));
    }

    /**
     * @see UserServiceImpl#removeUser(long)
     */
    @Test
    void testRemoveUserShouldReturnTrueWhenUserSuccessfullyRemoved() throws DaoException, ServiceException {
        User testUser = new User();
        testUser.setId(1000L);
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(true);
        Mockito.when(userDao.delete(testUser.getId())).thenReturn(true);
        assertTrue(userService.removeUser(testUser.getId()));
    }

    /**
     * @see UserServiceImpl#removeUser(long)
     */
    @Test
    void testRemoveUserShouldReturnFalseWhenUserInvalidOrNotExists() throws DaoException, ServiceException {
        User testUser = new User();
        testUser.setId(1000L);
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(false);
        Mockito.when(userDao.delete(testUser.getId())).thenReturn(true);
        assertFalse(userService.removeUser(testUser.getId()));

        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(true);
        Mockito.when(userDao.delete(testUser.getId())).thenReturn(false);
        assertFalse(userService.removeUser(testUser.getId()));
    }

    /**
     * @see UserServiceImpl#removeUser(long)
     */
    @Test
    void testRemoveUserShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        User testUser = new User();
        testUser.setId(1000L);
        Mockito.when(userValidator.validateId(testUser.getId())).thenReturn(true);
        Mockito.when(userDao.delete(testUser.getId())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.removeUser(testUser.getId()));
    }

    /**
     * @see UserServiceImpl#editUser(User)
     */
    @Test
    void testEditUserShouldReturnTrueWhenUserSuccessfullyEdited() throws DaoException, ServiceException, EditUserException {
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertTrue(userService.editUser(testUser));
    }

    /**
     * @see UserServiceImpl#editUser(User)
     */
    @Test
    void testEditUserShouldReturnFalseWhenUserDaoReturnFalse() throws DaoException, ServiceException, EditUserException {
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenReturn(false);
        assertFalse(userService.editUser(testUser));
    }

    /**
     * @see UserServiceImpl#editUser(User)
     */
    @Test
    void testEditUserShouldThrowExceptionWhenUserNotExists() throws DaoException {
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(null);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class, () -> userService.editUser(testUser));
    }

    /**
     * @see UserServiceImpl#editUser(User)
     */
    @Test
    void testEditUserShouldThrowExceptionWhenUserIsInvalid() throws DaoException {
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(false);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class, () -> userService.editUser(testUser));

        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(false);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class, () -> userService.editUser(testUser));
    }

    /**
     * @see UserServiceImpl#editUser(User)
     */
    @Test
    void testEditUserShouldThrowExceptionWhenUserDuplicated() throws DaoException {
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        User testUser = users.get(0);
        testUser.setLogin(users.get(9).getLogin());
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class, () -> userService.editUser(testUser));
    }

    /**
     * @see UserServiceImpl#editUser(User)
     */
    @Test
    void testEditUserShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> new User(i, "email" + i, "login" + i, i + "", i))
                .collect(Collectors.toList());
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "testPassword", 2);
        Mockito.when(userValidator.validateLogin(testUser.getLogin())).thenReturn(true);
        Mockito.when(userValidator.validateEmail(testUser.getEmail())).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.findAll()).thenReturn(users);
        Mockito.when(userDao.update(testUser)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.editUser(testUser));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldReturnTrueWhenUserSuccessfullyEdited() throws DaoException, ServiceException, EditUserException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertTrue(userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldReturnFalseWhenUserDaoReturnFalse() throws DaoException, ServiceException, EditUserException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(false);
        assertFalse(userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenUserNotExists() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(null);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenCurrentPasswordIsWrong() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", "wrongCurrentPassword", 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenCurrentPasswordAreInvalid() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(false);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenNewPasswordAreInvalid() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(false);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenConfirmedNewPasswordAreInvalid() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(false);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenDifferentConfirmPassword() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "differentPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenCurrentAndNewPasswordsAreEquals() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "currentPassword";
        String confirmedNewPassword = "newPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenReturn(true);
        assertThrows(EditUserException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }

    /**
     * @see UserServiceImpl#changePassword(long, String, String, String)
     */
    @Test
    void testChangePasswordShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmedNewPassword = "newPassword";
        Mockito.when(userValidator.validatePassword(currentPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(newPassword)).thenReturn(true);
        Mockito.when(userValidator.validatePassword(confirmedNewPassword)).thenReturn(true);
        User testUser = new User(1, "testUser1", "testUser1@mail.com", currentPassword, 2);
        Mockito.when(userDao.read(testUser.getId())).thenReturn(testUser);
        Mockito.when(userDao.update(testUser)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> userService.changePassword(userId, currentPassword, newPassword, confirmedNewPassword));
    }
}