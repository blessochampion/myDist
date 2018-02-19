package mydist.mydist.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.LoginActivity;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.DailyRetailersAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.RouteDbHelper;
import mydist.mydist.models.Retailer;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

import static mydist.mydist.utils.Days.getCurrentDay;
import static mydist.mydist.utils.Days.getThisWeek;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoverageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String KEY_WEEK = "week";
    private static final String KEY_DAY = "day";
    private static final String DELIMITER = ":";
    private String week;
    private String day;
    private DailyRetailersAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private String weekDay;

    public CoverageFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coverage, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        TextView message = (TextView) view.findViewById(R.id.tv_message);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_progress_loading_indicator);
        Bundle bundle = getArguments();
        if (bundle != null) {
            week = bundle.getString(KEY_WEEK);
            day = bundle.getString(KEY_DAY);
            weekDay = week + DELIMITER + day;
            Cursor cursor = DatabaseManager.getInstance(getActivity()).
                    getRetailerByVisitingInfo(week, day);
            if (cursor.getCount() < 1) {
                message.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.GONE);
            } else {
                mLoadingIndicator.setVisibility(View.GONE);
                adapter = new DailyRetailersAdapter(getActivity(), cursor, this);
                message.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
            }
        } else {
            throw new RuntimeException("Unable to Create Fragment, pass DAY and Week");
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
        StoreProfileHistoryDialogFragment.getNewInstance(v.getTag().toString()).show(getActivity().
                getSupportFragmentManager(), "");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String week = getThisWeek();
        String day = getCurrentDay();
        if ((week + DELIMITER + day).equals(weekDay)) {
            Cursor cursor = (Cursor) adapter.getItem(position);
            String retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_ID));
            launchStoreDetailsActivity(retailerId);
        } else {
            UIUtils.launchDialog(getString(R.string.retailer_not_for_today), getActivity());
        }
    }

    private void launchStoreDetailsActivity(String retailerId) {
        Intent intent = new Intent(getActivity(), StoreOverviewActivity.class);
        intent.putExtra(StoreOverviewActivity.KEY_RETAILER_ID, retailerId);
        startActivity(intent);
    }
}
