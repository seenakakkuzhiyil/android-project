package com.example.marketplace;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BuyerMessageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public ItemClickListener listener;

    public TextView listBuyerMessage;
    public ImageView listBuyerImage;
    public Button buttonCancel;
    public TextView textStatus;

    public BuyerMessageListViewHolder(View itemView)
    {
        super(itemView);
        listBuyerImage = itemView.findViewById(R.id.imageViewBuyerMessagePicture);
        listBuyerMessage = itemView.findViewById(R.id.textViewBuyerMessage);
        buttonCancel = itemView.findViewById(R.id.buttonCancelRequest);
        textStatus = itemView.findViewById(R.id.textViewBuyerStatus);


    }

    @Override
    public void onClick(View view) {

    }
}
