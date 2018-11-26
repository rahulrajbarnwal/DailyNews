package com.rahulrajbarnwal.mynews.modal;

import android.content.Intent;

public class Cats {
    public int id;
    public String catName;
    public Integer catImage;

    public Cats(int id, String catName, Integer catImage) {
        this.id = id;
        this.catName = catName;
        this.catImage = catImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Integer getCatImage() {
        return catImage;
    }

    public void setCatImage(Integer catImage) {
        this.catImage = catImage;
    }
}
