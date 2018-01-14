package mydist.mydist.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.DailyRetailersAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Retailer;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCoverageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, StoreDeviateDialogFragment.RetailerDateChangeListener {

    DailyRetailersAdapter adapter;

    public AllCoverageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coverage, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        TextView message = (TextView) view.findViewById(R.id.tv_message);
        Cursor cursor = DatabaseManager.getInstance(getActivity()).
                getAllRetailer();
        if(cursor.getCount() < 1){
            message.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else {
            adapter = new DailyRetailersAdapter(getActivity(), cursor, this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            message.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        setFonts(view);
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    @Override
    public void onClick(View v) {

        StoreProfileHistoryDialogFragment.getNewInstance(v.getTag().toString())
                .show(getActivity().
                        getSupportFragmentManager(),
                        "");

    }

    private void launchStoreDetailsActivity() {
        Intent intent = new Intent(getActivity(), StoreOverviewActivity.class);
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = ((Cursor) adapter.getItem(position));
        String retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_ID));
        StoreDeviateDialogFragment dialogFragment = StoreDeviateDialogFragment.getInstance(retailerId);
        dialogFragment.setListener(this);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void onDateChangeRequested(String id) {
        Log.e("eee", id);
    }
}
