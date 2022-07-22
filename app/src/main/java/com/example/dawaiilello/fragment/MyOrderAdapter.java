package com.example.dawaiilello.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.R;

import java.util.Date;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {
    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }


    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder viewholder, int position) {
        String resource = myOrderItemModelList.get(position).getProductImage();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String orderStatus = myOrderItemModelList.get(position).getDeliveryStatus();
        Date date;
        switch (orderStatus){
            case "Ordered":
                date=myOrderItemModelList.get(position).getOrderedDate();
                break;
            case "confirmed":
                date=myOrderItemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date=myOrderItemModelList.get(position).getShippedDate();
                break;
            case "paid":
                date=myOrderItemModelList.get(position).getDeliveredDate();
                break;
            default:
                date=myOrderItemModelList.get(position).getOrderedDate();

        }
        viewholder.setData(resource, title,orderStatus,date);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }
    class Viewholder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            orderIndicator=  itemView.findViewById(R.id.order_indicator);
            productTitle = itemView.findViewById(R.id.product_title);
            deliveryStatus= itemView.findViewById(R.id.order_delivered_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),orderDetailsActivity.class);
                    intent.putExtra("order_id",productTitle.getText().toString());
                    intent.putExtra("price","250");
                    view.getContext().startActivity(intent);
                }
            });

        }

        private void setData(String resource, String title,String orderStatus,Date deliveredDate){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.order)).into(productImage);
            productTitle.setText(title);
            if (orderStatus.equals("cancelled")) {


            }else {
                //orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark)));
            }
            deliveryStatus.setText(orderStatus+String.valueOf(deliveredDate));

        }
    }
}
