package ua.epam.akoreshev.finalproject.web.service.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.exceptions.CategoryException;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.model.dao.CategoryDao;
import ua.epam.akoreshev.finalproject.web.service.CategoryService;
import ua.epam.akoreshev.finalproject.web.utils.CategoryValidator;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceImplTest {
    private CategoryService categoryService;
    private CategoryDao categoryDao;
    private CategoryValidator categoryValidator;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        categoryDao = Mockito.mock(CategoryDao.class);
        categoryValidator = Mockito.mock(CategoryValidator.class);
        categoryService = new CategoryServiceImpl(categoryDao, categoryValidator);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see CategoryServiceImpl#getNumberCategories()
     */
    @Test
    void testGetNumberCategoriesShouldReturnNumber() throws DaoException, ServiceException {
        long expected = 1L;
        Mockito.when(categoryDao.getNumberCategories()).thenReturn(expected);
        assertEquals(expected, categoryService.getNumberCategories());
    }

    /**
     * @see CategoryServiceImpl#getNumberCategories()
     */
    @Test
    void testGetNumberCategoriesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Mockito.when(categoryDao.getNumberCategories()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> categoryService.getNumberCategories());
    }

    /**
     * @see CategoryServiceImpl#getCategories(int, int, String, String)
     */
    @Test
    void testGetCategoriesShouldReturnSortedList() throws DaoException, ServiceException {
        int limit = 5;
        List<Category> expected = IntStream.range(0, limit)
                .mapToObj(i -> new Category(i, "category" + i, "category_uk" + i))
                .collect(Collectors.toList());
        Mockito.when(categoryDao.findAll(limit, 0, "categories.name_en", "ASC")).thenReturn(expected);
        assertEquals(expected, categoryService.getCategories(limit, 0, "categories.name_en", "ASC"));

        expected.sort(Comparator.comparing(Category::getNameEn).reversed());
        Mockito.when(categoryDao.findAll(limit, 0, "categories.name_en", "DESC")).thenReturn(expected);
        assertEquals(expected, categoryService.getCategories(limit, 0, "categories.name_en", "DESC"));
    }

    /**
     * @see CategoryServiceImpl#getCategories(int, int, String, String)
     */
    @Test
    void testGetCategoriesShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        int limit = 5;
        Mockito.when(categoryDao.findAll(limit, 0, "categories.name_en", "ASC"))
                .thenThrow(DaoException.class);
        assertThrows(ServiceException.class,
                () -> categoryService.getCategories(limit, 0, "categories.name_en", "ASC"));
    }

    /**
     * @see CategoryServiceImpl#removeCategory(long)
     */
    @Test
    void testRemoveCategoryShouldReturnTrueWhenCategorySuccessfullyRemoved() throws DaoException, ServiceException {
        long categoryId = 1L;
        Mockito.when(categoryValidator.validateId(categoryId)).thenReturn(true);
        Mockito.when(categoryDao.delete(categoryId)).thenReturn(true);
        assertTrue(categoryService.removeCategory(categoryId));
    }

    /**
     * @see CategoryServiceImpl#removeCategory(long)
     */
    @Test
    void testRemoveCategoryShouldReturnFalseWhenCategoryIsInvalidOrCannotRemoveIt() throws DaoException, ServiceException {
        long categoryId = 1L;
        Mockito.when(categoryValidator.validateId(categoryId)).thenReturn(false);
        Mockito.when(categoryDao.delete(categoryId)).thenReturn(true);
        assertFalse(categoryService.removeCategory(categoryId));

        Mockito.when(categoryValidator.validateId(categoryId)).thenReturn(true);
        Mockito.when(categoryDao.delete(categoryId)).thenReturn(false);
        assertFalse(categoryService.removeCategory(categoryId));

        Mockito.when(categoryValidator.validateId(categoryId)).thenReturn(false);
        Mockito.when(categoryDao.delete(categoryId)).thenReturn(false);
        assertFalse(categoryService.removeCategory(categoryId));
    }

    /**
     * @see CategoryServiceImpl#removeCategory(long)
     */
    @Test
    void testRemoveCategoryShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        long categoryId = 1L;
        Mockito.when(categoryValidator.validateId(categoryId)).thenReturn(true);
        Mockito.when(categoryDao.delete(categoryId)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> categoryService.removeCategory(categoryId));
    }

    /**
     * @see CategoryServiceImpl#editCategory(Category)
     */
    @Test
    void testEditCategoryShouldReturnTrueWhenCategorySuccessfullyEdited() throws DaoException, CategoryException, ServiceException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.read(category.getId())).thenReturn(category);
        Mockito.when(categoryDao.update(category)).thenReturn(true);
        assertTrue(categoryService.editCategory(category));
    }

    /**
     * @see CategoryServiceImpl#editCategory(Category)
     */
    @Test
    void testEditCategoryShouldReturnFalseWhenCategoryCannotEdit() throws DaoException, CategoryException, ServiceException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.read(category.getId())).thenReturn(category);
        Mockito.when(categoryDao.update(category)).thenReturn(false);
        assertFalse(categoryService.editCategory(category));
    }

    /**
     * @see CategoryServiceImpl#editCategory(Category)
     */
    @Test
    void testEditCategoryShouldThrowExceptionWhenDaoThrowException() throws DaoException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.read(category.getId())).thenReturn(category);
        Mockito.when(categoryDao.update(category)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> categoryService.editCategory(category));
    }

    /**
     * @see CategoryServiceImpl#editCategory(Category)
     */
    @Test
    void testEditCategoryShouldThrowExceptionWhenCategoryIsInvalidOrDuplicatedOrNotExists() throws DaoException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(false);
        Mockito.when(categoryDao.read(category.getId())).thenReturn(category);
        Mockito.when(categoryDao.update(category)).thenReturn(true);
        assertThrows(CategoryException.class, () -> categoryService.editCategory(category));

        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.read(category.getId())).thenReturn(null);
        Mockito.when(categoryDao.update(category)).thenReturn(true);
        assertThrows(CategoryException.class, () -> categoryService.editCategory(category));

        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.read(category.getId())).thenReturn(category);
        Mockito.when(categoryDao.update(category))
                .thenThrow(new DaoException("duplicate exception", new SQLException(), MysqlErrorNumbers.ER_DUP_ENTRY));
        assertThrows(CategoryException.class, () -> categoryService.editCategory(category));
    }

    /**
     * @see CategoryServiceImpl#createCategory(Category)
     */
    @Test
    void testCreateCategoryShouldReturnTrueWhenCategorySuccessfullyCreated() throws DaoException, CategoryException, ServiceException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.create(category)).thenReturn(true);
        assertTrue(categoryService.createCategory(category));
    }

    /**
     * @see CategoryServiceImpl#createCategory(Category)
     */
    @Test
    void testCreateCategoryShouldReturnFalseWhenCannotCreateCategory() throws DaoException, CategoryException, ServiceException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.create(category)).thenReturn(false);
        assertFalse(categoryService.createCategory(category));
    }


    /**
     * @see CategoryServiceImpl#createCategory(Category)
     */
    @Test
    void testCreateCategoryShouldThrowExceptionWhenActivityIsInvalidOrDuplicate() throws DaoException {
        Category category = new Category(1L, "category", "categoryUk");
        Mockito.when(categoryValidator.validate(category)).thenReturn(false);
        Mockito.when(categoryDao.create(category)).thenReturn(true);
        assertThrows(CategoryException.class, () -> categoryService.createCategory(category));

        Mockito.when(categoryValidator.validate(category)).thenReturn(true);
        Mockito.when(categoryDao.create(category))
                .thenThrow(new DaoException("duplicate exception", new SQLException(), MysqlErrorNumbers.ER_DUP_ENTRY));
        assertThrows(CategoryException.class, () -> categoryService.createCategory(category));
    }
}
