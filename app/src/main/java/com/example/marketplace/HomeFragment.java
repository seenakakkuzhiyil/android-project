package com.example.marketplace;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {


    private DatabaseReference ProductRef;
    RecyclerView recyclerView;
    Query query;
    SQLiteDatabase db;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView = (RecyclerView) layoutView.findViewById(R.id.recyclerViewProducts);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        query = ProductRef.orderByChild("userId").equalTo(userId);


        //ProductViewAdapter adapter = new ProductViewAdapter();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));




        return layoutView;

    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product.class).build();
        FirebaseRecyclerAdapter<Product, ProductListViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductListViewHolder productListViewHolder, int i, @NonNull final Product product) {

//                if(product.getUserId() == FirebaseAuth.getInstance().getCurrentUser().getUid())


                    productListViewHolder.productNameTextView.setText(product.getItemName());
                    productListViewHolder.productDescTextView.setText(product.getItemDescription());
                    productListViewHolder.productPriceTextView.setText("$" +product.getItemPrice());
                    Picasso.get().load(product.getItemPhoto()).into(productListViewHolder.productImageView);


                    productListViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String itemToDelete = product.getItemId();
                            db = getActivity().openOrCreateDatabase("MarketPlaceProject.db",Context.MODE_PRIVATE,null);
                            db.delete("products","ItemId=?",new String[]{product.getItemId()});
                            ProductRef.child(itemToDelete).removeValue();
                        }
                    });

                    productListViewHolder.updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), UpdateActivity.class);
//                            intent.putExtra("itemId",product.getItemId());
//                            startActivity(intent);
                            Bundle productId = new Bundle();
                            productId.putString("itemId",product.getItemId());

                            Fragment myFragment = null;
                            Class fragmentClass;

                            fragmentClass = AddProductFragment.class;

                            try
                            {
                                myFragment = (Fragment) fragmentClass.newInstance();

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                            myFragment.setArguments(productId);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frame_content,myFragment)
                                    .commit();

                            getActivity().setTitle("Update Product");

                        }
                    });
            }

            @NonNull
            @Override
            public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_recycler,parent,false);
                return new ProductListViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

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
