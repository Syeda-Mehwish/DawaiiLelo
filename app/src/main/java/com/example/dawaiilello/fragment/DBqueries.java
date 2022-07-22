package com.example.dawaiilello.fragment;

import android.app.Dialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dawaiilello.Activity_save_money;
import com.example.dawaiilello.R;
import com.example.dawaiilello.fragment.HomePageModel;
import com.example.dawaiilello.fragment.horizontalProductModel;
import com.example.dawaiilello.fragment.sliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public  static FirebaseUser currentUser = firebaseAuth.getCurrentUser();
   public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<HomePageModel>homePageModelList = new ArrayList<>();
    public  static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList =new ArrayList<>();
    public  static List<String> alternateCartList = new ArrayList<>();
    public static List<SaveMoneyItemModel> saveMoneyItemModelList =new ArrayList<>();
    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
    public static List<AlternativeProductModel> alternativeProductModelList=new ArrayList<>();
    public static String NameText;





    public static String profile(){
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_Data").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            NameText = task.getResult().get("fullname").toString();

                        }
                    }
                });
        return NameText;
    }



    public static void setFragmentData(final homePageAdapter adapter, final Context context){

        firebaseFirestore.collection("Categories").document("Multivitamins").collection("Top_Deals")
                .orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                if ((long)documentSnapshot.get("view_type")==0){
                                    List<sliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x =1; x< no_of_banners+1 ;x++){
                                        sliderModelList.add(new sliderModel(documentSnapshot.get("banner_"+x).toString(),
                                                documentSnapshot.get("banner_"+x+"_background").toString()));
                                    }
                                    homePageModelList.add(new HomePageModel(0,sliderModelList));


                                }else if ((long)documentSnapshot.get("view_type")==1){
                                    homePageModelList.add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),
                                            documentSnapshot.get("background").toString()));


                                }else if ((long)documentSnapshot.get("view_type")==2){
                                    firebaseFirestore.collection("HomeCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                List<ViewALlcaModel>ViewAllCategory = new ArrayList<>();
                                                List<HomeCategory> homeCategoryList = new ArrayList<>();
                                                for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                    String Id, Image,Title;
                                                    Id = documentSnapshot.get("category_id").toString();
                                                    Image = documentSnapshot.get("icon").toString();
                                                    Title = documentSnapshot.get("category_name").toString();
                                                    homeCategoryList.add(new HomeCategory(Title,Image,Id));
                                                    ViewAllCategory.add(new ViewALlcaModel(Id,
                                                            Image,
                                                            Title));




                                                } homePageModelList.add(new HomePageModel(2,"Category",homeCategoryList,ViewAllCategory));


                                            }else{
                                                String error = task.getException().getMessage();

                                            }

                                        }
                                    });
                                }else if ((long)documentSnapshot.get("view_type")==3){
                                    List<horizontalProductModel> GridLayoutModelList = new ArrayList<>();
                                    String ID, image, title, subtitle, price;
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x =1; x< no_of_products+1 ;x++){
                                        ID = documentSnapshot.get("product_id_"+x).toString();
                                        image= documentSnapshot.get("product_img_"+x).toString();
                                        title= documentSnapshot.get("product_title_"+x).toString();
                                        subtitle = documentSnapshot.get("product_subtitle_"+x).toString();
                                        price = documentSnapshot.get("product_price_"+x).toString();
                                        GridLayoutModelList.add(new horizontalProductModel(ID,
                                                image,
                                                title,
                                                subtitle,
                                                price));
                                    }
                                    homePageModelList.add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutModelList));

                                }
                            }
                            adapter.notifyDataSetChanged();

                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();


                        }
                    }
                });

    }
    public static void loadCartList(Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount){
        cartList.clear();

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).collection("USER_DATA").document("MY_Cart")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x=0; x< (long)task.getResult().get("list_size");x++){
                        cartList.add(task.getResult().get("product_ID_"+x).toString());
                        if (DBqueries.cartList.contains(ProductDetailsActivity.productID)){
                            ProductDetailsActivity.ALREADY_ADDED_TO_CARTLIST=true;

                        }else{
                            ProductDetailsActivity.ALREADY_ADDED_TO_CARTLIST=false;
                        }
                        if (loadProductData) {
                            cartItemModelList.clear();
                            String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("Products").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        int indexx = 0;
                                       // if (cartList.size()>2){
                                         //   indexx = cartList.size()-2;
                                        //}
                                        cartItemModelList.add(indexx, new CartItemModel(
                                                CartItemModel.CART_ITEM,productId,task.getResult().get("Image").toString(),
                                                task.getResult().get("title").toString(),
                                                task.getResult().get("Price").toString(),(long)1,
                                                (boolean)task.getResult().get("in_stock"),(long)task.getResult().get("max_quantity")));
                                        if (cartItemModelList.size()==1){
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);
                                        }
                                        if (cartList.size()==0){
                                            cartItemModelList.clear();
                                        }
                                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                    if (cartList.size() != 0){
                        badgeCount.setVisibility(View.VISIBLE);
                    }else {
                        badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if (DBqueries.cartList.size()<99) {
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    }else{
                        badgeCount.setText("99");
                    }

                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

        });

    }
    public static void removeFromCart(final int index, final Context context, final TextView cartTotalAmount){

        //////////////////////////////////////////////////
        final String removedProductId = cartItemModelList.get(index).getProductID();
        final String product= String.valueOf(removedProductId);

       // int value = cartList.size()-1;
        //final String removedProductId = cartList.get(value-index);
        cartList.remove(cartList.indexOf(product));//value-index);
        Map<String,Object> updateCartList = new HashMap<>();
        for (int x=0;x<cartList.size();x++){
            updateCartList.put("product_ID_"+x,cartList.get(x));

        }
        updateCartList.put("list_size",(long)cartList.size());
        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).collection("USER_DATA").document("MY_Cart")
        .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (cartItemModelList.size()!=0){
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size()==0){
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartItemModelList.clear();
                    }

                        Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    cartList.add(index,removedProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.running_cartlist_query = false;
            }

        });
    }
    public static void loadAlternateList(Context context, final  Dialog dialog, final boolean loadProductData, final TextView cartTotalAmount ){
        alternateCartList.clear();

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).collection("USER_DATA").document("MY_Alternate")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x=0; x< (long)task.getResult().get("list_size");x++){
                        alternateCartList.add(task.getResult().get("product_ID_"+x).toString());
                        if (loadProductData) {
                            saveMoneyItemModelList.clear();
                            String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("Products").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                    if (task.isSuccessful()) {
                                        int indexxx = 0;

                                        saveMoneyItemModelList.add(indexxx, new SaveMoneyItemModel(
                                                SaveMoneyItemModel.SaveMoney_ITEM,productId,task.getResult().get("Image").toString(),
                                                task.getResult().get("title").toString(),
                                                task.getResult().get("Price").toString(),(long)1,
                                                (boolean)task.getResult().get("in_stock"),(long)task.getResult().get("max_quantity")));
                                        if (saveMoneyItemModelList.size()==1){
                                            saveMoneyItemModelList.add(new SaveMoneyItemModel(SaveMoneyItemModel.SaveMoney_TOTAL_AMOUNT));
                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);
                                        }
                                        if (alternateCartList.size()==0){
                                            alternateCartList.clear();
                                        }

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }


                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

        });


    }
    public static void removeFromAlternateCart(final int index, final Context context, final TextView cartTotalAmount){
        final String removedProductId = cartItemModelList.get(index).getProductID();
        final String product= String.valueOf(removedProductId);


        int value = cartList.indexOf(product);
        int ind=0;
       final String removedAlternateProductId = alternateCartList.get(value);
       for(int x=0;x<alternateCartList.size();x++){
           if (saveMoneyItemModelList.get(x).getProductID().equals(removedAlternateProductId)){
               ind=x;

           }
       }
        alternateCartList.remove(value);
        Map<String,Object> updateCartList = new HashMap<>();
        for (int x=0;x<alternateCartList.size();x++){
            updateCartList.put("product_ID_"+x,alternateCartList.get(x));

        }
        ///////////////////////////////////////////////////
        //////////////////////////////////////////////////
        updateCartList.put("list_size",(long)alternateCartList.size());
        int finalInd = ind;
        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).collection("USER_DATA").document("MY_Alternate")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (saveMoneyItemModelList.size()!=0){
                        saveMoneyItemModelList.remove(finalInd);
  //                        Activity_save_money.SaveMoneyAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();


                }else{
                    alternateCartList.add(index,removedAlternateProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.running_cartlist_query = false;
            }

        });
    }
    public static void loadOrders(Context context, MyOrderAdapter myOrderAdapter){
        myOrderItemModelList.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Users_Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){

                        firebaseFirestore.collection("ORDERS").document(documentSnapshot.getString("Order ID")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    MyOrderItemModel myOrderItemModel = new MyOrderItemModel(task.getResult().get("Order ID").toString(),task.getResult().get("Order Status").toString(),"Address",task.getResult().getDate("Ordered Date"),task.getResult().getDate("Packed Date"),null,task.getResult().getDate("Delivered Date"),"","",task.getResult().get("Total Amount").toString(),(long) (1),"User ID", null,task.getResult().get("Order ID").toString());
                                    myOrderItemModelList.add(myOrderItemModel);
                                    myOrderAdapter.notifyDataSetChanged();

                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void loadAlternate(Context context,String id, String formula){
        alternateCartList = new ArrayList<>();
        final Boolean[] Found = {false};
        firebaseFirestore.collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                firebaseFirestore.collection("Products").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if ((documentSnapshot.get("Formula").equals(formula))&& (!documentSnapshot.get("Product_id").equals(id))){
                                            Found[0] =true;
                                           // alternativeProductModelList = new ArrayList<>();
                                            String ID, image, title, subtitle, price;
                                            ID = documentSnapshot.get("Product_id").toString();
                                            image=documentSnapshot.get("Image").toString();
                                            title= documentSnapshot.get("title").toString();
                                            subtitle = documentSnapshot.get("Formula").toString();
                                            price = documentSnapshot.get("Price").toString();
                                            alternativeProductModelList.add(new AlternativeProductModel(ID,
                                                    image,
                                                    title,
                                                    subtitle,
                                                    price));
                                            ProductDetailsActivity.alternativeProductAdapter.notifyDataSetChanged();

                                        }

                                    }
                                });

                            }
                            if (Found[0] == false){


                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    public static void clearData(){
        cartList.clear();
        alternateCartList.clear();
        myOrderItemModelList.clear();
        cartItemModelList.clear();
        saveMoneyItemModelList.clear();
    }
}
