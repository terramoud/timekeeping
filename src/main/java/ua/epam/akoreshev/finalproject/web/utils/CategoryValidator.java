package ua.epam.akoreshev.finalproject.web.utils;

import ua.epam.akoreshev.finalproject.model.entity.Category;

import java.util.Arrays;

public class CategoryValidator extends EntityValidator {
    
    public boolean validateId(long categoryId) {
        return categoryId >= 0 && categoryId <= Integer.MAX_VALUE;
    }

    public boolean validate(Category category) {
        return validateId(category.getId())
                && validateAllNames(category);
    }

    public boolean validateAllNames(Category category) {
        return Arrays.stream(category.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("getName"))
                .allMatch(method -> validateField(invokeMethod(category, method)));
    }
}
