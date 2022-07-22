package com.example.dawaiilello.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.R;

import java.util.List;

public class orderDetailsAdapter extends   RecyclerView.Adapter<orderDetailsAdapter.ViewHolder>{
        Context context;

        List<orderDetailsModel> orderDetailsModellList;

public orderDetailsAdapter(Context context,List<orderDetailsModel> orderDetailsModelList) {
        this.context = context;
        this.orderDetailsModellList = orderDetailsModelList;
        }

public List<orderDetailsModel> getOrderDetailsModellList() {
        return orderDetailsModellList;
        }

public void setOrderDetailsModellList(List<orderDetailsModel> orderDetailsModellList) {
        this.orderDetailsModellList = orderDetailsModellList;
        }

@NonNull
@Override
public orderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent, false );
        return new orderDetailsAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull orderDetailsAdapter.ViewHolder holder, int position) {
        String productID = orderDetailsModellList.get(position).getProductID();
        String  resource = orderDetailsModellList.get(position).getProductImage();
        String title = orderDetailsModellList.get(position).getProductTitle();
        Long quantity = orderDetailsModellList.get(position).getProductQuantity();
        String Price = orderDetailsModellList.get(position).getProductPrice();

        holder.setProductID(productID);
        holder.setProduceImage(resource);
        holder.setProduct_tile(title);
        holder.setProductDescription(quantity);
        holder.setProductPrice(Price);


        }

@Override
public int getItemCount() {
        return orderDetailsModellList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView produceImage;
    TextView product_tile;
    TextView productQuantity;
    TextView product_Price;
    LinearLayout delete;


    public ViewHolder(@NonNull View itemView ) {
        super(itemView);
        produceImage = itemView.findViewById(R.id.product_image);
        product_tile = itemView.findViewById(R.id.product_title);
        productQuantity= itemView.findViewById(R.id.product_quantity);
        product_Price = itemView.findViewById(R.id.product_price);
        delete= itemView.findViewById(R.id.remove_item_btn);
        delete.setVisibility(View.GONE);



    }

    private  void setProduceImage(String resource){
        Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(produceImage);
    }
    private  void setProduct_tile(String title){
        product_tile.setText(title);




    }
    private  void setProductDescription(Long description){
        productQuantity.setText(String.valueOf(description));
    }




    public void setProductID(String productID) {


    }
    private void  setProductPrice(String productPrice){
        product_Price.setText("Rs: "+productPrice+"/-");
    }
}
}
