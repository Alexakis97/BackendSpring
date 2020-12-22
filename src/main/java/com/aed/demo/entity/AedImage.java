package com.aed.demo.entity;

public class AedImage {
    
   private String image;
   private String aedName;

    public AedImage(String image, String aedName) {
        this.image = image;
        this.aedName = aedName;
    }

    public AedImage() {
    }
    
    public String getImage() {
        return image;
    }

    public String getAedName() {
        return aedName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAedNameName(String imageName) {
        this.aedName = imageName;
    }
    
    
    
}
