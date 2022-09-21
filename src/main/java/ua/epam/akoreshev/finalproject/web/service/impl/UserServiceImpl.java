package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.*;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.UserService;
import ua.epam.akoreshev.finalproject.web.utils.UserValidator;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    public static final String INVALID_USER_LOGIN = "Invalid user login: '{}'";
    public static final String USER_ERROR_INVALID_LOGIN = "user.error.invalid.login";

    private final UserDao userDao;
    private final UserValidator userValidator;

    public UserServiceImpl(UserDao userDao, UserValidator userValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
    }

    @Override
    public boolean addUser(User user, String passwordConfirm) throws ServiceException, UserException {
        try {
            if (!userValidator.validateLogin(user.getLogin())) {
                LOG.warn(INVALID_USER_LOGIN, user.getLogin());
                throw new UserException(USER_ERROR_INVALID_LOGIN);
            }
            if (!userValidator.validateEmail(user.getEmail())) {
                LOG.warn("Invalid user email: '{}'", user.getEmail());
                throw new UserException("user.error.invalid.email");
            }
            if (!userValidator.validatePassword(user.getPassword()))
                throw new UserException("user.error.invalid.password");
            if (!userValidator.validatePassword(passwordConfirm))
                throw new UserException("user.error.invalid.confirmed_password");
            if (!user.getPassword().equals(passwordConfirm))
                throw new UserException("user.error.different_new_and_confirmed_passwords");
            return userValidator.validate(user) && userDao.create(user);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
                throw new UserException("user.error.duplicate_user");
            LOG.error("Cannot add new user '{}'", user);
            throw new ServiceException("Cannot add new user", e);
        }
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) throws ServiceException, UserException {
        if (!userValidator.validateLogin(login)) {
            LOG.warn(INVALID_USER_LOGIN, login);
            throw new UserException(USER_ERROR_INVALID_LOGIN);
        }
        if (!userValidator.validatePassword(password))
            throw new UserException("user.error.invalid.new_password");
        try {
            User user = userDao.read(login);
            LOG.debug("Obtained user from db by login is: '{}'", user);
            if (user == null)
                throw new UserException("user.error.not_exists");
            if (!user.getPassword().equals(password))
                throw new UserException("user.error.invalid.password");
            return user;
        } catch (DaoException e) {
            LOG.error("Cannot find user by login: '{}' and password", login);
            throw new ServiceException("Cannot find user by login and password", e);
        }
    }

    @Override
    public int getNumberUsers() throws ServiceException {
        try {
            return userDao.getNumberUsers();
        } catch (DaoException e) {
            LOG.error("Cannot count all users");
            throw new ServiceException("Cannot count all users", e);
        }
    }

    @Override
    public List<User> getUsers(int limit, int offset, String columnName, String sortOrder) throws ServiceException {
        try {
            return userDao.findAllUsers(limit, offset, columnName, sortOrder);
        } catch (DaoException e) {
            LOG.error("Cannot find any users");
            throw new ServiceException("Cannot find any users", e);
        }
    }

    @Override
    public boolean removeUser(long userId) throws ServiceException {
        try {
            return userValidator.validateId(userId) && userDao.delete(userId);
        } catch (DaoException e) {
            LOG.error("Cannot remove user by id: '{}'", userId);
            throw new ServiceException("Cannot remove user", e);
        }
    }

    @Override
    public boolean editUser(User user) throws ServiceException, EditUserException {
        try {
            User userToEdit = userDao.read(user.getId());
            LOG.debug("User to edit is: '{}'", userToEdit);
            if (userToEdit == null)
                throw new EditUserException("user.error.not_exists");
            user.setPassword(userToEdit.getPassword());
            if (!userValidator.validateLogin(user.getLogin())) {
                LOG.warn(INVALID_USER_LOGIN, user.getLogin());
                throw new EditUserException(USER_ERROR_INVALID_LOGIN);
            }
            if (!userValidator.validateEmail(user.getEmail())) {
                LOG.warn("Invalid user email: '{}'", user.getEmail());
                throw new EditUserException("user.error.invalid.email");
            }
            List<User> allUsers = userDao.findAll();
            allUsers.remove(userToEdit);
            boolean isLoginNotUnique = allUsers.stream()
                    .anyMatch(u -> u.getLogin().equals(user.getLogin()));
            if (isLoginNotUnique) {
                LOG.warn("User login is duplicated: '{}'", user.getLogin());
                throw new EditUserException("user.error.duplicate.login");
            }
            boolean isEmailNotUnique = allUsers.stream()
                    .anyMatch(u -> u.getEmail().equals(user.getEmail()));
            if (isEmailNotUnique) {
                LOG.warn("User email is duplicated: '{}'", user.getEmail());
                throw new EditUserException("user.error.duplicate.email");
            }
            return userDao.update(user);
        } catch (DaoException e) {
            LOG.error("Cannot edit user with id: '{}'", user.getId());
            throw new ServiceException("Cannot edit user", e);
        }
    }

    @Override
    public boolean changePassword(long userId,
                                  String currentPassword,
                                  String newPassword,
                                  String confirmedNewPassword) throws ServiceException, EditUserException {
        if (!userValidator.validatePassword(currentPassword))
            throw new EditUserException("user.error.invalid.old_password");
        if (!userValidator.validatePassword(newPassword))
            throw new EditUserException("user.error.invalid.new_password");
        if (!userValidator.validatePassword(confirmedNewPassword))
            throw new EditUserException("user.error.invalid.confirmed_password");
        if (!newPassword.equals(confirmedNewPassword))
            throw new EditUserException("user.error.different_new_and_confirmed_passwords");
        if (newPassword.equals(currentPassword))
            throw new EditUserException("user.error.equals_new_and_old_passwords");
        LOG.trace("New user password is valid");
        try {
            User user = userDao.read(userId);
            if (user == null || !user.getPassword().equals(currentPassword))
                throw new EditUserException("user.error.invalid.old_password");
            user.setPassword(newPassword);
            return userDao.update(user);
        } catch (DaoException e) {
            LOG.error("Cannot change password for user with id: '{}'", userId);
            throw new ServiceException("Cannot change password for user", e);
        }
    }
}
