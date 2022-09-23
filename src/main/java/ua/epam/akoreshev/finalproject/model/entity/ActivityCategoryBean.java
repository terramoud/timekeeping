package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents pair of the {@link Activity} and
 * {@link Category} entities
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public class ActivityCategoryBean {
    private Activity activity;
    private Category category;

    /**
     * Creates new instance of the {@link ActivityCategoryBean} bean
     *
     * @param activity the {@link Activity} entity
     * @param category the {@link Category} entity
     */
    public ActivityCategoryBean(Activity activity, Category category) {
        this.activity = activity;
        this.category = category;
    }

    /**
     * Creates new empty instance of the {@link ActivityCategoryBean} bean
     */
    public ActivityCategoryBean() {

    }

    /**
     * Returns the {@link Activity} entity or {@code null}
     *
     * @return the {@link Activity} entity or {@code null}
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Replaces the current activity by specified one.
     *
     * @param activity the {@link Activity} entity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Returns the {@link Category} entity or {@code null}
     *
     * @return the {@link Category} entity or {@code null}
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Replaces the current {@link Category} entity by specified one.
     *
     * @param category the {@link Category} entity
     */
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
