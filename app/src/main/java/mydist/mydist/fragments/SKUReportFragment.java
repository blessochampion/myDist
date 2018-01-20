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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.BrandSpinnerAdapter;
import mydist.mydist.adapters.SkuAdapter;
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
    View view;
    SkuAdapter mAdapter;
    ListView mListView;
    Cursor cursor;
    TextView mNoProduct;

    public SKUReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_skureport, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mBrand = (Spinner) view.findViewById(R.id.sp_brand);
        mBrand.setOnItemSelectedListener(this);
        mNoProduct = (TextView) view.findViewById(R.id.product_brand_name);
        setupBrandSpinner();
        setFonts();
        return view;
    }

    private void setupBrandSpinner() {
        Context context = getActivity();
        brands = DataUtils.getAllBrands(context);
        mBrand.setAdapter(new BrandSpinnerAdapter(context, brands));
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(view.findViewById(R.id.parent_layout), ralewayFont);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Brand brand = brands.get(position);
        cursor = DatabaseManager.getInstance(getActivity()).getProductInvoiceByBrandId(brand.getBrandId(), Days.getTodayDate());
        if (cursor.getCount() > 0) {
            mNoProduct.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            showProductUpdate();
        }else {
            mNoProduct.setText(getString(R.string.no_product_for_brand, brand.getBrandName()));
            mNoProduct.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

    }

    private void showProductUpdate() {
        mAdapter = new SkuAdapter(getActivity(), cursor);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
