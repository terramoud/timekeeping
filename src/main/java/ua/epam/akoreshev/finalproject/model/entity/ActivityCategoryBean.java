package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

public class ActivityCategoryBean {
    private Activity activity;
    private Category category;

    public ActivityCategoryBean(Activity activity, Category category) {
        this.activity = activity;
        this.category = category;
    }

    public ActivityCategoryBean() {

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityCategoryBean that = (ActivityCategoryBean) o;
        return activity.equals(that.activity) &&
                category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, category);
    }

    @Override
    public String toString() {
        return "ActivityCategoryBean{" +
                "activity=" + activity +
                ", category=" + category +
                '}';
    }
}
