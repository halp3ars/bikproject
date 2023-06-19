package com.example.bikproject.models.dto;


import android.net.Uri;

import javax.annotation.Nullable;

public class ListData {

    private String name;
    private Integer price;
    private Uri image;


    public ListData(String name, Integer price, @Nullable Uri image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public ListData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}