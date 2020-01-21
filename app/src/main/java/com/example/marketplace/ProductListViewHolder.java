package com.example.marketplace;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


class ProductListViewHolder extends RecyclerView.ViewHolder implements View

        .OnClickListener
{
    public ItemClickListener listener;

    TextView productNameTextView;
    ImageView productImageView;
    TextView productDescTextView;
    TextView productPriceTextView;
    Button deleteButton;
    Button updateButton;



    public ProductListViewHolder(View itemView ){

        super(itemView);
        productNameTextView = (TextView) itemView.findViewById(R.id.textViewProductRecyclerName);
        productImageView = (ImageView) itemView.findViewById(R.id.imageViewProductRecyclerImage);
        productDescTextView = (TextView) itemView.findViewById(R.id.textViewProductRecyclerDescription);
        productPriceTextView = (TextView) itemView.findViewById(R.id.textViewProductRecyclerPrice);
        deleteButton = (Button) itemView.findViewById(R.id.buttonDeleteProduct);
        updateButton =(Button) itemView.findViewById(R.id.buttonUpdateProduct);


        itemView.setOnClickListener(this);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }
    public void onClick(View view)
    {

      //  listener.onClick(view, getAdapterPosition(), false);
    }



}
