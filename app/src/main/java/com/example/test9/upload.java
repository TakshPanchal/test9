package com.example.test9;

public class upload {

    private String mName;
    private String mimageUrl;

    public upload() {

    }

    public upload(String name, String imageUrl) {
            if(name.trim().equals(""))
            {

                name ="no name";
            }
        mName = name;
        mimageUrl = imageUrl;

    }

    public String getName() {
        return mName;
    }

    public void setmame(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mimageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mimageUrl = imageUrl;
    }
}
