package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private static final Logger LOG = LogManager.getLogger(ActivityServiceImpl.class);
    private final ActivityDao activityDao;

    public ActivityServiceImpl(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    /**
     * @return
     * @throws DaoException if unable to retrieve information for certain reasons
     */
    @Override
    public Map<Category, List<Activity>> findAllActivitiesByCategories() throws ServiceException {
        List<Category> categories = null;
        try {
            categories = activityDao.findAllCategories();
            Map<Category, List<Activity>> categoryListMap = new LinkedHashMap<>();
            for (Category category : categories) {
                categoryListMap.put(category,
                        activityDao.findAllActivitiesByCategory(category.getId()));
            }
            return categoryListMap;
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find activities by categories " + e.getMessage());
        }
    }

    @Override
    public List<Activity> findAllActivitiesByUser(User user) throws ServiceException {
        try {
            return activityDao.findAllActivitiesByUser(user.getId());
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find activities by user " + e.getMessage());
        }
    }
}
