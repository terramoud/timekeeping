package ua.epam.akoreshev.finalproject.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.akoreshev.finalproject.exceptions.DaoException;
import ua.epam.akoreshev.finalproject.model.dao.CategoryDao;
import ua.epam.akoreshev.finalproject.model.dao.Mapper;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private static final String SQL_CREATE_CATEGORY = "INSERT INTO categories VALUES (DEFAULT, ?, ?)";

    private static final String SQL_GET_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";

    private static final String SQL_UPDATE_CATEGORY_BY_ID =
            "UPDATE categories SET name_en = ?, name_uk = ? WHERE id = ?";

    private static final String SQL_DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE id = ?";

    private static final String SQL_FIND_ALL_CATEGORIES = "SELECT * FROM categories";

    private static final String SQL_GET_NUMBER_CATEGORIES_OF_ACTIVITIES =
            "SELECT COUNT(*) AS numRows FROM categories";

    private final Mapper<Category, PreparedStatement> mapRowToDB = (Category category,
                                                                    PreparedStatement preparedStatement) -> {
        preparedStatement.setString(1, category.getNameEn());
        preparedStatement.setString(2, category.getNameUk());
    };

    private final Mapper<ResultSet, Category> mapRowFromDB = (ResultSet resultSet, Category category) -> {
        category.setId(resultSet.getLong("id"));
        category.setNameEn(resultSet.getString("name_en"));
        category.setNameUk(resultSet.getString("name_uk"));
    };

    private final Connection connection;

    private static final Logger LOG = LogManager.getLogger(CategoryDaoImpl.class);

    public CategoryDaoImpl(Connection connection) {
        this.connection = connection;
        LOG.debug("Actual obtained connection for CategoryDao is: {}", this.connection);
    }

    @Override
    public long getNumberCategories() throws DaoException {
        long result = 0;
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_GET_NUMBER_CATEGORIES_OF_ACTIVITIES);
            LOG.trace("SQL query find all 'categories' to database has already been completed successfully");
            if (rs.next()) {
                result = rs.getLong("numRows");
            }
            LOG.debug("The {} rows has been found by query to database", result);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find 'categories'", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public List<Category> findAll(int limit, int offset, String columnName, String sortOrder) throws DaoException {
        List<Category> categories = new LinkedList<>();
        String sqlParameters = " ORDER BY " + columnName
                .concat(" " + sortOrder)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        try (PreparedStatement pst = connection.prepareStatement(
                SQL_FIND_ALL_CATEGORIES + sqlParameters)) {
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query find all activities to database has already been completed successfully");
            while (rs.next()) {
                Category category = new Category();
                mapRowFromDB.map(rs, category);
                categories.add(category);
            }
            LOG.debug("The {} categories has been found by query to database", categories.size());
        } catch (SQLException e) {
            LOG.error("Cannot find activities sorted by: {}", sqlParameters);
            throw new DaoException("Cannot find activities", e, e.getErrorCode());
        }
        return categories;
    }

    @Override
    public List<Category> findAll() throws DaoException {
        List<Category> categoryList = new LinkedList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_FIND_ALL_CATEGORIES);
            LOG.trace("SQL query find all categories to database has already been completed successfully");
            while (rs.next()) {
                Category category = new Category();
                mapRowFromDB.map(rs, category);
                categoryList.add(category);
            }
            LOG.debug("The {} categories has been found by query to database", categoryList.size());
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot find any categories", e, e.getErrorCode());
        }
        return categoryList;
    }

    @Override
    public boolean create(Category category) throws DaoException {
        LOG.debug("Obtained category entity to create it at database is: {}", category);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_CREATE_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            mapRowToDB.map(category, pst);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to create category has already been completed successfully");
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                category.setId(rs.getLong(1));
                LOG.debug("The source category entity has synchronized 'id' with database and now represent the: {}", category);
            }
            if (rowCount == 0) {
                result = false;
                LOG.warn("The category wasn't created by query to database");
            }
            LOG.debug("The {} rows has been added to database to create category", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot create category at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public Category read(Long categoryId) throws DaoException {
        LOG.debug("Obtained 'category id' to read it from database is: {}", categoryId);
        Category category = new Category();
        try (PreparedStatement pst = connection.prepareStatement(SQL_GET_CATEGORY_BY_ID)) {
            pst.setLong(1, categoryId);
            ResultSet rs = pst.executeQuery();
            LOG.trace("SQL query to read an category from database has already been completed successfully");
            if (!rs.next()) return null;
            mapRowFromDB.map(rs, category);
            LOG.debug("The category: {} has been found by query to database", category);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot read category by id", e, e.getErrorCode());
        }
        return category;
    }

    @Override
    public boolean update(Category category) throws DaoException {
        LOG.debug("Obtained category entity to update it at database is: {}", category);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_CATEGORY_BY_ID)) {
            mapRowToDB.map(category, pst);
            pst.setLong(3, category.getId());
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to update an category from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The category wasn't updated by query to database");
            }
            LOG.debug("The {} rows has been changed to update category", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot update category at database", e, e.getErrorCode());
        }
        return result;
    }

    @Override
    public boolean delete(Long categoryId) throws DaoException {
        LOG.debug("Obtained 'category id' to delete it from database is: {}", categoryId);
        boolean result = true;
        try (PreparedStatement pst = connection.prepareStatement(SQL_DELETE_CATEGORY_BY_ID)) {
            pst.setLong(1, categoryId);
            int rowCount = pst.executeUpdate();
            LOG.trace("SQL query to delete an category from database has already been completed successfully");
            if (rowCount == 0) {
                result = false;
                LOG.warn("The category wasn't deleted by query to database");
            }
            LOG.debug("The {} rows has been removed to delete category", rowCount);
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Cannot delete category from database", e, e.getErrorCode());
        }
        return result;
    }
}
