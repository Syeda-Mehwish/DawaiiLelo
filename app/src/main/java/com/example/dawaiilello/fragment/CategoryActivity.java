package com.example.dawaiilello.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawaiilello.R;
import com.example.dawaiilello.fragment.HomePageModel;
import com.example.dawaiilello.fragment.homePageAdapter;
import com.example.dawaiilello.fragment.horizontalProductModel;
import com.example.dawaiilello.fragment.sliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView gridView;
    List<profileModel>horizontalProductModelslist;
    profileAdapter gridProductAdapter;
    public static String category;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private TextView title;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        gridView = findViewById(R.id.Categorygridview);
        title=findViewById(R.id.productHead);
        constraintLayout = findViewById(R.id.contraintforItems);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        category =getIntent().getStringExtra("Category_name");
        title.setText(category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        gridView.setLayoutManager(linearLayoutManager);
        horizontalProductModelslist = new ArrayList<>();
        gridProductAdapter = new profileAdapter(this,horizontalProductModelslist);
        gridView.setAdapter(gridProductAdapter);
        firebaseFirestore.collection("Products").whereEqualTo("category_id",category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                   profileModel profileModel = documentSnapshot.toObject(com.example.dawaiilello.fragment.profileModel.class);
                   horizontalProductModelslist.add(profileModel);
                   gridProductAdapter.notifyDataSetChanged();
               }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}