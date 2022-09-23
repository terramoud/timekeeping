package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;

/**
 * Basic entity for every model entity
 *
 * @author Alexander Koreshev
 * @since 1.0
 */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -1491456600908233143L;
    protected long id;

    /**
     * Returns the unique identifier or {@code 0L}
     *
     * @return the unique identifier or {@code 0L}
     */
    public long getId() {
        return id;
    }

    /**
     * Replaces the current identifier of entity by specified identifier.
     *
     * @param id the unique identifier
     */
    public void setId(long id) {
        this.id = id;
    }
}
