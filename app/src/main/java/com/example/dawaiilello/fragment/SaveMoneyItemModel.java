package com.example.dawaiilello.fragment;

public class SaveMoneyItemModel {
    public static final int SaveMoney_ITEM = 0;
    public static final int SaveMoney_TOTAL_AMOUNT = 1;

    private int type;



    public int getType() {
        return type;
    }

    public void setType1(int type) {
        this.type = type;
    }



    /////// cart item
    private String productID;
    private boolean inStock;
    private String sm_productImage;
    private String sm_productTitle;
    private String sm_productPrice;
    private long sm_productQuantity;

    private long maxQuantity;


    public SaveMoneyItemModel(int type,String productID, String productImage, String productTitle, String productPrice, long productQuantity, boolean inStock, long maxQuantity) {
        this.type = type;
        this.productID = productID;
        this.sm_productImage = productImage;
        this.sm_productTitle = productTitle;
        this.sm_productPrice = productPrice;
        this.sm_productQuantity = productQuantity;
        this.maxQuantity = maxQuantity;
        this.inStock=inStock;
    }

    public String getsmProductImage() {
        return sm_productImage;
    }

    public void setsmProductImage(String productImage) {
        this.sm_productImage = productImage;
    }

    public String getsmProductTitle() {
        return sm_productTitle;
    }

    public void setsmProductTitle(String productTitle) {
        this.sm_productTitle = productTitle;
    }



    public String getsmProductPrice() {
        return sm_productPrice;
    }

    public void setsmProductPrice(String productPrice) {
        this.sm_productPrice = productPrice;
    }


    public long getsmProductQuantity() {
        return sm_productQuantity;
    }

    public void setsmProductQuantity(long sm_productQuantity) {
        this.sm_productQuantity = sm_productQuantity;
    }


    /////// cart item

    //////  cart total


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public boolean isInStock() {
        return inStock;
    }


    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
    public long getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(long maxQuantity) {
        this.maxQuantity = maxQuantity;
    }


    //////  cart total

    private int totalItems, totalItemsPrice, totalAmount;
    private String deliveryPrice;
    public SaveMoneyItemModel(int type) {
        this.type = type;
    }
    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }



}

