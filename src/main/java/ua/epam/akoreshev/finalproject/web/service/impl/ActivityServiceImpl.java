package ua.epam.akoreshev.finalproject.web.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.*;
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

    @Override
    public long getNumberCategories() throws ServiceException {
        try {
            return activityDao.getNumberCategories();
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot count categories " + e.getMessage());
        }
    }

    @Override
    public List<Category> getCategories(int limit, int offset) throws ServiceException {
        try {
            return activityDao.findAllCategories(limit, offset);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find categories " + e.getMessage());
        }
    }

    @Override
    public long getNumberActivities() throws ServiceException {
        try {
            return activityDao.getNumberActivities();
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot count activities " + e.getMessage());
        }
    }

    @Override
    public List<ActivityCategoryBean> getActivities(int limit, int offset) throws ServiceException {
        try {
            return activityDao.findAllActivities(limit, offset);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot find activities " + e.getMessage());
        }
    }

    @Override
    public boolean removeCategory(long categoryId) throws ServiceException {
        try {
            return activityDao.deleteCategory(categoryId);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot remove category " + e.getMessage());
        }
    }

    @Override
    public boolean removeActivity(long activityId) throws ServiceException {
        try {
            return activityDao.delete(activityId);
        } catch (DaoException e) {
            LOG.error(e);
            throw new ServiceException("Cannot remove activity " + e.getMessage());
        }
    }
}
