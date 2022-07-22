package com.example.dawaiilello.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawaiilello.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class orderDetailsActivity extends AppCompatActivity {
    private TextView TotalAmount;
    RecyclerView gridView;
    List<orderDetailsModel> horizontalProductModelslist;
    orderDetailsAdapter gridProductAdapter;
    public static String category;
    public static String price;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private TextView estimatedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        gridView = findViewById(R.id.orderDetailRecyclerview);
        TotalAmount =findViewById(R.id.total_cart_amount);
        estimatedTime= findViewById(R.id.estimatedTime);


        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        category =getIntent().getStringExtra("order_id");
        price=getIntent().getStringExtra("price");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        gridView.setLayoutManager(linearLayoutManager);
        horizontalProductModelslist = new ArrayList<>();
        gridProductAdapter = new orderDetailsAdapter(this,horizontalProductModelslist);
        gridView.setAdapter(gridProductAdapter);
        firebaseFirestore.collection("ORDERS").document(category).collection("orederItems").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot orderItems: task.getResult().getDocuments()){
                                orderDetailsModel myOrderItemModel = new orderDetailsModel(  orderItems.get("Product ID").toString(),orderItems.get("Product Image").toString(),orderItems.get("Product Title").toString(),Long.valueOf(orderItems.get("Product Quantity").toString()),orderItems.get("Price").toString());
                                horizontalProductModelslist.add(myOrderItemModel);


                            }
                            gridProductAdapter.notifyDataSetChanged();

                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(orderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        firebaseFirestore.collection("ORDERS").document(category).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   price= task.getResult().get("Total Amount").toString();
                    TotalAmount.setText(price);
                    estimatedTime.setText(task.getResult().get("Estimated Time").toString());

                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(orderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}