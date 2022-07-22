package com.example.dawaiilello;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dawaiilello.fragment.AddressActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {
    private String imageUrl;
    private Uri imageUri;
    private ImageView close;
    private ImageView image_uploded;
    private TextView upload;
    int kilometers;
    String token;
    String time ="min";
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FusedLocationProviderClient mFusedLocationClient;
    EditText editText;
    Button live;
    TextView or;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        close = findViewById(R.id.close);
        image_uploded = findViewById(R.id.image_uploded);
        upload = findViewById(R.id.upload);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, prescriptionActivity.class));
                finish();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.custom, null);
                builder.setView(customLayout);
                editText = customLayout.findViewById(R.id.address);
                live=customLayout.findViewById(R.id.live);
                or=customLayout.findViewById(R.id.or);
                builder.setMessage("Enter your Address");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(PostActivity.this,editText.getText().toString(),Toast.LENGTH_LONG).show();
                                if (!TextUtils.isEmpty(editText.getText())){
                                   uploaded();

                                }else{
                                    Toast.makeText(PostActivity.this,"Please Enter Address",Toast.LENGTH_LONG).show();
                                }




                                dialogInterface.cancel();

                            }

                        });
                live.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       uploaded();

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
                    dialog= builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(dialog!=null)
                    dialog.show();
            }
        });
        CropImage.activity().start(PostActivity.this);
    }

    private  void uploaded(/*String address,double latitude, double logitude*/){
        FirebaseFirestore.getInstance().collection("Users").document("EL9c6jweU8YEyCYCjFgLzdeHTZy2").collection("USER_DATA").document("MY_Data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                token=task.getResult().get("token").toString();
            }
        });


        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (!TextUtils.isEmpty(editText.getText())){
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
        if(imageUri!= null){


            StorageReference filepath = FirebaseStorage.getInstance().getReference("prescription").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            StorageTask uploadTask = filepath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                            throw  task.getException();

                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    Uri  downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    Map<String , Object> data = new HashMap<>();
                    data.put("prescription",imageUrl);
                    data.put("status", "ordered");
                    data.put("user_id",currentUser.getUid().toString());
                    data.put("Address", editText.getText().toString());

                    data.put("Latitude",(double)0);
                    data.put("Longitude",(double)0);
                    data.put("Estimated Time",String.valueOf(kilometers+" "+time));

                    FirebaseFirestore.getInstance().collection("prescription").document(currentUser.getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PostActivity.this, "Image is uploaded", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });



                    pd.dismiss();
                    if (!TextUtils.isEmpty(editText.getText())){
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(AddressActivity.token, "Notification", "Prescription Placed", getApplicationContext(), PostActivity.this);
                        notificationsSender.SendNotifications();
                        Intent ii = new Intent(PostActivity.this, DekiveryActivity.class);
                        ii.putExtra("order_id","Prescription");
                        ii.putExtra("time",time);
                        ii.putExtra("address","manual");
                        startActivity(ii);
                    }else {
                        Intent i = new Intent(PostActivity.this, AddressActivity.class);
                        i.putExtra("intent", "Prescription");
                        i.putExtra("token", token);
                        i.putExtra("token1", "null");
                        startActivity(i);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
    private  String getFileExtension(Uri uri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image_uploded.setImageURI(imageUri);
        }else {

            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }
}
