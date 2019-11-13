package com.miracle.dronam.model;

/**
 * Created by Vaibhav on 25-Jul-18.
 */

public class ProfileObject {

    String title;
    Integer icon;

    public ProfileObject(String title, Integer icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public Integer getIcon() {return icon;}

    public void setIcon(Integer icon) {this.icon = icon;}
}


