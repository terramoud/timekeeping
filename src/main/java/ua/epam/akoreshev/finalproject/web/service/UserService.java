package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.model.entity.UserActivityBean;

import java.util.List;

public interface UserService {
    boolean addUser(User user) throws ServiceException;
    User findUserByLoginAndPassword(String login, String password) throws ServiceException;

    long getNumberUsers() throws ServiceException;

    List<User> getUsers(int limit, int offset) throws ServiceException;

    boolean removeUser(long userId) throws ServiceException;
}

