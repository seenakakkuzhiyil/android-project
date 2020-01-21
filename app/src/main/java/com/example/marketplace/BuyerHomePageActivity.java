package com.example.marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketplace.BuyerFragments.BuyerHomeFragment;
import com.example.marketplace.BuyerFragments.BuyerMessagesFragment;
import com.example.marketplace.BuyerFragments.BuyerMyProductsFragment;
import com.example.marketplace.BuyerFragments.BuyerProfileFragment;
import com.example.marketplace.BuyerFragments.BuyerWalletFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BuyerHomePageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

    Fragment myFragment = null;
    Class fragmentClass = HomeFragment.class;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    DatabaseReference userRef;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home_page);


        showHome();

        Intent intent = getIntent();
        String category = intent.getExtras().get("category").toString();
        Bundle bundle = new Bundle();
        bundle.putString("category",category);
        if(bundle != null && myFragment.getArguments() == null)
        {
            myFragment.setArguments(bundle);
        }

        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_buyer_home_page);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        NavigationView navigation = (NavigationView)findViewById(R.id.navigationView_menu_buyer);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigation);


        View headerView = navigation.inflateHeaderView(R.layout.header_layout);
        final TextView headerUsername = headerView.findViewById(R.id.textViewHeaderUsername);
        final ImageView headerImage = headerView.findViewById(R.id.imageViewHeaderUserImage);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(BuyerHomePageActivity.this, MainActivity.class));
                }
            }
        };

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                    String lastName = dataSnapshot.child("lastName").getValue().toString();
                    String userImage = dataSnapshot.child("image").getValue().toString();

                    String userName = firstName + " " +lastName;
                    headerUsername.setText(userName);
                    Picasso.get().load(userImage).into(headerImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHome() {
        fragmentClass = BuyerHomeFragment.class;
        try {
            myFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.buyer_frame_content, myFragment).commit();
        setTitle("Home");
        //startActivity(new Intent(SellHomePageAvtivity.this, SellHomePageAvtivity.class));
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView_menu_buyer);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        //navigationView.getMenu().getItem(4).setChecked(false);
        //navigationView.getMenu().getItem(5).setChecked(false);
        //navigationView.getMenu().getItem(i).setChecked(false)
    }

    @Override
    public void onBackPressed() {

        if(myFragment == null)
        {
            startActivity(new Intent(BuyerHomePageActivity.this,CategoryActivity.class));
        }

        else if(myFragment instanceof BuyerHomeFragment)
        {
            super.onBackPressed();

        }
        else
        {

            super.onBackPressed();
        }



    }

    public void selectItemDrawer(MenuItem menuItem)
    {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationView_menu_buyer);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        //navigationView.getMenu().getItem(4).setChecked(false);
        //navigationView.getMenu().getItem(5).setChecked(false);
        switch(menuItem.getItemId())
        {
            case R.id.buyer_home:
                fragmentClass = BuyerHomeFragment.class;
                break;
            case R.id.buyer_profile:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.buyer_message:
                fragmentClass = BuyerMessagesFragment.class;
                break;
//            case R.id.buyer_product:
//                fragmentClass = BuyerMyProductsFragment.class;
//                break;
//            case R.id.buyer_wallet:
//                fragmentClass = BuyerWalletFragment.class;
//                break;
            case R.id.buyer_logout:
                Toast.makeText(BuyerHomePageActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                fragmentClass = LogoutFragment.class;
                break;
            default:
                fragmentClass = BuyerHomeFragment.class;

        }
        try
        {

            myFragment = (Fragment) fragmentClass.newInstance();


        }catch(Exception e)
        {
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.buyer_frame_content,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();


    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                selectItemDrawer(menuItem);
                return true;
            }
        });
    }


}
