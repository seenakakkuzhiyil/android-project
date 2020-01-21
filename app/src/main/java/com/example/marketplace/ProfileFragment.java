package com.example.marketplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import org.w3c.dom.Text;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    ImageView inputImage;
    TextView header;
    EditText inputFirstName, inputLastName, inputEmail, inputPhone;
    Button updateButton;
    private static final int GALLERYPICTURE = 1;
    private Uri ImageUri;
    String fname, lname, email, phone;
    String userID, userEmail, downloadImageUrl;
    private StorageReference UserImageRef;
    private DatabaseReference UserRef;
    private DatabaseReference UserDetailsRef;
    Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View layoutView = inflater.inflate(R.layout.fragment_profile, container, false);




        //file name in the Firebase Storage for storing the user's profile pictures
        UserImageRef = FirebaseStorage.getInstance().getReference().child("User Images");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");



        //all the components associated with the fragment layoutView
        header = layoutView.findViewById(R.id.textViewProfileHeader);
        inputImage = layoutView.findViewById(R.id.imageViewProfileImage);
        inputFirstName = layoutView.findViewById(R.id.editTextFirstName);
        inputLastName = layoutView.findViewById(R.id.editTextLastName);
        inputEmail = layoutView.findViewById(R.id.editTextEmail);
        inputPhone = layoutView.findViewById(R.id.editTextPhone);
        updateButton = layoutView.findViewById(R.id.buttonUpdateProfile);


        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        inputEmail.setText(userEmail);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        UserDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        UserDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    fname = dataSnapshot.child("firstName").getValue().toString();
                    lname = dataSnapshot.child("lastName").getValue().toString();
                    phone = dataSnapshot.child("phone").getValue().toString();
                    downloadImageUrl = dataSnapshot.child("image").getValue().toString();
                    inputFirstName.setText(fname);
                    inputLastName.setText(lname);
                    inputPhone.setText(phone);
                    Picasso.get().load(downloadImageUrl).into(inputImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateUserInformation();
            }
        });


        return layoutView;
    }

    private void ValidateUserInformation() {

        fname = inputFirstName.getText().toString();
        lname = inputLastName.getText().toString();
        email = inputEmail.getText().toString();
        phone = inputPhone.getText().toString();

        if(ImageUri == null && downloadImageUrl == null)
        {
            Toast.makeText(getActivity(), "Profile Image is necessary", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fname))
        {
            Toast.makeText(getActivity(), "First name is empty!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lname))
        {
            Toast.makeText(getActivity(), "Last name is empty!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(getActivity(), "Phone number is empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreUserInformation();
        }
    }

    private void StoreUserInformation() {

        if(ImageUri != null) {
            //link in the firebase storage for the user image
            final StorageReference filePath = UserImageRef.child(ImageUri.getLastPathSegment() + userID + ".jpg");

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
                    //Toast.makeText(getActivity(), "Image uploaded Succesfully to the Database", Toast.LENGTH_SHORT).show();
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
                               // Toast.makeText(getActivity(), "User URL is obtained from the storage", Toast.LENGTH_SHORT).show();

                                SaveuserInformationToDatabase();
                            }
                        }
                    });
                }
            });
        }
        else
        {
            SaveuserInformationToDatabase();
        }

    }

    private void SaveuserInformationToDatabase() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId",userID);
        userMap.put("firstName", fname);
        userMap.put("lastName",lname);
        userMap.put("email",email);
        userMap.put("phone",phone);
        userMap.put("image",downloadImageUrl);

        UserRef.child(userID).updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {


//                            Fragment myFragment = null;
//                            Class fragmentClass;
//
//                            fragmentClass = HomeFragment.class;
//                            try
//                            {
//                                myFragment = (Fragment) fragmentClass.newInstance();
//
//                            }catch(Exception e)
//                            {
//                                e.printStackTrace();
//                            }
//
//
//                            getFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.frame_content,myFragment)
//                                    .commit();
//
//                            getActivity().setTitle("Home");


                            startActivity(new Intent(getActivity(),SellHomePageAvtivity.class));

                            Toast.makeText(getActivity(), "User Information Updated", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error "+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //opening the gallery on image click
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







//Automatically created along with the fragment


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
