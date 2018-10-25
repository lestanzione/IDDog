package br.com.stanzione.iddog.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DogGallery {

    @SerializedName("category")
    private String category;

    @SerializedName("list")
    private List<String> imageUrlList;

    public DogGallery(){}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

}
