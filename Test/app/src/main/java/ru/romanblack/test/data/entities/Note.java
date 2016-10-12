package ru.romanblack.test.data.entities;

import java.io.Serializable;

public class Note implements Serializable {

    private Long _id;
    private String title;

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
