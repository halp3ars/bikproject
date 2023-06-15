package com.example.bikproject.models.enums;

public enum UserInfoEnum {

    FIO("fio"),
    BIRTHDAY("dateBirth"),
    CITY("city"),
    PHONE("phone"),
    EMAIL("email"),
    POINTS("points");


    private final String fields;

    private UserInfoEnum(String fields) {
        this.fields = fields;
    }

    public String getField() {
        return this.fields;
    }
}
