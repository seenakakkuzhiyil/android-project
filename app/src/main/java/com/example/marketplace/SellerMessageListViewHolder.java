package com.example.marketplace;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SellerMessageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public ItemClickListener listener;

    public TextView listSellerMessage;
    public ImageView listSellerImage;
    public Button buttonApprove;
    public Button buttonReject;
    public TextView textViewApproved;


    public SellerMessageListViewHolder(View itemView)
    {
        super(itemView);

        listSellerMessage= itemView.findViewById(R.id.textViewSellerMessage);
        listSellerImage = itemView.findViewById(R.id.imageViewMessagePicture);
        buttonApprove = itemView.findViewById(R.id.buttonApprove);
        buttonReject = itemView.findViewById(R.id.buttonReject);
        textViewApproved = itemView.findViewById(R.id.textViewApproved);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {

    }




}
