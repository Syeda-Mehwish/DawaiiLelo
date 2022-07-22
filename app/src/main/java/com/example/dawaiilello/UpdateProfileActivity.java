package com.example.dawaiilello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    private ImageView UpdImg;
    private EditText UpdName;
    private EditText UpdNum;
    private EditText UpdEmail;
    private EditText UpdAddr;
    private MaterialButton updateBtn;

    private String nametext;
    private String emailText;
    private String addressText;
    private String numberText;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        UpdImg = findViewById(R.id.UpdImage);
        UpdName = findViewById(R.id.UpdName);
        UpdNum = findViewById(R.id.UpdNum);
        UpdEmail= findViewById(R.id.UpdEmail);
        UpdAddr = findViewById(R.id.UpdAddr);
        updateBtn = findViewById(R.id.UpdBtn);


        firebaseFirestore.collection("Users").document(user.getUid()).collection("USER_DATA").document("MY_Data").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            nametext=task.getResult().get("fullname").toString();
                            emailText = task.getResult().get("Email").toString();
                            if(task.getResult().get("profile").toString().equals("set")){
                                addressText = task.getResult().get("Address").toString();
                                numberText = task.getResult().get("Number").toString();
                            }else{
                                addressText ="";
                                numberText="";
                            }


                            UpdName.setText(nametext);
                            UpdEmail.setText(emailText);
                            UpdAddr.setText(addressText);
                            UpdNum.setText(numberText);

                        }else{

                            String error = task.getException().getMessage();
                            Toast.makeText(UpdateProfileActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> Userdata = new HashMap<>();
                Userdata.put("fullname",UpdName.getText().toString());
                Userdata.put("Email",UpdEmail.getText().toString());
                Userdata.put("profile","set");
                Userdata.put("Address",UpdAddr.getText().toString());
                Userdata.put("Number",UpdNum.getText().toString());
                firebaseFirestore.collection("Users").document(user.getUid()).collection("USER_DATA").document("MY_Data").set(Userdata)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(UpdateProfileActivity.this, "update successfully", Toast.LENGTH_SHORT).show();

                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(UpdateProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

    }
}