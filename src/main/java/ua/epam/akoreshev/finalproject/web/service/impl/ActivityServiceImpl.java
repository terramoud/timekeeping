package ua.epam.akoreshev.finalproject.web.service.impl;

import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao;

    public ActivityServiceImpl(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    /**
     *
     *
     * @return
     * @throws DaoException if unable to retrieve information for certain reasons
     */
    @Override
    public Map<Category, List<Activity>> findAllActivitiesByCategories() throws DaoException {
        List<Category> categories = activityDao.findAllCategories();
        Map<Category, List<Activity>> categoryListMap = new LinkedHashMap<>();
        for (Category category : categories) {
            categoryListMap.put(category,
                    activityDao.findAllActivitiesByCategory(category.getId()));
        }
        return categoryListMap;
    }
}
