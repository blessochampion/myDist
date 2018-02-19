package mydist.mydist.fragments;


import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.MerchandizingAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Merchandize;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.DatabaseLogicUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoReviewFragment extends Fragment {

    ListView mMerchandisingList;
    TextView mStoreTarget;

    public StoreInfoReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info_review, container, false);
        mMerchandisingList = (ListView) view.findViewById(R.id.lv_merchandisingList);
        mStoreTarget = (TextView) view.findViewById(R.id.tv_store_target_value);
        new LoadDetailsTask().execute();
        setFonts(view);
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    class LoadDetailsTask extends AsyncTask<Void, Void, Void> {
        List<Merchandize> merchandizes = new ArrayList<>();
        String storeTarget = DatabaseLogicUtils.DEFAULT_HPV;

        @Override
        protected Void doInBackground(Void... voids) {
            merchandizes = DataUtils.getAllMerchandize(getActivity());
            Cursor cursor = DatabaseManager.getInstance(getActivity()).getHPV(StoreOverviewActivity.retailerId);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                storeTarget = cursor.getString(cursor.getColumnIndex(MasterContract.HighestPurchaseValueContract.VALUE));
                storeTarget = DatabaseLogicUtils.getHighestPurchaseEver(storeTarget);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMerchandisingList.setAdapter(new MerchandizingAdapter(getActivity(), merchandizes));
            mStoreTarget.setText(getActivity().getText(R.string.naira) + String.format("%,.2f", Double.valueOf(storeTarget)));
        }
    }
}
