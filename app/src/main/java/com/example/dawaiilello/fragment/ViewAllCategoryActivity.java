package com.example.dawaiilello.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dawaiilello.R;

import java.util.ArrayList;
import java.util.List;

import io.grpc.internal.ClientStream;

public class ViewAllCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<horizontalProductModel> horizontalProductModelList;
    public static  List<ViewALlcaModel> viewALlcaModelList;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_category);
        title=findViewById(R.id.view_all_text);
        recyclerView = findViewById(R.id.Recyclerview);
        gridView = findViewById(R.id.gridview);
        int layout_code = getIntent().getIntExtra("layout_code",-1);
        if (layout_code==0) {
            title.setText("Categories");
            gridView.setVisibility(View.INVISIBLE);

            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            ViewALlcaAdapter adapter = new ViewALlcaAdapter(viewALlcaModelList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (layout_code==1) {
            title.setText("Deals");
            gridView.setVisibility(View.VISIBLE);
            GridProductAdapter gridProductAdapter = new GridProductAdapter(horizontalProductModelList);
            gridView.setAdapter(gridProductAdapter);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}