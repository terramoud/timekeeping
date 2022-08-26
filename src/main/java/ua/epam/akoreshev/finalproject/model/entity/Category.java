package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Category extends Entity {
    private String nameEn;
    private String nameUk;

    public Category() {
    }

    public Category(long id, String nameEn, String nameUk) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameUk = nameUk;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameUk() {
        return nameUk;
    }

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
