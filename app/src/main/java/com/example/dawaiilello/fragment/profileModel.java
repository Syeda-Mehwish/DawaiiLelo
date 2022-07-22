package com.example.dawaiilello.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class profileModel {
    String Product_id;
    String Image;
    String title;
    String Formula;
    String Price;
    String CuttedPrice;
    String category_id;
    String index;

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    private ArrayList<String> tags;
    public profileModel(){

    }


    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormula() {
        return Formula;
    }

    public void setFormula(String formula) {
        Formula = formula;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCuttedPrice() {
        return CuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        CuttedPrice = cuttedPrice;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public profileModel(String product_id, String image, String title, String formula, String price, String cuttedPrice, String category_id, String index) {
        Product_id = product_id;
        Image = image;
        this.title = title;
        Formula = formula;
        Price = price;
        CuttedPrice = cuttedPrice;
        this.category_id = category_id;
        this.index = index;
    }
}
