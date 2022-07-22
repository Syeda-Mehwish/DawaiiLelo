package com.example.dawaiilello;

import static com.example.dawaiilello.fragment.DBqueries.firebaseAuth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawaiilello.fragment.DBqueries;
import com.example.dawaiilello.fragment.profileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {


    private ImageView Img;
    private TextView Name;
    private TextView Name1;
    private TextView Num;
    private TextView Email;
    private TextView Addr;;
    private TextView editProfile;
    private TextView logOut;
    public static String NameText;
    private String AddressText;
    private String NumberText;

    private String EmailText;
    List<profileModel> profileModelList = new ArrayList<>();
  FirebaseFirestore firebaseFirestore;
  FirebaseAuth firebaseAuth;
  FirebaseUser user;
//  FirebaseUser currentUser = firebaseAuth.getCurrentUser();



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Img = view.findViewById(R.id.Image);
        Name = view.findViewById(R.id.name);
        Name1 = view.findViewById(R.id.name1);
        Num = view.findViewById(R.id.num);
        Email= view.findViewById(R.id.email);
        Addr = view.findViewById(R.id.addr);
        editProfile = view.findViewById(R.id.editProfile);
        logOut=view.findViewById(R.id.signOut);
        if(user!=null){
            firebaseFirestore.collection("Users").document(user.getUid()).collection("USER_DATA").document("MY_Data").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                NameText=task.getResult().get("fullname").toString();
                                EmailText = task.getResult().get("Email").toString();
                                if(task.getResult().get("profile").toString().equals("set")){
                                    AddressText = task.getResult().get("Address").toString();
                                    NumberText = task.getResult().get("Number").toString();
                                }else{
                                    AddressText ="";
                                    NumberText="";
                                }


                                Name1.setText(NameText);
                                Email.setText(EmailText);
                                Name.setText(NameText);
                                Addr.setText(AddressText);
                                Num.setText(NumberText);

                            }else{

                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }






        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updRiderIntent = new Intent(view.getContext(),UpdateProfileActivity.class);
                view.getContext().startActivity(updRiderIntent);

            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent registerIntent = new Intent(getContext(),MainActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment

        return view;
    }
}