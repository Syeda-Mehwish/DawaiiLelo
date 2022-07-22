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

public class ViewALlcaAdapter extends RecyclerView.Adapter<ViewALlcaAdapter.ViewHolder> {
    private List<ViewALlcaModel> viewModelList ;

    public  ViewALlcaAdapter(List<ViewALlcaModel> viewModelList){
        this.viewModelList = viewModelList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i){
        String resource = viewModelList.get(i).getProductImage();
        String title = viewModelList.get(i).getProductTitle();
        viewHolder.setProduceImage(resource);
        viewHolder.setProduct_tile(title);

    }
    @Override
    public int getItemCount(){
        return viewModelList.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_immage);
            productTitle = itemView.findViewById(R.id.categoryTitle);

        }
        private  void setProduceImage(String resource){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(productImage);
        }
        private  void setProduct_tile(String title){
            productTitle.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                    productDetailsIntent.putExtra("Category_name",title);

                    itemView.getContext().startActivity(productDetailsIntent);

                }
            });
        }
    }
}
