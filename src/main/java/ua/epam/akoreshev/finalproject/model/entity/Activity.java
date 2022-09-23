package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public class Activity extends Entity {
    private String nameEn;
    private String nameUk;
    private long categoryId;

    /**
     * Creates new instance of the {@link Activity} entity with id == 0L
     *
     * @param nameEn     Default name of activity
     * @param nameUk     Localized name of activity
     * @param categoryId the unique identifier of the {@link Category} entity
     */
    public Activity(String nameEn, String nameUk, long categoryId) {
        this.nameEn = nameEn;
        this.nameUk = nameUk;
        this.categoryId = categoryId;
    }

    /**
     * Creates new instance of the {@link Activity} entity
     *
     * @param id         the unique identifier of the {@link Activity} entity
     * @param nameEn     Default name of the {@link Activity} entity
     * @param nameUk     Localized name of the {@link Activity} entity
     * @param categoryId the unique identifier of the {@link Category} entity
     */
    public Activity(long id, String nameEn, String nameUk, long categoryId) {
        this(nameEn, nameUk, categoryId);
        this.id = id;
    }

    /**
     * Creates new instance of the {@link Activity} entity
     * with unfilled fields
     */
    public Activity() {
    }

    /**
     * Returns default name of the {@link Activity} entity or {@code null}
     *
     * @return Default name of the {@link Activity} entity or {@code null}
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * Replaces the current name of activity by specified name.
     *
     * @param nameEn Default name of the {@link Activity} entity
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * Returns localized name of the {@link Activity} entity or {@code null}
     *
     * @return Localized name of the {@link Activity} entity or {@code null}
     */
    public String getNameUk() {
        return nameUk;
    }

    /**
     * Replaces the current name of activity by specified name.
     *
     * @param nameUk Localized name of the {@link Activity} entity
     */
    public void setNameUk(String nameUk) {
        this.nameUk = nameUk;
    }

    /**
     * Returns the unique identifier of the category or {@code 0}
     *
     * @return the unique identifier of the category or {@code 0}
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * Replaces the current identifier of category by specified identifier.
     *
     * @param categoryId the unique identifier of the {@link Category} entity
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id &&
                categoryId == activity.categoryId &&
                nameEn.equals(activity.nameEn) &&
                nameUk.equals(activity.nameUk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameEn, nameUk, categoryId);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", nameEn='" + nameEn + '\'' +
                ", nameUk='" + nameUk + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
