package com.example.marketplace;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplace.SellerMessageListViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
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


public class MessagesFragment extends Fragment {

    DatabaseReference messageRef, productRef;
    RecyclerView recyclerViewSellerMessages;
    FirebaseRecyclerAdapter<Message, SellerMessageListViewHolder> firebaseListAdapter;
    Query query;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerViewSellerMessages = (RecyclerView) layoutView.findViewById(R.id.listViewSellerMessages);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        query = messageRef.orderByChild("sellerId").equalTo(userId);


        recyclerViewSellerMessages.setLayoutManager(new GridLayoutManager(getActivity(),1));


        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Message> messages = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        firebaseListAdapter = new FirebaseRecyclerAdapter<Message, SellerMessageListViewHolder>(messages) {
            @Override
            protected void onBindViewHolder(@NonNull final SellerMessageListViewHolder sellerMessageListViewHolder, int i, @NonNull final Message message) {




                productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(message.getItemId());
                productRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            sellerMessageListViewHolder.listSellerMessage.setText(dataSnapshot.child("itemName").getValue().toString());
                            String image = dataSnapshot.child("itemPhoto").getValue().toString();
                            // Picasso.get().load(product.getItemPhoto()).into(productListViewHolder.productImageView);
                            Picasso.get().load(image).into(sellerMessageListViewHolder.listSellerImage);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(!message.getMessages().equals("Waiting"))
                {
                    sellerMessageListViewHolder.buttonReject.setVisibility(View.INVISIBLE);
                    sellerMessageListViewHolder.buttonApprove.setVisibility(View.INVISIBLE);
                    sellerMessageListViewHolder.textViewApproved.setVisibility(View.VISIBLE);
                    sellerMessageListViewHolder.textViewApproved.setText(message.getMessages());
                }
                else
                {
                    sellerMessageListViewHolder.textViewApproved.setVisibility(View.INVISIBLE);
                }

                sellerMessageListViewHolder.buttonApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        message.setMessages("Approved");
                        sellerMessageListViewHolder.textViewApproved.setText(message.getMessages());
                        messageRef.child(message.getMessageId()).setValue(message);
                    }
                });

                sellerMessageListViewHolder.buttonReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        message.setMessages("Rejected");
                        sellerMessageListViewHolder.textViewApproved.setText(message.getMessages());
                        messageRef.child(message.getMessageId()).setValue(message);
//                        messageRef.child(message.getMessageId()).removeValue();
                    }
                });


            }

            @NonNull
            @Override
            public SellerMessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_seller_messages,parent,false);
                return new SellerMessageListViewHolder(view);
            }
        };

        recyclerViewSellerMessages.setAdapter(firebaseListAdapter);
        firebaseListAdapter.startListening();

    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        firebaseListAdapter.stopListening();
//    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MessagesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
