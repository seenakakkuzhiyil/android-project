package com.example.marketplace;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class AddProductFragment extends Fragment {


    FirebaseAuth mAuth;
    ImageView inputImage;
    TextView header;
    EditText inputItemName, inputDescription, inputPrice, inputPhoto;
    Button addProductButton;
    Spinner inputCategory;
    private static final int GALLERYPICTURE = 1;
    private Uri ImageUri;
    String itemName, itemDescription, itemPrice, itemPhoto, itemCategory;
    String itemID, userID, downloadImageUrl;
    String saveCurrentDate, saveCurrentTime;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductDatabaseRef, ProductDetailsRef;

    //for update details
    String itemId;

    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_add_product, container, false);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");

        inputImage = layoutView.findViewById(R.id.imageViewProductUploadImage);
        inputItemName = layoutView.findViewById(R.id.editTextProductName);
        inputDescription = layoutView.findViewById(R.id.editTextProductDescription);
        inputCategory = layoutView.findViewById(R.id.spinnerProductCategory);
        inputPrice = layoutView.findViewById(R.id.editTextProductPrice);
        addProductButton = layoutView.findViewById(R.id.buttonAddProduct);

        if(getArguments() != null)
        {
            addProductButton.setText("Update");
            itemId = getArguments().getString("itemId");

            ProductDetailsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(itemId);

            ProductDetailsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String category, image;
                        int id;
                        itemName = dataSnapshot.child("itemName").getValue().toString();
                        itemDescription = dataSnapshot.child("itemDescription").getValue().toString();
                        itemPrice = dataSnapshot.child("itemPrice").getValue().toString();
                        downloadImageUrl = dataSnapshot.child("itemPhoto").getValue().toString();


                        inputItemName.setText(itemName);
                        inputDescription.setText(itemDescription);
                        inputPrice.setText(itemPrice);

                        if(dataSnapshot.child("itemCategory").getValue().toString() != null) {
                            itemCategory = dataSnapshot.child("itemCategory").getValue().toString();


                            switch (itemCategory) {
                                case "Appliances":
                                    id = 0;
                                    break;
                                case "Automobiles":
                                    id = 1;
                                    break;
                                case "Beauty":
                                    id = 2;
                                    break;
                                case "Electronics":
                                    id = 3;
                                    break;

                            }
                        }
                        inputCategory.setSelection(0);



                        Picasso.get().load(downloadImageUrl).into(inputImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        //open local database
        db = getActivity().openOrCreateDatabase("MarketPlaceProject.db",android.content.Context.MODE_PRIVATE , null);
//
//        db.execSQL("DROP TABLE IF EXISTS products;");
//        String createProductsTable = "CREATE TABLE products (ItemId TEXT PRIMARY KEY, " +
//                "itemCategory TEXT, " +
//                "itemDescription TEXT," +
//                "itemName TEXT," +
//                "itemPhoto TEXT," +
//                "itemPrice DECIMAL," +
//                "itemUploadDate DATE," +
//                "itemUploadTime DATETIME," +
//                "userId TEXT);";
//
//        db.execSQL(createProductsTable);

//        db.delete("products","itemName=?",new String[]{"qwer"});




        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductInformation();
            }
        });



        return layoutView;
    }

    private void ValidateProductInformation() {

        itemName = inputItemName.getText().toString();
        itemDescription = inputDescription.getText().toString();
        itemPrice = inputPrice.getText().toString();
        itemCategory = inputCategory.getSelectedItem().toString();

        if(ImageUri == null && downloadImageUrl == null ){
            Toast.makeText(getActivity(), "Product Image is necessary", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(itemName))
        {
            Toast.makeText(getActivity(), "Item Name is empty!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(itemDescription))
        {
            Toast.makeText(getActivity(), "Item Description is empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(itemPrice))
        {
            Toast.makeText(getActivity(), "Item Price is empty", Toast.LENGTH_SHORT).show();
        }
        else
        {

            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        if(addProductButton.getText() == "Update")
        {
            itemID = itemId;
        }
        else
        {
            itemID = saveCurrentDate + saveCurrentTime;
        }

if(ImageUri != null) {


    //link in the firebase storage for the user image
    final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + itemID + ".jpg");

    final UploadTask uploadTask = filePath.putFile(ImageUri);

    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            String message = e.toString();
            Toast.makeText(getActivity(), "Error " + message, Toast.LENGTH_SHORT).show();
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
           // Toast.makeText(getActivity(), "Image uploaded Succesfully to the Database", Toast.LENGTH_SHORT).show();
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }

                    downloadImageUrl = filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadImageUrl = task.getResult().toString();
                        //Toast.makeText(getActivity(), "User URL is obtained from the storage", Toast.LENGTH_SHORT).show();

                        SaveProductInformationToDatabase();
                    }
                }
            });
        }
    });
}
else
{
    SaveProductInformationToDatabase();
}

    }

    private void SaveProductInformationToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("ItemId",itemID);
        productMap.put("userId",userID);
        productMap.put("itemName", itemName);
        productMap.put("itemCategory",itemCategory);
        productMap.put("itemDescription",itemDescription);
        productMap.put("itemPrice",itemPrice);
        productMap.put("itemUploadDate",saveCurrentDate);
        productMap.put("itemUploadTime",saveCurrentTime);
        productMap.put("itemPhoto",downloadImageUrl);

        long result = 0;
        ContentValues val = new ContentValues();
        val.put("ItemId",itemID);
        val.put("itemCategory",itemCategory);
        val.put("itemDescription",itemDescription);
        val.put("itemName",itemName);
        val.put("itemPhoto",downloadImageUrl);
        val.put("itemPrice",Double.parseDouble(itemPrice));
        val.put("itemUploadDate",saveCurrentDate);
        val.put("itemUploadTime",saveCurrentTime);
        val.put("userId",userID);

        result = db.insert("products",null,val);
        if(ImageUri == null)
        {
            db.update("products",val,"itemId=?",new String[]{itemID});
        }

        ProductDatabaseRef.child(itemID).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(getActivity(),SellHomePageAvtivity.class));

                            Toast.makeText(getActivity(), "Product Information added", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error "+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Activity.RESULT_OK

        if(requestCode == GALLERYPICTURE && resultCode==RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            inputImage.setImageURI(ImageUri);

        }
    }








    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddProductFragment() {
        // Required empty public constructor
    }


    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
