package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.UserDao;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityBean;
import ua.epam.akoreshev.finalproject.web.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private final UserDao userDao;


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean addUser(User user) throws ServiceException {
        try {
            return validateUserData(user) && userDao.create(user);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot add new user. " + e.getMessage());
        }
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) throws ServiceException {
        try {
            User user = userDao.read(login);
            LOG.debug("Obtained user from db by login is: {}", user);
            if (!user.getPassword().equals(password))
                throw new DaoException("Check that the user login and password is correct");
            return user;
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find user by login and password. " + e.getMessage());
        }
    }

    @Override
    public long getNumberUsers() throws ServiceException {
        try {
            return userDao.getNumberUsers();
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot count users " + e.getMessage());
        }
    }

    @Override
    public List<User> getUsers(int limit, int offset) throws ServiceException {
        try {
            return userDao.findAllUsers(limit, offset);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find users " + e.getMessage());
        }
    }

    @Override
    public boolean removeUser(long userId) throws ServiceException {
        try {
            return userDao.delete(userId);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot remove user " + e.getMessage());
        }
    }

    private boolean validateUserData(User user) {
        return !(user.getLogin() == null
                || user.getLogin().isEmpty()
                || user.getPassword() == null
                || user.getPassword().isEmpty()
                || user.getEmail() == null
                || user.getEmail().isEmpty()
                || user.getId() < 0
                || user.getRoleId() < 0);
    }
}
