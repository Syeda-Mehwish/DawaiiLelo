package com.example.dawaiilello.fragment;

import static com.example.dawaiilello.RegisterActivity.setSignUpFragment;
import static com.example.dawaiilello.fragment.DBqueries.alternateCartList;
import static com.example.dawaiilello.fragment.DBqueries.cartList;
import static com.example.dawaiilello.fragment.DBqueries.currentUser;
import static com.example.dawaiilello.fragment.DBqueries.firebaseAuth;
import static com.example.dawaiilello.fragment.DBqueries.loadAlternateList;
import static com.example.dawaiilello.fragment.DBqueries.loadCartList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.MainActivity;
import com.example.dawaiilello.R;
import com.example.dawaiilello.RegisterActivity;
import com.example.dawaiilello.SignInFragment;
import com.example.dawaiilello.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager productImagesViewpager;


    //////////////////////////////
    private TextView layoutTitle;
    private Button viewAllBtn;
    private RecyclerView HorizontalRecyclerview;
    //////////////////////////////
    private TextView badgeCount;
    public static MenuItem cartItem;
    public static boolean running_cartlist_query = false;
    private  TextView productTitle;
    private  TextView productPrice;
    private  TextView productCuttedprice;
   private FirebaseFirestore firebaseFirestore;
   private Button buyNowbtn;
   private Button addToCartBtn;
   private Dialog signInDialog;
   public static String productID;
   private Dialog loadingDialog;
   private DocumentSnapshot documentSnapshot;
   public static boolean ALREADY_ADDED_TO_CARTLIST = false;
   public static Toolbar toolbar;
   private String productFormula;
   public static List<AlternativeProductModel> alternativeProductModelList = new ArrayList<>();
    public static AlternativeProductAdapter alternativeProductAdapter;
    public String CheckproductPrice;
    private String productID1;
    private ImageView image;
    FirebaseUser currentUser;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_product_details);
        toolbar = (Toolbar)findViewById(R.id.toolbarr) ;

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = findViewById(R.id.productImage);
        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        addToCartBtn =findViewById(R.id.addtocartBtn);
        productCuttedprice = findViewById(R.id.CuttedproductPrice);
        firebaseFirestore = FirebaseFirestore.getInstance();
        productID =getIntent().getStringExtra("Product_ID");


        ///////////////loding dialog/////////////
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitle = findViewById(R.id.horizontal_scroll_layout_title);
        layoutTitle.setText("Alternate Products");
        viewAllBtn = findViewById(R.id.horizontal_scroll_layout_button);
        viewAllBtn.setVisibility(View.INVISIBLE);
        HorizontalRecyclerview = findViewById(R.id.horizontal_scroll_layout_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        HorizontalRecyclerview.setLayoutManager(linearLayoutManager);
        alternativeProductAdapter = new AlternativeProductAdapter(DBqueries.alternativeProductModelList);
        HorizontalRecyclerview.setAdapter(alternativeProductAdapter);
        loadingDialog.show();












        firebaseFirestore.collection("Products").document(productID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                             documentSnapshot = task.getResult();
                             productFormula=documentSnapshot.get("Formula").toString();
                            Glide.with(ProductDetailsActivity.this).load(documentSnapshot.get("Image").toString()).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(image);
                            productTitle.setText(documentSnapshot.get("title").toString());
                            productPrice.setText("Rs."+documentSnapshot.get("Price").toString()+"/-");
                            productCuttedprice.setText("Rs."+documentSnapshot.get("CuttedPrice").toString()+"/-");
                            CheckproductPrice = documentSnapshot.get("Price").toString();
                            DBqueries.loadAlternate(ProductDetailsActivity.this,productID,productFormula);
                            loadingDialog.dismiss();


                            if (currentUser!=null) {
                                if (DBqueries.cartList.size() == 0) {
                                    loadCartList(ProductDetailsActivity.this, loadingDialog,false,badgeCount,new TextView(ProductDetailsActivity.this));
                                }
                                if (DBqueries.alternateCartList.size()==0){
                                    loadAlternateList(ProductDetailsActivity.this, loadingDialog,false,new TextView(ProductDetailsActivity.this));
                                }
                            }else{
                                loadingDialog.dismiss();
                            }

                            if (DBqueries.cartList.contains(productID)){
                                ALREADY_ADDED_TO_CARTLIST=true;
                            }else{
                                ALREADY_ADDED_TO_CARTLIST=false;
                            }
                            if((boolean) documentSnapshot.get("in_stock")){
                                addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isConnected()){
                                            if (currentUser == null) {
                                                signInDialog.show();
                                            } else {
                                                if (!running_cartlist_query) {
                                                    running_cartlist_query = true;
                                                    if (ALREADY_ADDED_TO_CARTLIST) {
                                                        Toast.makeText(ProductDetailsActivity.this, "Already added to cart", Toast.LENGTH_SHORT).show();
                                                        running_cartlist_query = false;

                                                    } else {


                                                        Map<String, Object> addProduct = new HashMap<>();

                                                        addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                        addProduct.put("list_size", (long) DBqueries.cartList.size() + 1);
                                                        /////////////////////////////alternate////////////////////////


                                                        firebaseFirestore.collection("Users").document(currentUser.getUid()).collection("USER_DATA").document("MY_Cart")
                                                                .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    if (DBqueries.cartItemModelList.size() != 0) {
                                                                        DBqueries.cartItemModelList.add(0, new CartItemModel(
                                                                                CartItemModel.CART_ITEM, productID, documentSnapshot.get("Image").toString(),
                                                                                documentSnapshot.get("title").toString(),
                                                                                documentSnapshot.get("Price").toString(), (long) 1, (boolean) documentSnapshot.get("in_stock"), (long) documentSnapshot.get("max_quantity")));
                                                                    }

                                                                    firebaseFirestore.collection("Products").get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        Boolean exists = false;

                                                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                                                            if (documentSnapshot.get("Formula").equals(productFormula) && (!documentSnapshot.get("Product_id").equals(productID)) && (Integer.parseInt(CheckproductPrice) > (Integer.parseInt(documentSnapshot.get("Price").toString())))) {
                                                                                                exists = true;
                                                                                                Map<String, Object> addAlternateProduct = new HashMap<>();
                                                                                                addAlternateProduct.put("product_ID_" + String.valueOf(DBqueries.alternateCartList.size()), documentSnapshot.get("Product_id"));
                                                                                                addAlternateProduct.put("list_size", (long) DBqueries.alternateCartList.size() + 1);
                                                                                                productID1 = documentSnapshot.get("Product_id").toString();
                                                                                                firebaseFirestore.collection("Users").document(currentUser.getUid()).collection("USER_DATA").document("MY_Alternate")
                                                                                                        .update(addAlternateProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            if (DBqueries.saveMoneyItemModelList.size() != 0) {
                                                                                                                DBqueries.saveMoneyItemModelList.add(0, new SaveMoneyItemModel(
                                                                                                                        SaveMoneyItemModel.SaveMoney_ITEM, productID1, documentSnapshot.get("Image").toString(),
                                                                                                                        documentSnapshot.get("title").toString(),
                                                                                                                        documentSnapshot.get("Price").toString(), (long) 1, (boolean) documentSnapshot.get("in_stock"), (long) documentSnapshot.get("max_quantity")));

                                                                                                            }
                                                                                                            DBqueries.alternateCartList.add(productID1);

                                                                                                        } else {
                                                                                                            String error = task.getException().getMessage();
                                                                                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                                                        }

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                        if (exists == false) {

                                                                                            firebaseFirestore.collection("Users").document(currentUser.getUid()).collection("USER_DATA").document("MY_Alternate")
                                                                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                    String alternate = task.getResult().get("list_size").toString();
                                                                                                    Map<String, Object> addAlternateProduct = new HashMap<>();
                                                                                                    addAlternateProduct.put("product_ID_" + alternate, productID);
                                                                                                    addAlternateProduct.put("list_size", Long.valueOf(alternate) + 1);
                                                                                                    firebaseFirestore.collection("Users").document(currentUser.getUid()).collection("USER_DATA").document("MY_Alternate")
                                                                                                            .update(addAlternateProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (DBqueries.saveMoneyItemModelList.size() != 0) {
                                                                                                                    DBqueries.saveMoneyItemModelList.add(0, new SaveMoneyItemModel(
                                                                                                                            SaveMoneyItemModel.SaveMoney_ITEM, productID, documentSnapshot.get("Image").toString(),
                                                                                                                            documentSnapshot.get("title").toString(),
                                                                                                                            documentSnapshot.get("Price").toString(), (long) 1, (boolean) documentSnapshot.get("in_stock"), (long) documentSnapshot.get("max_quantity")));

                                                                                                                }
                                                                                                                DBqueries.alternateCartList.add(productID);

                                                                                                            } else {
                                                                                                                String error = task.getException().getMessage();
                                                                                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                                                            }

                                                                                                        }
                                                                                                    });


                                                                                                }
                                                                                            });


                                                                                            Toast.makeText(ProductDetailsActivity.this, "not found", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    } else {
                                                                                        String error = task.getException().getMessage();
                                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                }
                                                                            });


                                                                    ALREADY_ADDED_TO_CARTLIST = true;
                                                                    DBqueries.cartList.add(productID);
                                                                    Toast.makeText(ProductDetailsActivity.this, "Product added successfully    ", Toast.LENGTH_SHORT).show();
                                                                    invalidateOptionsMenu();
                                                                    running_cartlist_query = false;
                                                                } else {
                                                                    running_cartlist_query = false;
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                    }else{
                                            Toast.makeText(ProductDetailsActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }else{
                                addToCartBtn.setText("Out Of Stock");
                                addToCartBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                                addToCartBtn.setCompoundDrawables(null,null,null,null);
                            }

                        }else{
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



viewAllBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {



        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
       HorizontalRecyclerview.invalidate();
        HorizontalRecyclerview.setAdapter(alternativeProductAdapter);




    }
});
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.signin_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signupBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });


    }
    @Override
    protected void onStart()
    {
        super.onStart();
        //////////////////////
       currentUser = FirebaseAuth.getInstance().getCurrentUser();
       /* firebaseFirestore.collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Boolean Found = false;
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                if ((documentSnapshot.get("Formula").equals(productFormula))&& (!documentSnapshot.get("Product_id").equals(productID))){
                                    Found=true;
                                    alternativeProductModelList = new ArrayList<>();
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
                                    alternativeProductAdapter.notifyDataSetChanged();
                                }
                            }
                            if (Found == false){
                                Toast.makeText(ProductDetailsActivity.this, "no alternate found", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/




        if (DBqueries.cartList.contains(productID)){
            ALREADY_ADDED_TO_CARTLIST=true;
            running_cartlist_query = false;
        }else{
            ALREADY_ADDED_TO_CARTLIST=false;
        }
        invalidateOptionsMenu();

    }



    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        cartItem = menu.findItem(R.id.main_cart_icon);



            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.ic_cart);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
            if (currentUser !=null){
                if (DBqueries.alternateCartList.size() == 0) {
                    DBqueries.loadAlternateList(ProductDetailsActivity.this,loadingDialog,false,new TextView(ProductDetailsActivity.this));
                }
                if (DBqueries.cartList.size() == 0) {
                    DBqueries.loadCartList(ProductDetailsActivity.this,loadingDialog,false, badgeCount,new TextView(ProductDetailsActivity.this));
                }else{
                    badgeCount.setVisibility(View.VISIBLE);
                    if (DBqueries.cartList.size()<99) {
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    }else{
                        badgeCount.setText("99");
                    }
                }
            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser==null){
                        signInDialog.show();
                    }else{
                        Intent cartIntent = new Intent(ProductDetailsActivity.this,MainActivity.class);
                        startActivity(cartIntent);

                    }
                }
            });

        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }


        if (id == R.id.main_search_icon) {
            return true;
        }
        if (id == R.id.main_cart_icon) {
            if (currentUser==null){
                signInDialog.show();
            }else{
                Intent cartIntent = new Intent(ProductDetailsActivity.this,MainActivity.class);
                startActivity(cartIntent);
                return true;

            }

        }
        return  super.onOptionsItemSelected(item);

    }
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        /* NetworkInfo is deprecated in API 29 so we have to check separately for higher API Levels */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Network network = cm.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
            if (networkCapabilities == null) {
                return false;
            }
            boolean isInternetSuspended = !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED);
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    && !isInternetSuspended;
        } else {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }


}