package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.model.entity.User;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    /**
     * Finds all activities in DB
     *
     * @return list of all users
     * @throws DaoException if unable to retrieve information for certain reasons
     */
    Map<Category, List<Activity>> findAllActivitiesByCategories() throws ServiceException;

    List<Activity> findAllActivitiesByUser(User user) throws ServiceException;
}


