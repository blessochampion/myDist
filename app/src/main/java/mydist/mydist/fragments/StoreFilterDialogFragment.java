package mydist.mydist.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.FilterAdapter;
import mydist.mydist.adapters.StoreProfileAdapter;
import mydist.mydist.models.Brand;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFilterDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onFilterItemListener(brands.get(position).getBrandId());
        cancelDialog();
    }

    private void cancelDialog() {
        getDialog().cancel();
    }

    public interface FilterItemListener {
        public void onFilterItemListener(String key);
    }

    public StoreFilterDialogFragment() {
        // Required empty public constructor
    }

    private FilterItemListener listener;
    List<Brand> brands;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void setListener(FilterItemListener listener) {
        this.listener = listener;
    }

    private Button all;
    private Button cancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_info_details_filter, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.choose_brand));
        GridView filter = (GridView) view.findViewById(R.id.gl_filter);
        brands = DataUtils.getAllBrands(getActivity());
        filter.setAdapter(new FilterAdapter(getActivity(), brands));
        filter.setOnItemClickListener(this);
        setFonts(view);
        all = (Button) view.findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFilterItemListener("ALL");
                cancelDialog();
            }
        });
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
        return view;

    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
