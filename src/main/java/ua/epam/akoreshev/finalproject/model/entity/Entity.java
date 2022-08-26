package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;

/**
 * Basic class for every model entity
 */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -1491456600908233143L;
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
