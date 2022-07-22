package com.example.dawaiilello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dawaiilello.fragment.AddressActivity;

public class DekiveryActivity extends AppCompatActivity {
    private ImageButton closeBtn;
    String id;
    String time;
    String address;
    private TextView estimatedTime;
    private TextView orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dekivery);
        closeBtn = findViewById(R.id.deliveryclosebutton);
        estimatedTime=findViewById(R.id.textViewestimated);
        orderId= findViewById(R.id.orderid);
        id=getIntent().getStringExtra("order_id");
        time=getIntent().getStringExtra("time");
        address=getIntent().getStringExtra("address");
        orderId.setText("Order ID "+id);
        estimatedTime.setText("Expected delivery within "+time);
        if (address.equals("live")) {
            if (!AddressActivity.token1.equals("null")) {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(AddressActivity.token, "Order Placed", id, getApplicationContext(), DekiveryActivity.this);
                notificationsSender.SendNotifications();
                FcmNotificationsSender notificationsSenderr = new FcmNotificationsSender(AddressActivity.token1, "Order Placed", id, getApplicationContext(), DekiveryActivity.this);
                notificationsSenderr.SendNotifications();
            } else {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(AddressActivity.token, "Notification", "Prescription Placed", getApplicationContext(), DekiveryActivity.this);
                notificationsSender.SendNotifications();
            }
        }
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(DekiveryActivity.this,MainActivity.class);
                DekiveryActivity.this.finish();

                startActivity(mainIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent main = new Intent(DekiveryActivity.this,MainActivity.class);

        startActivity(main);
    }

}

