package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.*;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.utils.ActivityValidator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private static final Logger LOG = LogManager.getLogger(ActivityServiceImpl.class);

    private final ActivityDao activityDao;
    private final ActivityValidator activityValidator;

    public ActivityServiceImpl(ActivityDao activityDao, ActivityValidator activityValidator) {
        this.activityDao = activityDao;
        this.activityValidator = activityValidator;
    }

    @Override
    public Map<Category, List<Activity>> findAllActivitiesByCategories() throws ServiceException {
        try {
            Map<Category, List<Activity>> categoryListMap = new LinkedHashMap<>();
            for (Category category : activityDao.findAllCategories())
                categoryListMap.put(category, activityDao.findAllActivitiesByCategory(category.getId()));
            return categoryListMap;
        } catch (DaoException e) {
            LOG.error("Cannot find activities by categories");
            throw new ServiceException("Cannot find activities by categories", e);
        }
    }

    @Override
    public List<Activity> findAllActivitiesByUser(User user) throws ServiceException {
        try {
            return activityDao.findAllActivitiesByUser(user.getId());
        } catch (DaoException e) {
            LOG.error("Cannot find activities by user: {}", user);
            throw new ServiceException("Cannot find activities by user", e);
        }
    }

    @Override
    public long getNumberActivities() throws ServiceException {
        try {
            return activityDao.getNumberActivities();
        } catch (DaoException e) {
            LOG.error("Cannot count activities");
            throw new ServiceException("Cannot count activities", e);
        }
    }

    @Override
    public List<ActivityCategoryBean> getActivities(int limit, int offset, String columnName, String sortOrder) throws ServiceException {
        try {
            return activityDao.findAllActivities(limit, offset, columnName, sortOrder);
        } catch (DaoException e) {
            LOG.error("Cannot find any activities");
            throw new ServiceException("Cannot find any activities", e);
        }
    }

    @Override
    public boolean removeActivity(long activityId) throws ServiceException {
        try {
            return activityValidator.validateId(activityId) && activityDao.delete(activityId);
        } catch (DaoException e) {
            LOG.error("Cannot remove activity by id: {}", activityId);
            throw new ServiceException("Cannot remove activity", e);
        }
    }

    @Override
    public List<Category> findAllCategories() throws ServiceException {
        try {
            return activityDao.findAllCategories();
        } catch (DaoException e) {
            LOG.error("Cannot find any categories");
            throw new ServiceException("Cannot find any categories", e);
        }
    }

    @Override
    public boolean editActivity(Activity activity) throws ActivityException, ServiceException {
        if (!activityValidator.validateAllNames(activity)) {
            LOG.warn("Invalid activity names: {}", activity);
            throw new ActivityException("activity.error.invalid.names");
        }
        try {
            Activity activityToEdit = activityDao.read(activity.getId());
            LOG.debug("Activity to edit is: {}", activityToEdit);
            if (activityToEdit == null)
                throw new ActivityException("activity.error.not_exists");
            return activityDao.update(activity);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                LOG.warn("Activity is duplicated: {}", activity);
                throw new ActivityException("activity.error.duplicate.name");
            }
            LOG.error("Cannot edit activity {}", activity);
            throw new ServiceException("Cannot edit activity", e);
        }
    }

    @Override
    public boolean createActivity(Activity activity) throws ActivityException, ServiceException {
        if (!activityValidator.validate(activity)) {
            LOG.warn("Invalid activity is: {}", activity);
            throw new ActivityException("activity.error.invalid.names");
        }
        try {
            LOG.debug("Activity to create it is: {}", activity);
            return activityDao.create(activity);
        } catch (DaoException e) {
            if (e.getSqlErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                LOG.warn("Activity is duplicated: {}", activity);
                throw new ActivityException("activity.error.duplicate.name");
            }
            LOG.error("Cannot create activity {}", activity);
            throw new ServiceException("Cannot create activity", e);
        }
    }
}
