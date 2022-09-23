package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public class Category extends Entity {
    private String nameEn;
    private String nameUk;

    /**
     * Creates new instance of the {@link Category} entity
     * with unfilled fields
     */
    public Category() {
    }

    /**
     * Creates new instance of the {@link Category} entity
     *
     * @param id the unique identifier of the {@link Category} entity
     * @param nameEn Default name of activity
     * @param nameUk Localized name of activity
     */
    public Category(long id, String nameEn, String nameUk) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameUk = nameUk;
    }

    /**
     * Creates new instance of the {@link Category} entity with id == 0L
     *
     * @param nameEn Default name of activity
     * @param nameUk Localized name of activity
     */
    public Category(String nameEn, String nameUk) {
        this.nameEn = nameEn;
        this.nameUk = nameUk;
    }

    /**
     * Returns default name of the {@link Category} entity or {@code null}
     *
     * @return Default name of the {@link Category} entity or {@code null}
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * Replaces the current name of the {@link Category} by specified name.
     *
     * @param nameEn Default name of the {@link Category} entity
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * Returns localized name of the {@link Category} entity or {@code null}
     *
     * @return Localized name of the {@link Category} entity or {@code null}
     */
    public String getNameUk() {
        return nameUk;
    }

    /**
     * Replaces the current name of the {@link Category} by specified name.
     *
     * @param nameUk Localized name of the {@link Category} entity
     */
    public void setNameUk(String nameUk) {
        this.nameUk = nameUk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                nameEn.equals(category.nameEn) &&
                nameUk.equals(category.nameUk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameEn, nameUk);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name_en='" + nameEn + '\'' +
                ", name_uk='" + nameUk + '\'' +
                '}';
    }
}
