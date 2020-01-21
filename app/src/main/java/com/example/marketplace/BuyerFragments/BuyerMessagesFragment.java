package com.example.marketplace.BuyerFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketplace.BuyerMessageListViewHolder;
import com.example.marketplace.Message;
import com.example.marketplace.R;
import com.example.marketplace.SellerMessageListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class BuyerMessagesFragment extends Fragment {

    DatabaseReference messageRef, productRef, userRef;
    RecyclerView recyclerViewBuyerMessages;
    FirebaseRecyclerAdapter<Message, BuyerMessageListViewHolder> firebaseListAdapter;
    Query query;
    String userId;
    String details = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView =  inflater.inflate(R.layout.fragment_buyer_messages, container, false);

        recyclerViewBuyerMessages = layoutView.findViewById(R.id.listViewBuyerMessages);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        query = messageRef.orderByChild("buyerId").equalTo(userId);

        recyclerViewBuyerMessages.setLayoutManager(new GridLayoutManager(getActivity(),1));

        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Message> messages = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        firebaseListAdapter = new FirebaseRecyclerAdapter<Message, BuyerMessageListViewHolder>(messages) {
            @Override
            protected void onBindViewHolder(@NonNull final BuyerMessageListViewHolder buyerMessageListViewHolder, int i, @NonNull final Message message) {


                productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(message.getItemId());
                productRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            buyerMessageListViewHolder.listBuyerMessage.setText(dataSnapshot.child("itemName").getValue().toString());
                            String image = dataSnapshot.child("itemPhoto").getValue().toString();
                            // Picasso.get().load(product.getItemPhoto()).into(productListViewHolder.productImageView);
                            Picasso.get().load(image).into(buyerMessageListViewHolder.listBuyerImage);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final boolean flag = message.getMessages().equals("Approved");
                if(message.getMessages().equals("Approved"))
                {
                    //buyerMessageListViewHolder.buttonCancel.setText("Proceed To Payment");
                    buyerMessageListViewHolder.buttonCancel.setVisibility(View.INVISIBLE);
                    buyerMessageListViewHolder.textStatus.setVisibility(View.VISIBLE);
                    details = "";
                    userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(message.getSellerId());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {

                                details += "Seller Name: ";
                                details += dataSnapshot.child("firstName").getValue().toString();
                                details += " "+dataSnapshot.child("lastName").getValue().toString();
                                details += "\nContact No: "+ dataSnapshot.child("phone").getValue().toString();
                                buyerMessageListViewHolder.textStatus.setText(details);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                if(message.getMessages().equals("Rejected"))
                {
                    buyerMessageListViewHolder.buttonCancel.setVisibility(View.INVISIBLE);
                    buyerMessageListViewHolder.textStatus.setText(message.getMessages());

                }

                buyerMessageListViewHolder.buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //go to wallet fragment from here
//                        if(flag)
//                        {
//
//                        }
//                        else
//                        {
                            messageRef.child(message.getMessageId()).removeValue();
//                        }
                    }
                });


            }

            @NonNull
            @Override
            public BuyerMessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_buyer_messages,parent,false);
                return new BuyerMessageListViewHolder(view);
            }
        };

        recyclerViewBuyerMessages.setAdapter(firebaseListAdapter);
        firebaseListAdapter.startListening();
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BuyerMessagesFragment() {
        // Required empty public constructor
    }

    public static BuyerMessagesFragment newInstance(String param1, String param2) {
        BuyerMessagesFragment fragment = new BuyerMessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
