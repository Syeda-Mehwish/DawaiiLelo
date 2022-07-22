package com.example.dawaiilello.fragment;

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

import java.util.List;

public class horizontalProductScrollAdapter extends RecyclerView.Adapter<horizontalProductScrollAdapter.ViewHolder> {
    private List<horizontalProductModel> horizontalProductModellist;

    public horizontalProductScrollAdapter(List<horizontalProductModel> horizontalProductModellist) {
        this.horizontalProductModellist = horizontalProductModellist;
    }


    @NonNull
    @Override
    public horizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull horizontalProductScrollAdapter.ViewHolder holder, int position) {
        String resource = horizontalProductModellist.get(position).getProductImage();
        String title = horizontalProductModellist.get(position).getProductTitle();
        String description = horizontalProductModellist.get(position).getProductDescription();
        String price = horizontalProductModellist.get(position).getProductPrice();
        holder.setProduceImage(resource);

        holder.setProduct_tile(title);
        holder.setProductDescription(description);
        holder.setProductPrice(price);

    }

    @Override
    public int getItemCount() {
        if (horizontalProductModellist.size()>8){
            return  8;

        }else {
            return horizontalProductModellist.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView produceImage;
        private TextView product_tile;
        private TextView productDescription;
        private TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            produceImage = itemView.findViewById(R.id.categories_image);
            product_tile = itemView.findViewById(R.id.categories_title);
            productDescription= itemView.findViewById(R.id.hs_product_description);
            productPrice = itemView.findViewById(R.id.hs_product_price);

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
            productPrice.setText("Rs: "+price);
        }
    }
}
