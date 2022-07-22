package com.example.dawaiilello.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dawaiilello.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {
    TextView text;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    public MyOrdersFragment() {
        // Required empty public constructor
    }
    private RecyclerView myOrdersRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_orders, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        myOrdersRecyclerView = view.findViewById(R.id.my_order_recyclerview);
        text = view.findViewById(R.id.orderText);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        MyOrderAdapter myOrderAdapter= new MyOrderAdapter(DBqueries.myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        if (user!=null) {
            if (DBqueries.myOrderItemModelList.size() == 0) {
                DBqueries.loadOrders(getContext(), myOrderAdapter);
            }
        }else{
            text.setVisibility(View.VISIBLE);
            myOrdersRecyclerView.setVisibility(View.GONE);
        }

        return view;
    }
}