package com.example.dawaiilello.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.Activity_save_money;
import com.example.dawaiilello.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {
    private List<CartItemModel> cartItemModelList;
    private TextView cartTotalAmount;


    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
    }


    @Override
    public int getItemViewType(int position) {
       switch (cartItemModelList.get(position).getType())
       {
           case 0:
               return CartItemModel.CART_ITEM;

           case 1:
               return CartItemModel.TOTAL_AMOUNT;

           default:
               return  -1;

       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new cartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new cartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource= cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                boolean inStock = cartItemModelList.get(position).isInStock();
                long productQuantity = cartItemModelList.get(position).getProductQuantity();
                long maxQuantity = cartItemModelList.get(position).getMaxQuantity();

                ((cartItemViewholder)viewHolder).setItemDetails(productID,resource ,title,productPrice,position, inStock,String.valueOf(productQuantity),maxQuantity);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemsPrice = 0;
                String deliveryPrice;
                int totalAmount;
                for (int x =0; x< cartItemModelList.size();x++){
                    if (cartItemModelList.get(x).getType()==CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()){
                        totalItems++;
                        totalItemsPrice = totalItemsPrice + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())*(int) (cartItemModelList.get(x).getProductQuantity()));
                    }
                }
                if (totalItemsPrice >500){
                    deliveryPrice= "Free";
                    totalAmount = totalItemsPrice;
                }else{
                    deliveryPrice ="60";
                    totalAmount = totalItemsPrice+60;
                }



                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemsPrice(totalItemsPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                ((cartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems, totalItemsPrice, deliveryPrice, totalAmount);
                break;
            default:return;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }
    class cartItemViewholder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;
        private LinearLayout deleteBtn;

        public cartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity =  itemView.findViewById(R.id.product_quantity);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
        }

        private void setItemDetails(String productID, String resource, String title, String productPriceText, int position, boolean inStock,String Quantity,Long maxQuantity){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(productImage);
            productTitle.setText(title);
            if (inStock) {
                productPrice.setText("Rs."+productPriceText+"/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                productQuantity.setText("Qty:"+Quantity);

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog quantityDialog= new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        quantityNo.setHint("Max "+String.valueOf(maxQuantity));
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                quantityDialog.dismiss();
                            }
                        });
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0 ) {
                                        DBqueries.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        DBqueries.saveMoneyItemModelList.get(position).setsmProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        productQuantity.setText("Qty:" + quantityNo.getText());
                                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                                        ProductDetailsActivity.running_cartlist_query=false;


                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantity : " + maxQuantity, Toast.LENGTH_SHORT).show();

                                    }
                                }
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });

            }else{
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
               productQuantity.setVisibility(View.INVISIBLE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("Are you sure?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!ProductDetailsActivity.running_cartlist_query){
                                    ProductDetailsActivity.running_cartlist_query = true;
                                    DBqueries.removeFromAlternateCart(position,itemView.getContext(),cartTotalAmount);
                                    DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);

                                    ProductDetailsActivity.running_cartlist_query = false;

                                }



                                dialogInterface.cancel();

                            }

                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

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
        }
    }
    class cartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView totalAmount;
        private TextView deliveryPrice;



        public cartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems= itemView.findViewById(R.id.total_items) ;
            totalItemPrice= itemView.findViewById(R.id.total_items_price) ;
            totalAmount= itemView.findViewById(R.id.total_price) ;
            deliveryPrice= itemView.findViewById(R.id.delivery_price) ;
        }
        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText){
            totalItems.setText("Price("+totalItemText+" items)");
            totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")){
                deliveryPrice.setText(deliveryPriceText);
            }else{
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");

            }
            totalAmount.setText("Rs"+totalAmountText+"/-");
            cartTotalAmount.setText("Rs"+totalAmountText+"/-");
            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

}
