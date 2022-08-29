package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.User;

public interface UserService {
    boolean addUser(User user) throws ServiceException;
    User findUserByLoginAndPassword(String login, String password) throws ServiceException;
}

