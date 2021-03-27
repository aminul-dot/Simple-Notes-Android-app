package com.letsseetech.data.simplenotes;

public class Data {

    String title;
    String description;
    String id;
    String userId;

    public Data(String id, String title, String description, String userId) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

}
