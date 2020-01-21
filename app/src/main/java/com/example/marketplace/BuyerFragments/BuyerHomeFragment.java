package com.example.marketplace.BuyerFragments;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.marketplace.BuyerRecyclerAdapter;
import com.example.marketplace.CustomDialog;
import com.example.marketplace.Product;

import com.example.marketplace.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BuyerHomeFragment extends Fragment {

    DatabaseReference ProductRef;
    RecyclerView recyclerView;
    String category;
    String userId;
    Query query;
    Button filterButton;
    double maxPrice = 999999.0;
    double minPrice = 0.0;

    SQLiteDatabase db;
    String queryString;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_buyer_home,container,false);

        recyclerView = layoutView.findViewById(R.id.recyclerViewProductsBuyerHome);
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(getArguments() != null)
        {
            category = getArguments().getString("category");
        }
        else
        {
            category = "";
            //Toast.makeText(getActivity(), "Appliances, Go back to choose a category", Toast.LENGTH_SHORT).show();
        }

        if(getArguments() != null && getArguments().getString("minPrice") != null)
        {
            minPrice = Double.parseDouble(getArguments().getString("minPrice"));
        }
        if(getArguments() != null && getArguments().getString("maxPrice") != null)
        {
            maxPrice = Double.parseDouble(getArguments().getString("maxPrice"));
        }



        //open database
        db = getActivity().openOrCreateDatabase("MarketPlaceProject.db",android.content.Context.MODE_PRIVATE , null);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));


        List<Product> filterProducts = new ArrayList<Product>();
              filterProducts  = GetProducts();

              if(filterProducts != null)
        recyclerView.setAdapter(new BuyerRecyclerAdapter(filterProducts,getContext()));

        filterButton = layoutView.findViewById(R.id.buttonFilter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog customDialog = new CustomDialog();
                customDialog.show(getFragmentManager(),"CustomDialog");
            }
        });

        return layoutView;
    }

    private List<Product> GetProducts()
    {
        List<Product> productList = new ArrayList<Product>();
        String queryStr;
        Cursor cursor;

        if(getArguments() == null)
        {
            queryStr = "SELECT * FROM products;";
            cursor = db.rawQuery(queryStr, null);
        }
        else
        {

           queryStr = "SELECT * FROM products where itemCategory like ? and itemPrice >= ? and itemPrice < ? ;";
           cursor = db.rawQuery(queryStr, new String[]{"%"+category,String.valueOf(minPrice),String.valueOf(maxPrice)});

        }

        try
        {


            if(cursor != null)
            {
                cursor.moveToFirst();
                while(!cursor.isAfterLast())
                {
                    Product eachProduct = new Product();
                    eachProduct.setItemId(cursor.getString(0).toString());
                    eachProduct.setItemCategory(cursor.getString(1).toString());
                    eachProduct.setItemDescription(cursor.getString(2).toString());
                    eachProduct.setItemName(cursor.getString(3).toString());
                    eachProduct.setItemPhoto(cursor.getString(4).toString());
                    eachProduct.setItemPrice(cursor.getString(5).toString());
                    eachProduct.setItemUploadDate(cursor.getString(6).toString());
                    eachProduct.setItemUploadTime(cursor.getString(7).toString());
                    eachProduct.setUserId(cursor.getString(8).toString());


                    productList.add(eachProduct);
                    cursor.moveToNext();
                }
            }

        }catch(Exception e)
        {
            Log.e("Demo", e.getMessage());
        }


        return productList;


    }








    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BuyerHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BuyerHomeFragment newInstance(String param1, String param2) {
        BuyerHomeFragment fragment = new BuyerHomeFragment();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
