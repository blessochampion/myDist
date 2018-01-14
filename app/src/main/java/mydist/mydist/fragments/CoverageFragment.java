package mydist.mydist.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.DailyRetailersAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.RouteDbHelper;
import mydist.mydist.models.Retailer;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoverageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String KEY_WEEK = "week";
    private static final String KEY_DAY = "day";
    private String week;
    private String day;
    private DailyRetailersAdapter adapter;


    public CoverageFragment() {
        // Required empty public constructor
    }

    public static CoverageFragment getNewInstance(String week, String day) {
        CoverageFragment fragment = new CoverageFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_WEEK, week);
        arguments.putString(KEY_DAY, day);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coverage, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        TextView message = (TextView) view.findViewById(R.id.tv_message);
        Bundle bundle = getArguments();

        if (bundle != null) {
            week = bundle.getString(KEY_WEEK);
            day = bundle.getString(KEY_DAY);
            Cursor cursor = DatabaseManager.getInstance(getActivity()).
                    getRetailerByVisitingInfo(week, day);
            if (cursor.getCount() < 1) {
                message.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                adapter = new DailyRetailersAdapter(getActivity(), cursor, this);
                message.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
            }
        } else {
            throw new RuntimeException("Unable to Create Fragment, pass DAY and Week");
        }

        // Inflate the layout for this fragment


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

    private void launchStoreDetailsActivity(String retailerId) {
        Intent intent = new Intent(getActivity(), StoreOverviewActivity.class);
        intent.putExtra(StoreOverviewActivity.KEY_RETAILER_ID, retailerId);
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) adapter.getItem(position);
        String retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_ID));
        launchStoreDetailsActivity(retailerId);
    }
}
