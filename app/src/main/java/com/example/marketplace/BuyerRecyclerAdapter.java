package com.example.marketplace;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplace.BuyerFragments.BuyerMessagesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyerRecyclerAdapter extends RecyclerView.Adapter<BuyerRecyclerAdapter.ViewHolder> {

    DatabaseReference MessageDatabaseRef;

    List<Product> adapterList = new ArrayList<Product>();
    Context context;

    //for filter
    public BuyerRecyclerAdapter(List<Product> anyList, Context anyContext)
    {
        adapterList = anyList;
        context = anyContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_list_recycler,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Product product = adapterList.get(position);

        final String itemId = adapterList.get(position).getItemId();

        holder.productNameTextView.setText(product.getItemName());
        holder.productDescTextView.setText(product.getItemDescription());
        holder.productPriceTextView.setText("$" + product.getItemPrice());
        Picasso.get().load(product.getItemPhoto()).into(holder.productImageView);

        MessageDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        final String buyerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String sellerId = adapterList.get(position).getUserId();
        final String messages = "Waiting";
        final String messageId = itemId + buyerId;

        MessageDatabaseRef.orderByKey().equalTo(messageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot each : dataSnapshot.getChildren())
                {
                    if(dataSnapshot.exists())
                    {
                        holder.productBuyButton.setText("Request Sent");
                        holder.productBuyButton.setTextSize(10);
                        holder.productBuyButton.setClickable(false);
                        holder.productBuyButton.setBackgroundColor(Color.GRAY);
                        holder.productBuyButton.setTextColor(Color.BLACK);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.productBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                
                final HashMap<String, Object> messageMap = new HashMap<>();
                messageMap.put("buyerId", buyerId);
                messageMap.put("sellerId", sellerId);
                messageMap.put("messages", messages);
                messageMap.put("messageId", messageId);
                messageMap.put("itemId",itemId);


                MessageDatabaseRef.child(messageId).updateChildren(messageMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                    Toast.makeText(context, "Message sent to the seller", Toast.LENGTH_LONG).show();
                                }


                            }
                        });


            }
        });
    }


    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView productNameTextView;
        public ImageView productImageView;
        public TextView productDescTextView;
        public TextView productPriceTextView;
        public Button productBuyButton;
        public CardView productCardView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.textViewProductRecyclerName);
            productImageView = (ImageView) itemView.findViewById(R.id.imageViewProductRecyclerImage);
            productDescTextView = (TextView) itemView.findViewById(R.id.textViewProductRecyclerDescription);


            productPriceTextView = (TextView) itemView.findViewById(R.id.textViewProductRecyclerPrice);
            productBuyButton = (Button) itemView.findViewById(R.id.buttonBuyProduct);

            productCardView = itemView.findViewById(R.id.cardViewForProductsBuyer);
        }
    }

}
