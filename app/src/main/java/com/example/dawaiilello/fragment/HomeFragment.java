package com.example.dawaiilello.fragment;


import static com.example.dawaiilello.fragment.DBqueries.homePageModelList;
import static com.example.dawaiilello.fragment.DBqueries.setFragmentData;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawaiilello.MainActivity;
import com.example.dawaiilello.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.FieldOrBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {


    private horizontalProductScrollAdapter horizontalProductScrollAdapter;
    private ViewALlcaAdapter viewALlcaAdapter;
    private RecyclerView homePageRecycerView;
    private  homePageAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        homePageRecycerView = view.findViewById(R.id.homePageRecyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecycerView.setLayoutManager(testingLayoutManager);
        adapter = new homePageAdapter(homePageModelList);
        homePageRecycerView.setAdapter(adapter);

        if (homePageModelList.size() == 0){
            setFragmentData(adapter,getContext());
        }else{
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().moveTaskToBack(true);
                getActivity().finish();



            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).invalidateOptionsMenu();
    }

}
