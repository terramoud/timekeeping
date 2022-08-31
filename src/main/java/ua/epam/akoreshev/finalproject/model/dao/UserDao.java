package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityBean;

import java.util.List;

public interface UserDao extends BaseDao<User, Long> {
    List<User> findAllUsersByActivity(long activityId) throws DaoException;
    List<User> findAllUsersByActivity(String activityName) throws DaoException;
    User read(String login) throws DaoException;

    long getNumberUsers() throws DaoException;

    List<User> findAllUsers(int limit, int offset) throws DaoException;
}

