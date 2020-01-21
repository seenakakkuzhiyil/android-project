package com.example.marketplace;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    List<String> adapterList = new ArrayList<String>();
    List<Integer> adapterPics = new ArrayList<Integer>();

    public CategoryAdapter(List<String> anyList, List<Integer> anyPics)
    {
        adapterList = anyList;
        adapterPics = anyPics;
    }


    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public Object getItem(int i) {
        return adapterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.categorylinearlayout,null);

        }

        ImageView categoryImage = convertView.findViewById(R.id.imageViewCategoryImage);
        TextView categoryText = convertView.findViewById(R.id.textViewCategoryItem);

        categoryImage.setImageResource(adapterPics.get(position));
        categoryText.setText(adapterList.get(position));

        return convertView;
    }
}
