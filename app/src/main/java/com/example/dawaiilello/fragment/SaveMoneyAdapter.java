package com.example.dawaiilello.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.R;

import java.util.List;

public class SaveMoneyAdapter extends RecyclerView.Adapter{
    private List<SaveMoneyItemModel> SaveMoneyItemModelList;
    private TextView cartTotalAmount;

    public SaveMoneyAdapter(List<SaveMoneyItemModel> SaveMoneyItemModelList, TextView cartTotalAmount) {
        this.SaveMoneyItemModelList = SaveMoneyItemModelList;
        this.cartTotalAmount = cartTotalAmount;
    }


    @Override
    public int getItemViewType(int position) {
        switch (SaveMoneyItemModelList.get(position).getType())
        {
            case 0:
                return SaveMoneyItemModel.SaveMoney_ITEM;

            case 1:
                return SaveMoneyItemModel.SaveMoney_TOTAL_AMOUNT;

            default:
                return  -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case SaveMoneyItemModel.SaveMoney_ITEM:
                View SaveMoneyItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.save_money_item_layout, viewGroup, false);
                return new SaveMoneyAdapter.SaveMoneyItemViewholder(SaveMoneyItemView);
            case SaveMoneyItemModel.SaveMoney_TOTAL_AMOUNT:
                View SaveMoneyTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.save_total_amount_layout, viewGroup, false);
                return new SaveMoneyAdapter.SaveMoneyTotalAmountViewholder(SaveMoneyTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (SaveMoneyItemModelList.get(position).getType()){
            case SaveMoneyItemModel.SaveMoney_ITEM:
                String productID = SaveMoneyItemModelList.get(position).getProductID();
                String resource= SaveMoneyItemModelList.get(position).getsmProductImage();
                String title = SaveMoneyItemModelList.get(position).getsmProductTitle();
                String productPrice = SaveMoneyItemModelList.get(position).getsmProductPrice();
                boolean inStock = SaveMoneyItemModelList.get(position).isInStock();
                long productQuantity = SaveMoneyItemModelList.get(position).getsmProductQuantity();
                long maxQuantity = SaveMoneyItemModelList.get(position).getMaxQuantity();

                ((SaveMoneyAdapter.SaveMoneyItemViewholder)viewHolder).setItemDetails(productID,resource ,title,productPrice,position, inStock,String.valueOf(productQuantity),maxQuantity);
                break;
            case SaveMoneyItemModel.SaveMoney_TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemsPrice = 0;
                String deliveryPrice;
                int totalAmount;
                for (int x =0; x< SaveMoneyItemModelList.size();x++){
                    if (SaveMoneyItemModelList.get(x).getType()==CartItemModel.CART_ITEM && SaveMoneyItemModelList.get(x).isInStock()){
                        totalItems++;
                        totalItemsPrice = totalItemsPrice + (Integer.parseInt(SaveMoneyItemModelList.get(x).getsmProductPrice())*(int) (SaveMoneyItemModelList.get(x).getsmProductQuantity()));
                    }
                }
                if (totalItemsPrice >500){
                    deliveryPrice= "Free";
                    totalAmount = totalItemsPrice;
                }else{
                    deliveryPrice ="60";
                    totalAmount = totalItemsPrice+60;
                }

                 SaveMoneyItemModelList.get(position).setTotalItems(totalItems);
                SaveMoneyItemModelList.get(position).setTotalItemsPrice(totalItemsPrice);
                SaveMoneyItemModelList.get(position).setTotalAmount(totalAmount);
                SaveMoneyItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                ((SaveMoneyAdapter.SaveMoneyTotalAmountViewholder)viewHolder).setTotalAmount(totalItems, totalItemsPrice, deliveryPrice, totalAmount);
                break;
            default:return;
        }

    }

    @Override
    public int getItemCount() {
        return SaveMoneyItemModelList.size();
    }
    class SaveMoneyItemViewholder extends RecyclerView.ViewHolder{
        private ImageView sm_productImage;
        private TextView sm_productTitle;
        private TextView sm_productPrice;
        private TextView sm_productQuantity;

        public SaveMoneyItemViewholder(@NonNull View itemView) {
            super(itemView);
            sm_productImage = itemView.findViewById(R.id.sm_product_image);
            sm_productTitle = itemView.findViewById(R.id.sm_product_title);
            sm_productPrice = itemView.findViewById(R.id.sm_product_price);
            sm_productQuantity =  itemView.findViewById(R.id.sm_product_quantity);
        }
        private void setItemDetails(String productID, String resource, String title, String productPriceText, int position, boolean inStock,String Quantity,Long maxQuantity){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(sm_productImage);
            sm_productTitle.setText(title);
            sm_productPrice.setText("Rs"+productPriceText+"/-");
            sm_productQuantity.setText("Qty:"+Quantity);
        }
    }
    class SaveMoneyTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView sm_totalItems;
        private TextView sm_totalItemPrice;
        private TextView sm_totalAmount;
        private TextView sm_deliveryPrice;



        public SaveMoneyTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            sm_totalItems= itemView.findViewById(R.id.sm_total_items) ;
            sm_totalItemPrice= itemView.findViewById(R.id.sm_total_items_price) ;
            sm_totalAmount= itemView.findViewById(R.id.sm_total_price) ;
            sm_deliveryPrice= itemView.findViewById(R.id.sm_delivery_price) ;

        }
        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText){
            sm_totalItems.setText("Price("+totalItemText+" items)");
            sm_totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")){
                sm_deliveryPrice.setText(deliveryPriceText);
            }else{
                sm_deliveryPrice.setText("Rs."+deliveryPriceText+"/-");

            }
            sm_totalAmount.setText("Rs"+totalAmountText+"/-");
            cartTotalAmount.setText("Rs"+totalAmountText+"/-");
            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);
            }

            //savedAmount.setText(savedAmountText);
        }
    }


}
