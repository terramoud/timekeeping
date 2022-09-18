package ua.epam.akoreshev.finalproject.web.service;


import ua.epam.akoreshev.finalproject.exceptions.CategoryException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.util.List;

public interface CategoryService {

    long getNumberCategories() throws ServiceException;

    List<Category> getCategories(int limit, int offset, String columnName, String sortOrder) throws ServiceException;

    boolean removeCategory(long categoryId) throws ServiceException;

    boolean editCategory(Category category) throws CategoryException, ServiceException;

    boolean createCategory(Category category) throws CategoryException, ServiceException;
}


