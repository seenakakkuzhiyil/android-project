package com.example.marketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.marketplace.BuyerFragments.BuyerHomeFragment;

public class CustomDialog extends DialogFragment {

    ImageView closeImage;
    Button submitFilterButton;
    RadioGroup categoryRadioGroup;
    RadioGroup priceRangeRadioGroup;
    String minPrice, maxPrice;
    String category;
    int priceId;
    int categoryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View layoutView = inflater.inflate(R.layout.filterpopup,container,false);


        submitFilterButton = layoutView.findViewById(R.id.buttonSubmitFilter);
        closeImage = layoutView.findViewById(R.id.imageViewCloseWindow);
        priceRangeRadioGroup = layoutView.findViewById(R.id.radioGroupPriceRange);
        categoryRadioGroup = layoutView.findViewById(R.id.radioGroupCategory);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        submitFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priceId = priceRangeRadioGroup.getCheckedRadioButtonId();

                switch(priceId)
                {
                    case R.id.radioButtonUnder100:
                        minPrice = "0.0";
                        maxPrice = "100.0";
                        break;
                    case R.id.radioButtonBetween100And500:
                        minPrice = "100.0";
                        maxPrice = "500.0";
                        break;
                    case R.id.radioButtonAbove500:
                        minPrice = "500.0";
                        maxPrice = "999999.99";
                        break;
                    default:
                        minPrice = "0.0";
                        maxPrice = "999999.99";

                }

                categoryId = categoryRadioGroup.getCheckedRadioButtonId();
                switch(categoryId)
                {
                    case R.id.radioButtonAppliances:
                        category ="Appliances";
                        break;
                    case R.id.radioButtonAutombiles:
                        category ="Automobiles";
                        break;
                    case R.id.radioButtonBeauty:
                        category ="Beauty";
                        break;
                    case R.id.radioButtonElectronics:
                        category ="Electronics";
                        break;
                    default: category = "";
                }


                Bundle filterData = new Bundle();
                filterData.putString("minPrice",minPrice);
                filterData.putString("maxPrice",maxPrice);
                filterData.putString("category",category);

                Fragment myFragment = null;
                Class fragmentClass;

                fragmentClass = BuyerHomeFragment.class;

                try
                {
                    myFragment = (Fragment) fragmentClass.newInstance();

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                myFragment.setArguments(filterData);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.buyer_frame_content,myFragment)
                        .commit();

                getDialog().dismiss();

            }
        });




        return layoutView;
    }
}
