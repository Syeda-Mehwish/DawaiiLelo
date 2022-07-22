package com.example.dawaiilello.fragment;

import static android.app.Activity.RESULT_OK;

import static com.example.dawaiilello.fragment.DBqueries.alternateCartList;
import static com.example.dawaiilello.fragment.DBqueries.cartItemModelList;
import static com.example.dawaiilello.fragment.DBqueries.cartList;
import static com.example.dawaiilello.fragment.DBqueries.currentUser;
import static com.example.dawaiilello.fragment.DBqueries.saveMoneyItemModelList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawaiilello.Activity_save_money;
import com.example.dawaiilello.DekiveryActivity;
import com.example.dawaiilello.FcmNotificationsSender;
import com.example.dawaiilello.MainActivity;
import com.example.dawaiilello.ProfileFragment;
import com.example.dawaiilello.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class MyCartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button continueBtn;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton jazzcash;
    private ImageButton easypaisa;
    private ImageButton bank;
    private TextView price;
    private Button saveBtn;
    private  TextView totalAmount;
    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot documentSnapshot;
    String NameText;
     public static String orderID;
    String token;
    String token1;
    String time="min";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyCartFragment() {
        // Required empty public constructor
    }
    ///////////////////////////////////////
    private RecyclerView cartItemsRecyclerView;
///////////////////////////////////////
    public static CartAdapter cartAdapter;
    String productID;
    String productFormula;
    long quantity;
    String productID1;
    int kilometers;
    EditText editText;
    Button live;
    TextView or;

