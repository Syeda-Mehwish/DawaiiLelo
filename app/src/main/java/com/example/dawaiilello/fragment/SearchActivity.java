package com.example.dawaiilello.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.example.dawaiilello.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private TextView  textView;
    private RecyclerView recyclerView;
    final List<profileModel> list =  new ArrayList<>();
    final List<String> ids =new ArrayList<>();
    Adapter adapter = new Adapter(this,list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        textView = findViewById(R.id.textViewSearch);
        recyclerView=findViewById(R.id.searchRecyclerview);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                list.clear();
                ids.clear();
                final String[] tags = query.toLowerCase().split(" ");
                for (String tag : tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("Products").whereArrayContains("tags",tag).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                    profileModel profileModel = documentSnapshot.toObject(com.example.dawaiilello.fragment.profileModel.class);

                                    if (!ids.contains(profileModel.getProduct_id())){
                                        list.add(profileModel);
                                        ids.add(profileModel.getProduct_id());
                                    }
                                }
                                if (tag.equals(tags[tags.length-1])){
                                    if (list.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else{
                                        recyclerView.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.GONE);
                                        adapter.getFilter().filter(query);
                                    }

                                }

                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(SearchActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.clear();
                ids.clear();
                String[] tags = newText.toLowerCase().split(" ");
                for (String tag : tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("Products").whereArrayContains("tags",tag).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                    profileModel profileModel = documentSnapshot.toObject(com.example.dawaiilello.fragment.profileModel.class);
                                    profileModel.setTags((ArrayList<String>) documentSnapshot.get("tags"));
                                    if (!ids.contains(profileModel.getProduct_id())){
                                        list.add(profileModel);
                                        ids.add(profileModel.getProduct_id());
                                    }
                                }
                                if (tag.equals(tags[tags.length-1])){
                                    if (list.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else{
                                        recyclerView.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.GONE);
                                        adapter.getFilter().filter(newText);
                                    }

                                }

                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(SearchActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                return false;
            }
        });
    }
    class Adapter extends profileAdapter implements Filterable{

        private List<profileModel> originalList;

        public Adapter(Context context, List<profileModel> alternativeProductModelList) {
            super(context, alternativeProductModelList);
            originalList=alternativeProductModelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();
                    List<profileModel> filteredList = new ArrayList<>();
                    final String[] tags = charSequence.toString().toLowerCase().split(" ");
                    for (profileModel model: originalList){
                        ArrayList<String> presentTags = new ArrayList<>();
                        for(String tag: tags){
                            if (model.getTags().contains(tag)){
                                presentTags.add(tag);
                            }
                        } model.setTags(presentTags);
                    }
                    for (int i = tags.length;i>0;i--){
                        for (profileModel model: originalList){
                            if (model.getTags().size()==i){
                                filteredList.add(model);
                            }

                        }

                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if (filterResults.count>0){
                        setAlternativeProductModelList((List<profileModel>) filterResults.values);
                    }
                    notifyDataSetChanged();

                }
            };
        }
    }

}