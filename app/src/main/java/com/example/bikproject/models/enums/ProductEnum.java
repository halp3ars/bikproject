package com.example.bikproject.models.enums;

public enum ProductEnum {
    PRICE("price"), NAME("name"), PHOTO_NAME("photoName");

    private final String fields;

    private ProductEnum(String fields) {
        this.fields = fields;
    }

    public String getField() {
        return this.fields;
    }

}
