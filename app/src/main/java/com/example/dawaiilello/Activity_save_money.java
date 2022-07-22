package com.example.dawaiilello;

import static com.example.dawaiilello.fragment.DBqueries.currentUser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawaiilello.fragment.AddressActivity;
import com.example.dawaiilello.fragment.CartItemModel;
import com.example.dawaiilello.fragment.DBqueries;
//import com.example.dawaiilello.fragment.DebaitOrCreditPayment;
import com.example.dawaiilello.fragment.MyCartFragment;
import com.example.dawaiilello.fragment.SaveMoneyAdapter;
import com.example.dawaiilello.fragment.SaveMoneyItemModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Activity_save_money extends AppCompatActivity {

    private String mParam1;
    private String mParam2;
    private Button continueBtn;
    private Dialog loadingDialog;
    private TextView price;
    FirebaseFirestore firebaseFirestore;
    private String NameText;
    public static String orderId;
    String token;
    int kilometers;
    String time;
    String token1;
    EditText editText;
    TextView or;
    Button live;



    private RecyclerView SaveMoneyItemsRecyclerView;
    public static SaveMoneyAdapter SaveMoneyAdapter;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_money);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        continueBtn =  findViewById(R.id.cart_contine_btn);
        price = findViewById(R.id.total_cart_amount);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        loadingDialog = new Dialog(Activity_save_money.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.slider_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_save_money.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.custom, null);
                builder.setView(customLayout);
                editText = customLayout.findViewById(R.id.address);
                live = customLayout.findViewById(R.id.live);
                or = customLayout.findViewById(R.id.or);
                builder.setMessage("Enter your Address");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(Activity_save_money.this, editText.getText().toString(), Toast.LENGTH_LONG).show();
                                if (!TextUtils.isEmpty(editText.getText())) {
                                    UUID id = UUID.randomUUID();
                                    orderId = id.toString();
                                    placeOrderDetails();

                                } else {
                                    Toast.makeText(Activity_save_money.this, "Please Enter Address", Toast.LENGTH_LONG).show();
                                }


                                dialogInterface.cancel();

                            }

                        });
                live.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////////////////////////////
                        UUID id = UUID.randomUUID();
                        orderId = id.toString();
                        placeOrderDetails();

                    }
                });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ///////////////////////////
                                dialogInterface.cancel();
                                //////////////////////////////
                            }
                        });
                AlertDialog dialog = null;

                try {
                    dialog = builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dialog != null)
                    dialog.show();


            }else{
                    Toast.makeText(Activity_save_money.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        SaveMoneyItemsRecyclerView = (RecyclerView) findViewById(R.id.save_money_items_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SaveMoneyItemsRecyclerView.setLayoutManager(layoutManager);
        if (DBqueries.saveMoneyItemModelList.size()==0){
            DBqueries.alternateCartList.clear();
            DBqueries.loadAlternateList(Activity_save_money.this,loadingDialog,true,price);
        }else {
            if (DBqueries.saveMoneyItemModelList.get(DBqueries.saveMoneyItemModelList.size()-1).getType()== SaveMoneyItemModel.SaveMoney_TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) price.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }

        SaveMoneyAdapter= new SaveMoneyAdapter(DBqueries.saveMoneyItemModelList,price);
        SaveMoneyItemsRecyclerView.setAdapter(SaveMoneyAdapter);
        SaveMoneyAdapter.notifyDataSetChanged();


    }

    private void cod(String time, String id){
        FirebaseFirestore.getInstance().collection("Users").document(currentUser.getUid()).collection("USER_DATA").document("MY_Data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                token=task.getResult().get("token").toString();

            }
        });
        FirebaseFirestore.getInstance().collection("Users").document("EL9c6jweU8YEyCYCjFgLzdeHTZy2").collection("USER_DATA").document("MY_Data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                token1=task.getResult().get("token").toString();
            }
        });
        Map<String,Object>userOrder = new HashMap<>();
        userOrder.put("Order ID",orderId);
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Users_Orders").document(orderId).set(userOrder)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DBqueries.cartItemModelList.clear();
                            DBqueries.cartList.clear();
                            DBqueries.alternateCartList.clear();
                            DBqueries.saveMoneyItemModelList.clear();
                            Map<String,Object> cartListMap = new HashMap<>();
                            cartListMap.put("list_size",(long)0);
                            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("USER_DATA").document("MY_Cart").set(cartListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Map<String,Object>AlternateCartList = new HashMap<>();
                                        AlternateCartList.put("list_size",(long)0);
                                        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("USER_DATA").document("MY_Alternate").set(AlternateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }

                                            }
                                        });

                                    }

                                }
                            });


                            if (!TextUtils.isEmpty(editText.getText())){
                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Order Placed",orderId,getApplicationContext(),Activity_save_money.this);
                                notificationsSender.SendNotifications();
                                FcmNotificationsSender notificationsSenderr=new FcmNotificationsSender(token1,"Order Placed",orderId,getApplicationContext(),Activity_save_money.this);
                                notificationsSenderr.SendNotifications();
                                Intent ii = new Intent(Activity_save_money.this, DekiveryActivity.class);
                                ii.putExtra("order_id",orderId);
                                ii.putExtra("time",time);
                                ii.putExtra("address","manual");
                                startActivity(ii);
                            }else{
                                Intent i = new Intent(Activity_save_money.this, AddressActivity.class);
                                i.putExtra("intent",orderId);
                                i.putExtra("token",token);
                                i.putExtra("token1",token1);
                                startActivity(i);

                            }
                        }else{
                            Toast.makeText(Activity_save_money.this, "failed to update user", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



    }
    private void placeOrderDetails(/*String address,double latitude, double logitude*/){

        if (!TextUtils.isEmpty(editText.getText())){

            FirebaseFirestore.getInstance().collection("Riders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        String keyword= documentSnapshot.get("AssignedArea").toString();
                        Boolean found = editText.getText().toString().toUpperCase().contains(keyword.toUpperCase());
                        if(found){
                            String id = documentSnapshot.get("Rider_id").toString();
                            Map<String,Object> userOrder = new HashMap<>();
                            userOrder.put("Order ID",orderId);
                            FirebaseFirestore.getInstance().collection("Riders").document(id).collection("orders").document(orderId).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                    }

                }
            });

            if(editText.getText().toString().contains("latifabad")||editText.getText().toString().contains("Latifabad")){
                kilometers=12;
            }else if((editText.getText().toString().contains("Qasimabad")||editText.getText().toString().contains("qasimabad"))){
                kilometers=30;
            }else if((editText.getText().toString().contains("Jamshoro")||editText.getText().toString().contains("jamshoro"))){
                kilometers=1;
                time="hour";
            }else{
                kilometers=30;
            }

        }
        String userID = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (SaveMoneyItemModel saveMoneyItemModel : DBqueries.saveMoneyItemModelList) {
           if( saveMoneyItemModel.getType() == SaveMoneyItemModel.SaveMoney_ITEM) {
               Map<Object,Object> orderDetails = new HashMap<>();
               orderDetails.put("Order ID", orderId);
               orderDetails.put("Product ID", saveMoneyItemModel.getProductID());
               orderDetails.put("Product Image",saveMoneyItemModel.getsmProductImage());
               orderDetails.put("Product Title",saveMoneyItemModel.getsmProductTitle());
               orderDetails.put("User ID", userID);
               orderDetails.put("Product Quantity", saveMoneyItemModel.getsmProductQuantity());
               orderDetails.put("Price", saveMoneyItemModel.getsmProductPrice());
               orderDetails.put("Ordered Date", FieldValue.serverTimestamp());
               orderDetails.put("Packed Date", FieldValue.serverTimestamp());
               orderDetails.put("Shipped Date", FieldValue.serverTimestamp());
               orderDetails.put("Delivered Date", FieldValue.serverTimestamp());
               orderDetails.put("Payment Method", "Cash ON Delivery");
               orderDetails.put("Address", editText.getText().toString());
               orderDetails.put("Order Status","ordered");
               firebaseFirestore.collection("Products").document(saveMoneyItemModel.getProductID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       long quantity = (long)task.getResult().get("max_quantity");
                       quantity = quantity-saveMoneyItemModel.getsmProductQuantity();
                       Map<String,Object> quan = new HashMap<>();
                       quan.put("max_quantity",quantity);
                       if (quantity==0){
                           quan.put("in_stock",(boolean)false);
                       }
                       firebaseFirestore.collection("Products").document(saveMoneyItemModel.getProductID()).update(quan).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                           }
                       });



                   }
               });


               firebaseFirestore.collection("ORDERS").document(orderId).collection("orederItems").document(saveMoneyItemModel.getProductID())
                       .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){

                       }else{
                           String error = task.getException().getMessage();
                           Toast.makeText(Activity_save_money.this, error, Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }else{
               Map<String,Object> orderDetails = new HashMap<>();
               orderDetails.put("Ordered Date", FieldValue.serverTimestamp());
               orderDetails.put("Packed Date", FieldValue.serverTimestamp());
               orderDetails.put("Shipped Date", FieldValue.serverTimestamp());
               orderDetails.put("Delivered Date", FieldValue.serverTimestamp());
               orderDetails.put("Order ID", orderId);
               orderDetails.put("User ID", userID);
               orderDetails.put("Total Items", saveMoneyItemModel.getTotalItems());
               orderDetails.put("Total Items Price", saveMoneyItemModel.getTotalItemsPrice());
               orderDetails.put("Total Amount", saveMoneyItemModel.getTotalAmount());
               orderDetails.put("Delivery Charges", saveMoneyItemModel.getDeliveryPrice());
               orderDetails.put("Payment Status", "Not Paid");
               orderDetails.put("Order Status", "Ordered");
               orderDetails.put("Address", editText.getText().toString());

               orderDetails.put("Latitude",(double)0);
               orderDetails.put("Longitude",(double)0);
               orderDetails.put("Estimated Time",String.valueOf(kilometers+" "+time));
                          firebaseFirestore.collection("ORDERS").document(orderId)
                       .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           cod(String.valueOf(kilometers+" "+time),orderId);
                       }else{
                           String error = task.getException().getMessage();
                           Toast.makeText(Activity_save_money.this, error, Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }//cod();
        }

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