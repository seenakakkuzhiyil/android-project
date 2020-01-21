package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    List<String> categoryList = new ArrayList<String>();
    List<Integer> categoryPics = new ArrayList<Integer>();

    private void addItems()
    {
        categoryList.add("Appliances");
        categoryList.add("Automobiles");
        categoryList.add("Electronics");
        categoryList.add("Beauty");

        categoryPics.add(R.drawable.appliances);
        categoryPics.add(R.drawable.automobiles);
        categoryPics.add(R.drawable.electronics);
        categoryPics.add(R.drawable.beauty);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        addItems();
        GridView gridViewCategoryItems = (GridView)findViewById(R.id.gridViewCategory);
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList,categoryPics);

        gridViewCategoryItems.setAdapter(categoryAdapter);

        gridViewCategoryItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

              Intent intent = new Intent(CategoryActivity.this, BuyerHomePageActivity.class);
              intent.putExtra("category",categoryList.get(position));
              startActivity(intent);

            }
        });

    }
}
