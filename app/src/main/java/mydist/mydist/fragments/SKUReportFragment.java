package mydist.mydist.fragments;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.BrandSpinnerAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Brand;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SKUReportFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner mBrand;
    List<Brand> brands;

    public SKUReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skureport, container, false);
        mBrand = (Spinner) view.findViewById(R.id.sp_brand);
        mBrand.setOnItemSelectedListener(this);
        setupBrandSpinner();
        setFonts(view);
        return view;
    }

    private void setupBrandSpinner() {
        Context context = getActivity();
        brands = DataUtils.getAllBrands(context);
        mBrand.setAdapter(new BrandSpinnerAdapter(context, brands));
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Brand brand = brands.get(position);
        Cursor cursor = DatabaseManager.getInstance(getActivity()).getProductInvoiceByBrandId(brand.getBrandId(), Days.getTodayDate());
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.e(brand.getBrandName(), ":" + cursor.getString(cursor.getColumnIndex(MasterContract.ProductOrderContract.PRODUCT_NAME)));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
