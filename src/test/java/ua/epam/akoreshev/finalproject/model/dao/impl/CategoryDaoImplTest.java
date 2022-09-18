package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.CategoryDao;
import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDaoImplTest {
    private static CategoryDao categoryDao;
    private static Connection connection;

    @BeforeAll
    public static void setUpBeforeAll() throws DaoException {
        connection = DBUtil.getConnection();
        categoryDao = new CategoryDaoImpl(connection);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @AfterAll
    public static void tearDownBeforeAll() throws SQLException {
        connection.close();
    }

    /**
     * @see CategoryDaoImpl#read(Long)
     */
    @Test
    void testReadCategory() throws DaoException, SQLException {
        Category expectedCategory = new Category("Category1", "Категорія1");
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO categories VALUES (DEFAULT, 'Category1', 'Категорія1')",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new UnsupportedOperationException("Fatal: The test is crashed");
        }
        expectedCategory.setId(rs.getLong(1));
        Category actualCategory = categoryDao.read(expectedCategory.getId());
        assertEquals(expectedCategory, actualCategory);
    }

    /**
     * @see CategoryDaoImpl#read(Long)
     */
    @Test
    void testReadCategoryShouldReturnNullWhenCategoryIsWrong() throws DaoException {
        assertNull(categoryDao.read(-1L));
        assertNull(categoryDao.read(Long.MAX_VALUE));
    }

    /**
     * @see CategoryDaoImpl#read(Long)
     */
    @Test
    void testReadCategoryShouldReturnNullWhenCategoryIsMissing() throws DaoException {
        assertNull(categoryDao.read(0L));
        assertNull(categoryDao.read((long) Integer.MAX_VALUE));
    }

    /**
     * @see CategoryDaoImpl#create(Category)
     */
    @Test
    void testCreateCategory() throws DaoException {
        Category expectedCategory = new Category("Category1", "Категорія1");
        long rowsBeforeCreate = getCountRowsFromTable();
        assertTrue(categoryDao.create(expectedCategory)); // And now expected has synchronized 'id' with db
        Category actualCategory = categoryDao.read(expectedCategory.getId());
        assertEquals(expectedCategory, actualCategory);
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate + 1, rowsAfterCreate);
    }

    /**
     * @see CategoryDaoImpl#create(Category)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenCategoryIsNotUniqueRowInTable")
    void testCreateCategoryShouldThrowExceptionWhenCategoryIsDuplicated(Category duplicatedCategory) throws DaoException {
        Category testCategory = new Category("Category1", "Категорія1");
        assertTrue(categoryDao.create(testCategory));
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> categoryDao.create(duplicatedCategory));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see CategoryDaoImpl#create(Category)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenCategoryIsNull")
    void testCreateCategoryShouldThrowExceptionWhenCategoryIsNull(Category expectedCategory) {
        long rowsBeforeCreate = getCountRowsFromTable();
        assertThrows(DaoException.class, () -> categoryDao.create(expectedCategory));
        long rowsAfterCreate = getCountRowsFromTable();
        assertEquals(rowsBeforeCreate, rowsAfterCreate);
    }

    /**
     * @see CategoryDaoImpl#update(Category)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenUpdatedCategoryIsNotUniqueRowInTable")
    void testUpdateCategoryShouldThrowExceptionWhenCategoryIsDuplicated(Category duplicatedCategory) throws DaoException {
        assertTrue(categoryDao.create(new Category("Category2", "Категорія2")));
        Category sourceCategory = new Category("Category1", "Категорія1");
        assertTrue(categoryDao.create(sourceCategory));
        sourceCategory.setNameEn(duplicatedCategory.getNameEn());
        sourceCategory.setNameUk(duplicatedCategory.getNameUk());
        assertThrows(DaoException.class, () -> categoryDao.update(sourceCategory));
    }

    /**
     * @see CategoryDaoImpl#update(Category)
     */
    @ParameterizedTest
    @MethodSource("testCasesWhenCategoryIsNull")
    void testUpdateCategoryShouldThrowExceptionWhenCategoryIsNull(Category categoryHasNullField) throws DaoException {
        Category sourceCategory = new Category("Category1", "Категорія1");
        assertTrue(categoryDao.create(sourceCategory));
        sourceCategory.setNameEn(categoryHasNullField.getNameEn());
        sourceCategory.setNameUk(categoryHasNullField.getNameUk());
        assertThrows(DaoException.class, () -> categoryDao.update(sourceCategory));
    }

    /**
     * @see CategoryDaoImpl#delete(Long)
     */
    @Test
    void testDeleteCategoryShouldReturnFalseWhenCategoryIsMissing() throws DaoException {
        assertFalse(categoryDao.delete(0L));
        assertFalse(categoryDao.delete(-1L));
    }

    /**
     * @see CategoryDaoImpl#delete(Long)
     */
    @Test
    void testDeleteCategory() throws DaoException, SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO categories VALUES (DEFAULT, 'Category1', 'Категорія1')",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: The test is crashed");
        }
        long rowsBeforeDelete = getCountRowsFromTable();
        assertTrue(categoryDao.delete(rs.getLong(1)));
        long rowsAfterDelete = getCountRowsFromTable();
        assertNull(categoryDao.read(rs.getLong(1)));
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    /**
     * @see CategoryDaoImpl#findAll()
     */
    @Test
    void testFindAllCategories() throws DaoException, SQLException {
        List<Category> categoryListBeforeAddedCategory = categoryDao.findAll();
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO categories VALUES (DEFAULT, 'Category1', 'Категорія1')",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: The test is crashed");
        }
        Category expectedCategory = new Category("Category1", "Категорія1");
        expectedCategory.setId(rs.getLong(1));

        List<Category> categoryListAfterAddedCategory = categoryDao.findAll();
        List<Category> differences = new LinkedList<>(categoryListAfterAddedCategory);
        differences.removeAll(categoryListBeforeAddedCategory);
        assertEquals(1, differences.size());
        assertEquals(expectedCategory, differences.get(0));
    }

    /**
     * @see CategoryDaoImpl#findAll(int, int, String, String) ()
     */
    @Test
    void testFindAllWithLimit() throws DaoException, SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO categories VALUES (DEFAULT, 'Category1', 'Категорія1')",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: The test is crashed");
        }
        Category expectedCategory1 = new Category("Category1", "Категорія1");
        expectedCategory1.setId(rs.getLong(1));
        pst.close();
        rs.close();
        pst = connection.prepareStatement(
                "INSERT INTO categories VALUES (DEFAULT, 'Category2', 'Категорія2')",
                Statement.RETURN_GENERATED_KEYS);
        pst.executeUpdate();
        rs = pst.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Fatal: The test is crashed");
        }
        Category expectedCategory2 = new Category("Category2", "Категорія2");
        expectedCategory2.setId(rs.getLong(1));
        int limit = (int) getCountRowsFromTable();
        List<Category> sortedCategoryListByASC =
                categoryDao.findAll(limit, 0, "name_en", "ASC")
                        .stream()
                        .filter(c -> c.equals(expectedCategory1) || c.equals(expectedCategory2))
                        .collect(Collectors.toList());
        List<Category> sortedCategoryListByDESC =
                categoryDao.findAll(limit, 0, "name_en", "DESC")
                        .stream()
                        .filter(c -> c.equals(expectedCategory1) || c.equals(expectedCategory2))
                        .collect(Collectors.toList());
        int ascIndexCategory1 = sortedCategoryListByASC.indexOf(expectedCategory1);
        int ascIndexCategory2 = sortedCategoryListByASC.indexOf(expectedCategory2);
        int descIndexCategory1 = sortedCategoryListByDESC.indexOf(expectedCategory1);
        int descIndexCategory2 = sortedCategoryListByDESC.indexOf(expectedCategory2);
        assertTrue(ascIndexCategory1 < ascIndexCategory2);
        assertTrue(descIndexCategory1 > descIndexCategory2);
    }

    /**
     * @see CategoryDaoImpl#getNumberCategories()
     */
    @Test
    void testGetNumberCategories() throws DaoException {
        assertEquals(getCountRowsFromTable(), categoryDao.getNumberCategories());
    }

    private static Stream<Arguments> testCasesWhenCategoryIsNotUniqueRowInTable() {
        return Stream.of(
                Arguments.of(new Category("Category1", "Категорія1")),
                Arguments.of(new Category("Category2", "Категорія1")),
                Arguments.of(new Category("Category1", "Категорія2"))
        );
    }

    private static Stream<Arguments> testCasesWhenUpdatedCategoryIsNotUniqueRowInTable() {
        return Stream.of(
                Arguments.of(new Category("Category2", "Категорія1")),
                Arguments.of(new Category("Category1", "Категорія2"))
        );
    }

    private static Stream<Arguments> testCasesWhenCategoryIsChanged() {
        return Stream.of(
                Arguments.of(new Category("Category1", "Категорія1")), // update cloned categ
                Arguments.of(new Category("updated_Category1", "обновлена_Категорія1")),
                Arguments.of(new Category("updated_Category1", "Категорія1")),
                Arguments.of(new Category("Category1", "обновлена_Категорія1"))
        );
    }

    private static Stream<Arguments> testCasesWhenCategoryIsNull() {
        return Stream.of(
                Arguments.of(new Category(null, null)),
                Arguments.of(new Category("Категорія1", null)),
                Arguments.of(new Category(null, "Категорія1"))
        );
    }

    private long getCountRowsFromTable() {
        long result = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM categories");
            if (rs.next()) {
                result = rs.getLong("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}