package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.ActivityException;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.dao.ActivityDao;
import ua.epam.akoreshev.finalproject.model.entity.Activity;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.model.entity.User;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.utils.ActivityValidator;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class ActivityServiceImplTest {
    private ActivityService activityService;
    private ActivityDao activityDao;
    private ActivityValidator activityValidator;

    @BeforeAll
    public static void setUpBeforeAll() {
    }

    @BeforeEach
    public void setUp() {
        activityDao = Mockito.mock(ActivityDao.class);
        activityValidator = Mockito.mock(ActivityValidator.class);
        activityService = new ActivityServiceImpl(activityDao, activityValidator);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see ActivityServiceImpl#findAllActivitiesByCategories()
     */
    @Test
    void testFindAllActivitiesByCategoriesShouldReturnMap() throws DaoException, ServiceException {
        List<Activity> activities = LongStream.range(0, 5L)
                .mapToObj(i -> new Activity(i, "activity" + i, i + "activityUk", i))
                .collect(Collectors.toList());
        List<Category> categories = LongStream.range(0, 5L)
                .mapToObj(i -> new Category(i, "activity" + i, i + "activityUk"))
                .collect(Collectors.toList());
        Map<Category, List<Activity>> expected = categories.stream()
                .collect(Collectors.toMap(Function.identity(), a -> activities));
        Mockito.when(activityDao.findAllCategories()).thenReturn(categories);
        Mockito.when(activityDao.findAllActivitiesByCategory(Mockito.anyLong())).thenReturn(activities);
        assertEquals(expected, activityService.findAllActivitiesByCategories());
        assertEquals(expected.size(), activityService.findAllActivitiesByCategories().size());

        Map<Category, List<Activity>> expectedEmptyList = new LinkedHashMap<>();
        Mockito.when(activityDao.findAllCategories()).thenReturn(new LinkedList<>());
        Mockito.when(activityDao.findAllActivitiesByCategory(Mockito.anyLong())).thenReturn(activities);
        assertEquals(expectedEmptyList, activityService.findAllActivitiesByCategories());

        Mockito.when(activityDao.findAllCategories()).thenReturn(new LinkedList<>());
        Mockito.when(activityDao.findAllActivitiesByCategory(Mockito.anyLong())).thenReturn(new LinkedList<>());
        assertEquals(expectedEmptyList, activityService.findAllActivitiesByCategories());

        Map<Category, List<Activity>> expectedOnlyCategories = categories.stream()
                .collect(Collectors.toMap(Function.identity(), a -> new LinkedList<>()));
        Mockito.when(activityDao.findAllCategories()).thenReturn(categories);
        Mockito.when(activityDao.findAllActivitiesByCategory(Mockito.anyLong())).thenReturn(new LinkedList<>());
        assertEquals(expectedOnlyCategories, activityService.findAllActivitiesByCategories());
    }

    /**
     * @see ActivityServiceImpl#findAllActivitiesByCategories()
     */
    @Test
    void testFindAllActivitiesByCategoriesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        List<Activity> activities = LongStream.range(0, 5L)
                .mapToObj(i -> new Activity(i, "activity" + i, i + "activityUk", i))
                .collect(Collectors.toList());
        List<Category> categories = LongStream.range(0, 5L)
                .mapToObj(i -> new Category(i, "activity" + i, i + "activityUk"))
                .collect(Collectors.toList());
        Mockito.when(activityDao.findAllCategories()).thenThrow(DaoException.class);
        Mockito.when(activityDao.findAllActivitiesByCategory(Mockito.anyLong())).thenReturn(activities);
        assertThrows(ServiceException.class, () -> activityService.findAllActivitiesByCategories());

        Mockito.reset(activityDao);
        Mockito.when(activityDao.findAllCategories()).thenReturn(categories);
        Mockito.when(activityDao.findAllActivitiesByCategory(Mockito.anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.findAllActivitiesByCategories());
    }

    /**
     * @see ActivityServiceImpl#findAllActivitiesByUser(User)
     */
    @Test
    void testFindAllActivitiesByUserShouldReturnList() throws DaoException, ServiceException {
        long userId = Mockito.anyLong();
        User user = new User(userId, "testUserLogin", "testUser@email.com", "Password111", 2);
        List<Activity> expected = LongStream.range(0, 5L)
                .mapToObj(i -> new Activity(i, "activity" + i, i + "activityUk", i))
                .collect(Collectors.toList());
        Mockito.when(activityDao.findAllActivitiesByUser(userId)).thenReturn(expected);
        assertEquals(expected, activityService.findAllActivitiesByUser(user));

        List<Activity> emptyList = new LinkedList<>();
        Mockito.when(activityDao.findAllActivitiesByUser(userId)).thenReturn(emptyList);
        assertEquals(emptyList, activityService.findAllActivitiesByUser(user));
    }

    /**
     * @see ActivityServiceImpl#findAllActivitiesByUser(User)
     */
    @Test
    void testFindAllActivitiesByUserShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        User user = new User(0, "testUserLogin", "testUser@email.com", "Password111", 2);
        Mockito.when(activityDao.findAllActivitiesByUser(Mockito.anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.findAllActivitiesByUser(user));
    }

    /**
     * @see ActivityServiceImpl#getNumberActivities()
     */
    @Test
    void testGetNumberActivitiesShouldReturnNumber() throws DaoException, ServiceException {
        int expected = 1;
        Mockito.when(activityDao.getNumberActivities()).thenReturn(expected);
        assertEquals(expected, activityService.getNumberActivities());
    }

    /**
     * @see ActivityServiceImpl#getNumberActivities()
     */
    @Test
    void testGetNumberActivitiesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(activityDao.getNumberActivities()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.getNumberActivities());
    }

    /**
     * @see ActivityServiceImpl#getActivities(int, int, String, String)
     */
    @Test
    void testGetActivitiesShouldReturnList() throws DaoException, ServiceException {
        int limit = 5;
        List<ActivityCategoryBean> expected = IntStream.range(0, limit)
                .mapToObj(i -> new ActivityCategoryBean(
                        new Activity(i, "activity" + i, "activity_uk" + i, i),
                        new Category(i, "category" + i, "category_uk" + i)))
                .collect(Collectors.toList());
        Mockito.when(activityDao.findAllActivities(limit, 0, "activities.name_en", "ASC"))
                .thenReturn(expected);
        assertEquals(expected,
                activityService.getActivities(limit, 0, "activities.name_en", "ASC"));

        expected.sort(Comparator.comparing(o -> o.getActivity().getNameEn(), Comparator.reverseOrder()));
        Mockito.when(activityDao.findAllActivities(limit, 0, "activities.name_en", "DESC"))
                .thenReturn(expected);
        assertEquals(expected,
                activityService.getActivities(limit, 0, "activities.name_en", "DESC"));

        List<ActivityCategoryBean> expectedEmptyList = new LinkedList<>();
        Mockito.when(activityDao.findAllActivities(limit, 0, "activities.name_en", "ASC"))
                .thenReturn(expectedEmptyList);
        assertEquals(expectedEmptyList,
                activityService.getActivities(limit, 0, "activities.name_en", "ASC"));
    }

    /**
     * @see ActivityServiceImpl#getActivities(int, int, String, String)
     */
    @Test
    void testGetActivitiesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        int limit = 5;
        Mockito.when(activityDao.findAllActivities(limit, 0, "activities.name_en", "ASC"))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> activityService.getActivities(limit, 0, "activities.name_en", "ASC"));
    }

    /**
     * @see ActivityServiceImpl#removeActivity(long)
     */
    @Test
    void testRemoveActivityShouldReturnTrueWhenActivitySuccessfullyRemoved() throws DaoException, ServiceException {
        long activityId = Mockito.anyLong();
        Mockito.when(activityValidator.validateId(activityId)).thenReturn(true);
        Mockito.when(activityDao.delete(activityId)).thenReturn(true);
        assertTrue(activityService.removeActivity(activityId));
    }

    /**
     * @see ActivityServiceImpl#removeActivity(long)
     */
    @Test
    void testRemoveActivityShouldReturnFalseWhenActivityIsInvalidOrDaoReturnFalse() throws DaoException, ServiceException {
        long activityId = Mockito.anyLong();
        Mockito.when(activityValidator.validateId(activityId)).thenReturn(false);
        Mockito.when(activityDao.delete(activityId)).thenReturn(true);
        assertFalse(activityService.removeActivity(activityId));

        Mockito.when(activityValidator.validateId(activityId)).thenReturn(true);
        Mockito.when(activityDao.delete(activityId)).thenReturn(false);
        assertFalse(activityService.removeActivity(activityId));

        Mockito.when(activityValidator.validateId(activityId)).thenReturn(false);
        Mockito.when(activityDao.delete(activityId)).thenReturn(false);
        assertFalse(activityService.removeActivity(activityId));
    }

    /**
     * @see ActivityServiceImpl#removeActivity(long)
     */
    @Test
    void testRemoveActivityShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        long activityId = Mockito.anyLong();
        Mockito.when(activityValidator.validateId(activityId)).thenReturn(true);
        Mockito.when(activityDao.delete(activityId)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.removeActivity(activityId));
    }

    /**
     * @see ActivityServiceImpl#findAllCategories()
     */
    @Test
    void testFindAllCategoriesShouldReturnList() throws DaoException, ServiceException {
        List<Category> expected = LongStream.range(0, 5L)
                .mapToObj(i -> new Category(i, "category" + i, i + "category_uk"))
                .collect(Collectors.toList());
        Mockito.when(activityDao.findAllCategories()).thenReturn(expected);
        assertEquals(expected, activityService.findAllCategories());

        List<Category> emptyList = new LinkedList<>();
        Mockito.when(activityDao.findAllCategories()).thenReturn(emptyList);
        assertEquals(emptyList, activityService.findAllCategories());
    }

    /**
     * @see ActivityServiceImpl#findAllCategories()
     */
    @Test
    void testFindAllCategoriesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(activityDao.findAllCategories()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.findAllActivitiesByCategories());
    }

    /**
     * @see ActivityServiceImpl#editActivity(Activity)
     */
    @Test
    void testEditActivityShouldReturnTrueWhenActivitySuccessfullyEdited() throws DaoException, ServiceException, ActivityException {
        long activityId = 1L;
        Activity activity = new Activity(activityId, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(true);
        Mockito.when(activityDao.read(activityId)).thenReturn(activity);
        Mockito.when(activityDao.update(activity)).thenReturn(true);
        assertTrue(activityService.editActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#editActivity(Activity)
     */
    @Test
    void testEditActivityShouldReturnFalseWhenDaoReturnFalse() throws DaoException, ServiceException, ActivityException {
        long activityId = 1L;
        Activity activity = new Activity(activityId, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(true);
        Mockito.when(activityDao.read(activityId)).thenReturn(activity);
        Mockito.when(activityDao.update(activity)).thenReturn(false);
        assertFalse(activityService.editActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#editActivity(Activity)
     */
    @Test
    void testEditActivityShouldThrowExceptionWhenActivityIsInvalidOrExistsOrDuplicate() throws DaoException {
        long activityId = Mockito.anyLong();
        Activity activity = new Activity(activityId, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(false);
        Mockito.when(activityDao.read(activityId)).thenReturn(activity);
        Mockito.when(activityDao.update(activity)).thenReturn(true);
        assertThrows(ActivityException.class, () -> activityService.editActivity(activity));

        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(true);
        Mockito.when(activityDao.read(activityId)).thenReturn(null);
        Mockito.when(activityDao.update(activity)).thenReturn(true);
        assertThrows(ActivityException.class, () -> activityService.editActivity(activity));

        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(true);
        Mockito.when(activityDao.read(activityId)).thenReturn(activity);
        Mockito.when(activityDao.update(activity))
                .thenThrow(new DaoException("duplicate exception", new SQLException(), MysqlErrorNumbers.ER_DUP_ENTRY));
        assertThrows(ActivityException.class, () -> activityService.editActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#editActivity(Activity)
     */
    @Test
    void testEditActivityShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        long activityId = 1L;
        Activity activity = new Activity(activityId, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(true);
        Mockito.when(activityDao.read(activityId)).thenThrow(DaoException.class);
        Mockito.when(activityDao.update(activity)).thenReturn(true);
        assertThrows(ServiceException.class, () -> activityService.editActivity(activity));

        Mockito.reset(activityDao);
        Mockito.when(activityValidator.validateAllNames(activity)).thenReturn(true);
        Mockito.when(activityDao.read(activityId)).thenReturn(activity);
        Mockito.when(activityDao.update(activity)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.editActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#createActivity(Activity)
     */
    @Test
    void testCreateActivityShouldReturnTrueWhenActivitySuccessfullyCreated() throws DaoException, ServiceException, ActivityException {
        Activity activity = new Activity(1L, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validate(activity)).thenReturn(true);
        Mockito.when(activityDao.create(activity)).thenReturn(true);
        assertTrue(activityService.createActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#createActivity(Activity)
     */
    @Test
    void testCreateActivityShouldReturnFalseWhenCannotCreateActivity() throws DaoException, ServiceException, ActivityException {
        Activity activity = new Activity(1L, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validate(activity)).thenReturn(true);
        Mockito.when(activityDao.create(activity)).thenReturn(false);
        assertFalse(activityService.createActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#createActivity(Activity)
     */
    @Test
    void testCreateActivityShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Activity activity = new Activity(1L, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validate(activity)).thenReturn(true);
        Mockito.when(activityDao.create(activity)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> activityService.createActivity(activity));
    }

    /**
     * @see ActivityServiceImpl#createActivity(Activity)
     */
    @Test
    void testCreateActivityShouldThrowExceptionWhenActivityIsInvalidOrDuplicate() throws DaoException {
        Activity activity = new Activity(1L, "activity", "activityUk", 1);
        Mockito.when(activityValidator.validate(activity)).thenReturn(true);
        Mockito.when(activityDao.create(activity))
                .thenThrow(new DaoException("duplicate exception", new SQLException(), MysqlErrorNumbers.ER_DUP_ENTRY));
        assertThrows(ActivityException.class, () -> activityService.createActivity(activity));

        Mockito.reset(activityDao);
        Mockito.when(activityValidator.validate(activity)).thenReturn(false);
        Mockito.when(activityDao.create(activity)).thenReturn(true);
        assertThrows(ActivityException.class, () -> activityService.createActivity(activity));
    }
}