package com.example.dawaiilello.fragment;

public class HomeCategory {
    String CategoryName;
    String icon;
    String type;
    public HomeCategory(String CategoryName, String icon, String type){
        this.CategoryName = CategoryName;
        this.icon = icon;
        this.type = type;
    }
    public  String getCategoryName(){
        return CategoryName;
    }
    public void setCategoryName(String CategoryName){
        this.CategoryName = CategoryName;
    }

    public  String getIcon(){
        return icon;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }

    public  String getType(){
        return type;
    }
    public void setType(String CategoryName){
        this.type = type;
    }
}
