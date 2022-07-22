package com.example.dawaiilello.fragment;

public class ViewALlcaModel {
    private String category_Id;
    private String productImage;
    private String productTitle;
    public ViewALlcaModel(String category_Id,String productImage, String productTitle){
        this.category_Id = category_Id;
        this.productImage = productImage;
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }
}
