package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.EditUserException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.exceptions.UserException;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.util.List;

public interface UserService {
    boolean addUser(User user, String passwordConfirm) throws ServiceException, UserException;

    User findUserByLoginAndPassword(String login, String password) throws ServiceException, UserException;

    int getNumberUsers() throws ServiceException;

    List<User> getUsers(int limit, int offset, String columnName, String sortOrder) throws ServiceException;

    boolean removeUser(long userId) throws ServiceException;

    boolean editUser(User user) throws ServiceException, EditUserException;

    boolean changePassword(long userId,
                           String oldPassword,
                           String newPassword,
                           String confirmedNewPassword) throws ServiceException, EditUserException;
}