///////////////////////////////////////////////

    FusedLocationProviderClient mFusedLocationClient;


    ////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) ProductDetailsActivity.toolbar;

        continueBtn =  view.findViewById(R.id.cart_contine_btn);
        saveBtn = view.findViewById(R.id.cart_save_money_btn);
        totalAmount =view.findViewById(R.id.total_cart_amount);
        price = view.findViewById(R.id.total_cart_amount);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


        loadingDialog = new Dialog(getActivity());
       loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.slider_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        ////////////////////////////////////////////////////////////
        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

                                    Toast.makeText(getContext(), editText.getText().toString(), Toast.LENGTH_LONG).show();
                                    if (!TextUtils.isEmpty(editText.getText())) {
                                        UUID id = UUID.randomUUID();
                                        orderID = id.toString();
                                        placeOrderDetails();

                                    } else {
                                        Toast.makeText(getContext(), "Please Enter Address", Toast.LENGTH_LONG).show();
                                    }


                                    dialogInterface.cancel();

                                }

                            });
                    live.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ////////////////////////////
                            UUID id = UUID.randomUUID();
                            orderID = id.toString();
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
                    Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });



        firebaseFirestore =FirebaseFirestore.getInstance();
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), Activity_save_money.class);
                getActivity().startActivity(i);
            }
        });
        ////////////////////////////////////////////////////////////
        loadingDialog.show();
        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);


        if (cartItemModelList.size()==0){
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(),loadingDialog,true, new TextView(getContext()),totalAmount);
            LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
            parent.setVisibility(View.VISIBLE);
        }else {

            if (cartItemModelList.get(cartItemModelList.size()-1).getType()==CartItemModel.TOTAL_AMOUNT){

                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }

        //////////////////////////////////////////////////////////////

        cartAdapter= new CartAdapter(cartItemModelList, totalAmount);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        ///////////////////////////////////////////////////////////
        if (DBqueries.saveMoneyItemModelList.size()==0){
            DBqueries.alternateCartList.clear();
            DBqueries.loadAlternateList(getContext(),loadingDialog,true,price);
        }



        return view;

    }



    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

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
        userOrder.put("Order ID",orderID);
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Users_Orders").document(orderID).set(userOrder)
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


                            if (token==null){
                                Toast.makeText(getContext(), "failed to update user", Toast.LENGTH_SHORT).show();
                            }

                            loadingDialog.dismiss();

                            if (!TextUtils.isEmpty(editText.getText())){
                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Order Placed",orderID,getContext(),getActivity());
                                notificationsSender.SendNotifications();
                                 FcmNotificationsSender notificationsSenderr=new FcmNotificationsSender(token1,"Order Placed",orderID,getContext(),getActivity());
                                 notificationsSenderr.SendNotifications();
                                Intent ii = new Intent(getContext(), DekiveryActivity.class);
                                ii.putExtra("order_id",orderID);
                                ii.putExtra("time",time);
                                ii.putExtra("address","manual");
                                startActivity(ii);
                            }else{
                                Intent i = new Intent(getContext(), AddressActivity.class);
                                i.putExtra("intent",orderID);
                                i.putExtra("token",token);
                                i.putExtra("token1",token1);
                                startActivity(i);
                            }

                        }else{
                            Toast.makeText(getContext(), "failed to update user", Toast.LENGTH_SHORT).show();
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
                        userOrder.put("Order ID",orderID);
                        FirebaseFirestore.getInstance().collection("Riders").document(id).collection("orders").document(orderID).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }

                }

            }
        });

            if(editText.getText().toString().contains("latifabad")||editText.getText().toString().contains("Latifabad")){
                kilometers=4;
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

        for (CartItemModel cartItemModel : cartItemModelList) {
            if( cartItemModel.getType() == CartItemModel.CART_ITEM) {
                Map<Object,Object> orderDetails = new HashMap<>();
                orderDetails.put("Order ID", orderID);
                orderDetails.put("Product ID", cartItemModel.getProductID());
                orderDetails.put("Product Image",cartItemModel.getProductImage());
                orderDetails.put("Product Title",cartItemModel.getProductTitle());
                orderDetails.put("User ID", userID);
                orderDetails.put("Product Quantity", cartItemModel.getProductQuantity());
                orderDetails.put("Price", cartItemModel.getProductPrice());
                orderDetails.put("Ordered Date", FieldValue.serverTimestamp());
                orderDetails.put("Packed Date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped Date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered Date", FieldValue.serverTimestamp());
                orderDetails.put("Payment Method", "Cash ON Delivery");
                orderDetails.put("Address", editText.getText().toString());
                orderDetails.put("Order Status","ordered");
                firebaseFirestore.collection("Products").document(cartItemModel.getProductID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        long quantity = (long)task.getResult().get("max_quantity");
                        quantity = quantity-cartItemModel.getProductQuantity();
                        Map<String,Object> quan = new HashMap<>();
                        quan.put("max_quantity",quantity);
                        if (quantity==0){
                            quan.put("in_stock",(boolean)false);
                        }
                        firebaseFirestore.collection("Products").document(cartItemModel.getProductID()).update(quan).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });



                    }
                });


                firebaseFirestore.collection("ORDERS").document(orderID).collection("orederItems").document(cartItemModel.getProductID())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{

                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("Ordered Date", FieldValue.serverTimestamp());
                orderDetails.put("Packed Date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped Date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered Date", FieldValue.serverTimestamp());

                orderDetails.put("Order ID", orderID);
                orderDetails.put("User ID", userID);
                orderDetails.put("Total Items", cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price", cartItemModel.getTotalItemsPrice());
                orderDetails.put("Total Amount", cartItemModel.getTotalAmount());
                orderDetails.put("Delivery Charges", cartItemModel.getDeliveryPrice());
                orderDetails.put("Payment Status", "Not Paid");
                orderDetails.put("Order Status", "Ordered");
                orderDetails.put("Address", editText.getText().toString());

               orderDetails.put("Latitude",(double)0);
                orderDetails.put("Longitude",(double)0);
                orderDetails.put("Estimated Time",String.valueOf(kilometers+" "+time));


                firebaseFirestore.collection("ORDERS").document(orderID)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cod(String.valueOf(kilometers+" "+time),orderID);
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }//cod();
        }

    }


}