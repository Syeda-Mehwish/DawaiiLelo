package com.example.dawaiilello.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.R;

import java.util.ArrayList;
import java.util.List;

public class GridProductAdapter extends BaseAdapter {
    List<horizontalProductModel> horizontalProductModelList;
    public GridProductAdapter(List<horizontalProductModel>horizontalProductModelList){
        this.horizontalProductModelList= horizontalProductModelList;
    }
    @Override
    public int getCount() {

        return horizontalProductModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View view,final ViewGroup viewGroup) {
        View view1;
        if (view == null){
            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
            view1.setElevation(0);
            view1.setBackgroundColor(Color.parseColor("#ffffff"));




            ImageView productImage = view1.findViewById(R.id.categories_image);
            TextView productTitle = view1.findViewById(R.id.categories_title);
            TextView productDescription = view1.findViewById(R.id.hs_product_description);
            TextView productPrice = view1.findViewById(R.id.hs_product_price);

            Glide.with(viewGroup.getContext()).load(horizontalProductModelList.get(i).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(productImage);
            productTitle.setText(horizontalProductModelList.get(i).getProductTitle());
            productDescription.setText(horizontalProductModelList.get(i).getProductDescription());
            productPrice.setText(horizontalProductModelList.get(i).getProductPrice());
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBqueries.alternativeProductModelList=new ArrayList<>();
                    Intent productDetailsIntent = new Intent(view1.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("Product_ID",horizontalProductModelList.get(i).getProductID());

                    view1.getContext().startActivity(productDetailsIntent);
                }
            });


        }else{
            view1 = view;

        }
        return  view1;
    }
}
