package com.example.dawaiilello.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawaiilello.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class homePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public homePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }



    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()){
            case 0:
                return  HomePageModel.BannerSlider;
            case 1:
                return HomePageModel.StripAdBanner;
            case 2:
                return HomePageModel.HorizontalProductView ;
            case 3:
                return HomePageModel.GridProductView;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case HomePageModel.BannerSlider:
                View bannerSliderview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_banner, parent,false);
                return new BannerSliderViewHolder(bannerSliderview);
            case HomePageModel.StripAdBanner:
                View StripAdview = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent,false);
                return new StripAdBannerViewHolder(StripAdview);
            case HomePageModel.HorizontalProductView:
                View horizontalProductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_home_layout, parent,false);
                return new HorizontalProductViewHolder(horizontalProductview);
            case HomePageModel.GridProductView:
                View GridlProductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent,false);
                return new GridProductViewHolader(GridlProductview);
            default:
                return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()){
            case HomePageModel.BannerSlider:
                List<sliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder)holder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomePageModel.StripAdBanner:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgrounColor();
                ((StripAdBannerViewHolder)holder).setStripAd(resource,color);
                break;
            case HomePageModel.HorizontalProductView:
                String HorizontalLayouttitle = homePageModelList.get(position).getTitle();
                List<ViewALlcaModel> viewALlcaModelList= homePageModelList.get(position).getViewAlllist();
                List<HomeCategory>homeCategoryList = homePageModelList.get(position).getHomeCategoryList();
                ((HorizontalProductViewHolder)holder).setHorizontalProductLayout(homeCategoryList,HorizontalLayouttitle,viewALlcaModelList);
                break;
            case HomePageModel.GridProductView:
                String gridLayoutColor = homePageModelList.get(position).getBackgrounColor();
                String gridLayouttitle = homePageModelList.get(position).getTitle();
                List<horizontalProductModel> gridProductModellist = homePageModelList.get(position).getHorizontalProductModelList();
                ((GridProductViewHolader)holder).setGridProductLayout(gridProductModellist,gridLayouttitle,gridLayoutColor);
                break;


            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public  class BannerSliderViewHolder extends  RecyclerView.ViewHolder{
        private ViewPager bannerSlider;
        private  int currentPage;
        private Timer timer;
        final  private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<sliderModel> arranagedList;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerSlider = itemView.findViewById(R.id.viewPager);


        }


        private  void setBannerSliderViewPager(List<sliderModel>sliderModelList){
            currentPage=2;
            if (timer!=null){
                timer.cancel();
            }
            arranagedList = new ArrayList<>();
            for (int x=0; x<sliderModelList.size();x++){
                arranagedList.add(x, sliderModelList.get(x));
            }
            arranagedList.add(0, sliderModelList.get(sliderModelList.size()-2));
            arranagedList.add(1, sliderModelList.get(sliderModelList.size()-1));
            arranagedList.add(sliderModelList.get(0));
            arranagedList.add(sliderModelList.get(1));
            sliderAdapter sliderAdapter = new sliderAdapter(arranagedList);
            bannerSlider.setAdapter(sliderAdapter);
            bannerSlider.setClipToPadding(false);
            bannerSlider.setPageMargin(20);
            bannerSlider.setCurrentItem(currentPage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if ( state == ViewPager.SCROLL_STATE_IDLE){
                        pageLooper(arranagedList);
                    }

                }
            };
            bannerSlider.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideshow(arranagedList);
            bannerSlider.setOnTouchListener((view, motionEvent) -> {
                pageLooper(arranagedList);
                stopBannerSlideshow();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    startBannerSlideshow(arranagedList);
                }
                return false;
            });

        }
        private void  pageLooper(List<sliderModel>sliderModelList){
            if (currentPage== sliderModelList.size()-2){
                currentPage = 2;
                bannerSlider.setCurrentItem(currentPage,false);
            }
            if (currentPage== 1){
                currentPage = sliderModelList.size()-3;
                bannerSlider.setCurrentItem(currentPage,false);
            }

        }
        private  void  startBannerSlideshow(List<sliderModel>sliderModelList){
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()){
                        currentPage = 1;
                    }
                    bannerSlider.setCurrentItem(currentPage++);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME );
        }
        private  void stopBannerSlideshow(){
            timer.cancel();
        }
    }
    public  class  StripAdBannerViewHolder extends  RecyclerView.ViewHolder{
        private ImageView StripImage;
        private ConstraintLayout stripAdContainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            StripImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }
        private void setStripAd(String resource, String color){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(StripImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }
    public  class HorizontalProductViewHolder extends RecyclerView.ViewHolder{
        // private ConstraintLayout container;
        private TextView horizontalLayoutTitle;
        private Button HorizontalViewAllBtn;
        private RecyclerView horizontalRecyclerview;
        private LinearLayout linearLayout;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout4);
            horizontalLayoutTitle = itemView.findViewById(R.id.category_home_title);
            HorizontalViewAllBtn = itemView.findViewById(R.id.category_home_layout_button);
            horizontalRecyclerview = itemView.findViewById(R.id.category_home_recyclerview);
            horizontalRecyclerview.setRecycledViewPool(recycledViewPool);

        }
        private void  setHorizontalProductLayout(List<HomeCategory> homeCategoryList, String title,List<ViewALlcaModel>viewALlcaModelList){
            horizontalLayoutTitle.setText("Category");
            HorizontalViewAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ViewAllCategoryActivity.viewALlcaModelList = viewALlcaModelList;
                    Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllCategoryActivity.class);
                    viewAllIntent.putExtra("layout_code",0);
                    itemView.getContext().startActivity(viewAllIntent);

                }
            });
            HomeCategoryAdapter adapter = new HomeCategoryAdapter(homeCategoryList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerview.setLayoutManager(linearLayoutManager);
            horizontalRecyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }
    public class GridProductViewHolader extends RecyclerView.ViewHolder{
        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        //private GridView gridView;
        private GridLayout gridLayout;


        public GridProductViewHolader(@NonNull View itemView) {
            super(itemView);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_Layout_title);
            gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewAllBtn);
            container = itemView.findViewById(R.id.container);
            gridLayout = itemView.findViewById(R.id.gridLayout);

        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setGridProductLayout(List<horizontalProductModel> horizontalProductModellist, String title, String color){
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);
            for (int x=0; x<4 ; x++){
                ImageView productimage = gridLayout.getChildAt(x).findViewById(R.id.categories_image);
                TextView producttitle = gridLayout.getChildAt(x).findViewById(R.id.categories_title);
                TextView productdescription = gridLayout.getChildAt(x).findViewById(R.id.hs_product_description);
                TextView productprice =gridLayout.getChildAt(x).findViewById(R.id.hs_product_price);


                Glide.with(itemView.getContext()).load(horizontalProductModellist.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_camera)).into(productimage);
                producttitle.setText(horizontalProductModellist.get(x).getProductTitle());
                productdescription.setText(horizontalProductModellist.get(x).getProductDescription());
                productprice.setText(horizontalProductModellist.get(x).getProductPrice());
                int finalX = x;
                gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DBqueries.alternativeProductModelList = new ArrayList<>();

                        Intent productDetailsIntent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                        productDetailsIntent.putExtra("Product_ID",horizontalProductModellist.get(finalX).getProductID());

                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }


            gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewAllCategoryActivity.horizontalProductModelList = horizontalProductModellist;
                    Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllCategoryActivity.class);
                    viewAllIntent.putExtra("layout_code",1);

                    itemView.getContext().startActivity(viewAllIntent);

                }
            });

        }
    }
}



























