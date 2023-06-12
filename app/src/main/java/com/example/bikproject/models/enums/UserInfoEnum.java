package com.example.bikproject.models.enums;

public enum UserInfoEnum {

    FIO("fio"),
    BIRTHDAY("dateBirth"),
    CITY("city"),
    PHONE("phone"),
    EMAIL("email");

    public String firestoreInfo;

    UserInfoEnum(String firestoreInfo) {
    }

    public String getFirestoreInfo() {
        return firestoreInfo;
    }
}
