package mydist.mydist.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.BrandSpinnerAdapter;
import mydist.mydist.models.Brand;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SKUReportFragment extends Fragment {

    Spinner mBrand;

    public SKUReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_skureport, container, false);
        mBrand = (Spinner) view.findViewById(R.id.sp_brand);
        setupBrandSpinner();
        setFonts(view);
        return view;
    }

    private void setupBrandSpinner()
    {
        Context context  =  getActivity();
        final List<Brand> brands = DataUtils.getAllBrands(context);
        mBrand.setAdapter(new BrandSpinnerAdapter(context, brands));
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


}
