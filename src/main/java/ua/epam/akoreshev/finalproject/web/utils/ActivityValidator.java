package ua.epam.akoreshev.finalproject.web.utils;

import ua.epam.akoreshev.finalproject.model.entity.Activity;

import java.util.Arrays;

public class ActivityValidator extends EntityValidator {

    public boolean validateId(long activityId) {
        return activityId >= 0 && activityId <= Integer.MAX_VALUE;
    }

    public boolean validateCategoryId(long categoryId) {
        return categoryId >= 0 && categoryId <= Integer.MAX_VALUE;
    }

    public boolean validate(Activity activity) {
        return validateId(activity.getId())
                && validateCategoryId(activity.getCategoryId())
                && validateAllNames(activity);
    }

    public boolean validateAllNames(Activity activity) {
        return Arrays.stream(activity.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("getName"))
                .allMatch(method -> validateField(invokeMethod(activity, method)));
    }
}
