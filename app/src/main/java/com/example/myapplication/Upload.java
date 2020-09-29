package com.example.myapplication;

public class Upload {
    private String Info;
    private String ImageUrl;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String info, String imageUrl) {
        Info = info;
        ImageUrl = imageUrl;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
