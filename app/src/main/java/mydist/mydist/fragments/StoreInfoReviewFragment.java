package mydist.mydist.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.MerchandizingAdapter;
import mydist.mydist.models.Merchandize;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoReviewFragment extends Fragment {

    ListView mMerchandisingList;

    public StoreInfoReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info_review, container, false);
        mMerchandisingList = (ListView) view.findViewById(R.id.lv_merchandisingList);
        List<Merchandize> merchandizes = DataUtils.getAllMerchandize(getActivity());
        mMerchandisingList.setAdapter(new MerchandizingAdapter(getActivity(), merchandizes));
        setFonts(view);
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

}
