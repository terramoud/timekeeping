package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.ActivityException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.*;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    Map<Category, List<Activity>> findAllActivitiesByCategories() throws ServiceException;

    List<Activity> findAllActivitiesByUser(User user) throws ServiceException;

    long getNumberActivities() throws ServiceException;

    List<ActivityCategoryBean> getActivities(int limit, int offset, String columnName, String sortOrder) throws ServiceException;

    boolean removeActivity(long activityId) throws ServiceException;

    List<Category> findAllCategories() throws ServiceException;

    boolean editActivity(Activity activity) throws ActivityException, ServiceException;

    boolean createActivity(Activity activity) throws ActivityException, ServiceException;
}


