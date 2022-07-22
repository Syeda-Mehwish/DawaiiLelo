package com.example.dawaiilello.fragment;

import java.util.List;

public class HomePageModel {
    public static  final  int BannerSlider = 0;
    public static final int StripAdBanner=1;
    public static final int HorizontalProductView = 2;
    public static final int GridProductView = 3;

    private  int type;
    private  String backgrounColor;

    ////////////////////////banner slider
    private List<sliderModel> sliderModelList;
    public HomePageModel(int type, List<sliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public List<sliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<sliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    ////////////////////////banner slider
    ////////////////////////Strip Ad
    private  String resource ;


    public  HomePageModel(int type, String resource, String backgrounColor){
        this.type=type;
        this.resource= resource;
        this.backgrounColor=backgrounColor;
    }
    public String getBackgrounColor() {
        return backgrounColor;
    }
    public void setBackgrounColor(String backgrounColor) {
        this.backgrounColor = backgrounColor;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }


    ////////////////////////Strip Ad
    //////////////////////////horizontal product layout
    private String title;
    private List<horizontalProductModel> horizontalProductModelList;


    public  HomePageModel(int type,String title,String backgrounColor, List<horizontalProductModel> horizontalProductModelList){
        this.type=type;
        this.title= title;
        this.backgrounColor = backgrounColor;
        this.horizontalProductModelList=horizontalProductModelList;

    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<horizontalProductModel> getHorizontalProductModelList() {
        return horizontalProductModelList;

    }
    public void setHorizontalProductModelList(List<horizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;

    }

    //////////////////////////horizontal product layout



    //////////////////////////horizontal product layout
    String tittle;
    List<HomeCategory>homeCategoryList;
    List<ViewALlcaModel> ViewAlllist;
    public  HomePageModel(int type,String tittle, List<HomeCategory> homeCategoryList/*List<horizontalProductModel> horizontalProductModelList*/,List<ViewALlcaModel> ViewAlllist){
        this.type=type;
        this.tittle= tittle;
        this.homeCategoryList = homeCategoryList;
        this.ViewAlllist =ViewAlllist;
    }
    public List<ViewALlcaModel>getViewAlllist(){
        return ViewAlllist;
    }
    public  void setViewAlllist(List<ViewALlcaModel> ViewAlllist){
        this.ViewAlllist=ViewAlllist;
    }

    public  HomePageModel(int type,String tittle, List<HomeCategory> homeCategoryList){
        this.type=type;
        this.tittle= tittle;
        this.homeCategoryList = homeCategoryList;
    }
    public String getTittle() {
        return tittle;
    }
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
    public List< HomeCategory> getHomeCategoryList() {
        return  homeCategoryList;
    }
    public void setHomeCategoryList(List<HomeCategory>homeCategoryList) {

        this.homeCategoryList=homeCategoryList;
    }


}
