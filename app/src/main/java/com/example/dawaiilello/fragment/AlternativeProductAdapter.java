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

import java.util.ArrayList;
import java.util.List;

public class AlternativeProductAdapter extends RecyclerView.Adapter<AlternativeProductAdapter.ViewHolder> {
    private List<AlternativeProductModel> alternativeProductModelList;

    public AlternativeProductAdapter(List<AlternativeProductModel> alternativeProductModelList) {
        this.alternativeProductModelList = alternativeProductModelList;
    }


    @NonNull
    @Override
    public AlternativeProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent, false );
        return new AlternativeProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlternativeProductAdapter.ViewHolder holder, int position) {
        String productID = alternativeProductModelList.get(position).getProductID();
        String  resource = alternativeProductModelList.get(position).getProductImage();
        String title = alternativeProductModelList.get(position).getProductTitle();
        String description = alternativeProductModelList.get(position).getProductDescription();
        String price = alternativeProductModelList.get(position).getProductPrice();
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
        private ImageView produceImage;
        private TextView product_tile;
        private TextView productDescription;
        private TextView productPrice;
        private String productID;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            produceImage = itemView.findViewById(R.id.categories_image);
            product_tile = itemView.findViewById(R.id.categories_title);
            productDescription= itemView.findViewById(R.id.hs_product_description);
            productPrice = itemView.findViewById(R.id.hs_product_price);
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
            this.productID = productID;
        }
    }
}

