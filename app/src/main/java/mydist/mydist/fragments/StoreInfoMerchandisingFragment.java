package mydist.mydist.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.MerchandizingAdapter;
import mydist.mydist.models.Merchandize;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoMerchandisingFragment extends Fragment {

    ListView mMerchandisingList;

    public StoreInfoMerchandisingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info_merchandising, container, false);
        setFonts(view);
        mMerchandisingList = (ListView) view.findViewById(R.id.lv_merchandisingList);
        List<Merchandize> merchandizes = DataUtils.getAllMerchandize(getActivity());
        mMerchandisingList.setAdapter(new MerchandizingAdapter(getActivity(), merchandizes) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchandizing_list_item, parent, false);
                }
                TextView brandName = (TextView) convertView.findViewById(R.id.brand_name);
                CheckBox item = (CheckBox) convertView.findViewById(R.id.product_name);
                Merchandize merchandize = getItem(position);
                brandName.setText(merchandize.getBrandName());
                item.setText(merchandize.getMerchandizeItem());
                return convertView;
            }
        });
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


}
