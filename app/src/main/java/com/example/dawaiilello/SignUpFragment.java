package com.example.dawaiilello;

import static com.example.dawaiilello.R.color.colorAccent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {



    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView AlreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText fullName;
    private  EditText password;
    private EditText confirmPassword;
    private ImageButton closeBtn;
    private Button signUpbtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public  static boolean disableCloseBtn =false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        AlreadyHaveAnAccount = view.findViewById(R.id.tvAlreadyHaveAnAccount);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.SignUpEmailAddress);
        fullName=view.findViewById(R.id.SignUpName);
        password=view.findViewById(R.id.SignUpPassword);
        confirmPassword = view.findViewById(R.id.signUpConfirmPassword);
        closeBtn = view.findViewById(R.id.signUpclosebutton);
        signUpbtn = view.findViewById(R.id.SignupButton);
        progressBar = view.findViewById(R.id.SignUpprogressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else{
            closeBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainIntent();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                checkEmailAndPassword();

            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(fullName.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length()>=8){
                    if (!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpbtn.setEnabled(true);
                        signUpbtn.setTextColor(Color.argb(255,255,255,255));

                    }else{
                        signUpbtn.setEnabled(false);
                        signUpbtn.setTextColor(Color.argb(50,255,255,255));
                    }

                }else{
                    signUpbtn.setEnabled(false);
                    signUpbtn.setTextColor(Color.argb(50,255,255,255));
                }

            }else{
                signUpbtn.setEnabled(false);
                signUpbtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signUpbtn.setEnabled(false);
            signUpbtn.setTextColor(Color.argb(50,255,255,255));

        }
    }
    private void checkEmailAndPassword(){
        if (email.getText().toString().matches(emailPattern)){
            if (password.getText().toString().equals(confirmPassword.getText().toString())){
                progressBar.setVisibility(View.VISIBLE);
                signUpbtn.setEnabled(false);
                signUpbtn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    Map<String,Object> userdata = new HashMap<>();
                                    userdata.put("fullname",fullName.getText().toString());
                                    userdata.put("Email",email.getText().toString());
                                    firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Map<String,Object> pres = new HashMap<>();
                                                        pres.put("status","");
                                                        firebaseFirestore.collection("prescription").document(firebaseAuth.getUid()).set(pres).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                        CollectionReference userDataReference =  firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).collection("USER_DATA");
                                                        List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_Cart");
                                                        documentNames.add("MY_Alternate");
                                                        documentNames.add("MY_Data");
                                                        Map<String,Object> cartListMap = new HashMap<>();
                                                        cartListMap.put("list_size",(long)0);
                                                        Map<String,Object>AlternateCartList = new HashMap<>();
                                                        AlternateCartList.put("list_size",(long)0);
                                                        Map<String,Object>Userdata = new HashMap<>();
                                                        Userdata.put("fullname",fullName.getText().toString());
                                                        Userdata.put("Email",email.getText().toString());
                                                        Userdata.put("profile","");
                                                        List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(cartListMap);
                                                        documentFields.add(AlternateCartList);
                                                        documentFields.add(Userdata);
                                                        for (int x=0; x< documentNames.size();x++){
                                                            int finalX = x;
                                                            userDataReference.document(documentNames.get(x)).set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        // progressBar.setVisibility(View.INVISIBLE);
                                                                        if (finalX == documentNames.size()-1) {

                                                                            mainIntent();
                                                                        }

                                                                    }else{
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        signUpbtn.setEnabled(true);
                                                                        signUpbtn.setTextColor(Color.argb(255,255,255,255));
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }else{

                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });


                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpbtn.setEnabled(true);
                                    signUpbtn.setTextColor(Color.argb(255,255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else{
                confirmPassword.setError("Password doesn't matched!");

            }
        }else{
            email.setError("Invalid Email!");

        }
    }

    private void mainIntent(){
        if (disableCloseBtn){
            disableCloseBtn=false;

        }else{
            Intent mainIntent = new Intent(getActivity(),MainActivity.class);
            startActivity(mainIntent);


        }
        disableCloseBtn=false;
        getActivity().finish();

    }
}