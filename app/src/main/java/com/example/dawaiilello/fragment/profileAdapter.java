package com.example.dawaiilello.fragment;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.R;

import java.util.ArrayList;
import java.util.List;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ViewHolder> {
    Context context;

    List<profileModel> alternativeProductModelList;

    public profileAdapter(Context context,List<profileModel> alternativeProductModelList) {
        this.context = context;
        this.alternativeProductModelList = alternativeProductModelList;
    }

    public List<profileModel> getAlternativeProductModelList() {
        return alternativeProductModelList;
    }

    public void setAlternativeProductModelList(List<profileModel> alternativeProductModelList) {
        this.alternativeProductModelList = alternativeProductModelList;
    }

    @NonNull
    @Override
    public profileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent, false );
        return new profileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profileAdapter.ViewHolder holder, int position) {
        String productID = alternativeProductModelList.get(position).getProduct_id();
        String  resource = alternativeProductModelList.get(position).getImage();
        String title = alternativeProductModelList.get(position).getTitle();
        String description = alternativeProductModelList.get(position).getFormula();
        String price = alternativeProductModelList.get(position).getPrice();
        holder.setProductID(productID);
        holder.setProduceImage(resource);
        holder.setProduct_tile(title);
        holder.setProductDescription(description);
        holder.setProductPrice(price);


    }

    @Override
    public int getItemCount() {
        return alternativeProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView produceImage;
        TextView product_tile;
        TextView productDescription;
        TextView productPrice;


        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            produceImage = itemView.findViewById(R.id.productCat_image);
            product_tile = itemView.findViewById(R.id.productCat_title);
            productDescription= itemView.findViewById(R.id.productCat_description);
            productPrice = itemView.findViewById(R.id.productCat_price);



        }

        private  void setProduceImage(String resource){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(produceImage);
        }
        private  void setProduct_tile(String title){
            product_tile.setText(title);




        }
        private  void setProductDescription(String description){
            productDescription.setText(description);
        }
        private  void setProductPrice(String price){
            productPrice.setText("Rs: "+price+"/-");
        }



        public void setProductID(String productID) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBqueries.alternativeProductModelList=new ArrayList<>();
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("Product_ID",productID);
                    itemView.getContext().startActivity(productDetailsIntent);

                }
            });
        }
    }
}


