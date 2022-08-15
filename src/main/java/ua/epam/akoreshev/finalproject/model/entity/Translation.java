package ua.epam.akoreshev.finalproject.model.entity;

import java.util.Objects;

/**
 * Represents relevant entity from database's table
 *
 * @author Alexander Koreshev
 */
public class Translation extends Entity {
    private String translatedEn;
    private String translatedUk;

    public Translation() {
    }

    public Translation(String translatedEn, String translatedUk) {
        this.translatedEn = translatedEn;
        this.translatedUk = translatedUk;
    }

    public String getTranslatedEn() {
        return translatedEn;
    }

    public void setTranslatedEn(String translatedEn) {
        this.translatedEn = translatedEn;
    }

    public String getTranslatedUk() {
        return translatedUk;
    }

    public void setTranslatedUk(String translatedUk) {
        this.translatedUk = translatedUk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return id == that.id &&
                translatedEn.equals(that.translatedEn) &&
                translatedUk.equals(that.translatedUk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, translatedEn, translatedUk);
    }

    @Override
    public String toString() {
        return "Translation{" +
                "id=" + id +
                ", translated_en='" + translatedEn + '\'' +
                ", translated_uk='" + translatedUk + '\'' +
                '}';
    }
}
