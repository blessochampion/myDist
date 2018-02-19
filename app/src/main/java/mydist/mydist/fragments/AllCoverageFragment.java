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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.DailyRetailersAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Retailer;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.utils.Days.getCurrentDay;
import static mydist.mydist.utils.Days.getDay;
import static mydist.mydist.utils.Days.getThisWeek;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCoverageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, StoreDeviateDialogFragment.RetailerDateChangeListener {

    DailyRetailersAdapter adapter;
    Cursor cursor;
    View view;
    ListView listView;
    TextView message;
    private ProgressBar mLoadingIndicator;

    public AllCoverageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coverage, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        message = (TextView) view.findViewById(R.id.tv_message);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_progress_loading_indicator);
        bindView();
        return view;
    }

    private void bindView() {
        String week = getThisWeek();
        String day = getCurrentDay();
        Cursor todaysRetailerscursor = DatabaseManager.getInstance(getActivity()).
                getRetailerByVisitingInfo(week, day);
        String filter = null;
        if (todaysRetailerscursor.getCount() > 0) {
            filter = getRetailers(todaysRetailerscursor);
        }
        cursor = DatabaseManager.getInstance(getActivity()).getAllRetailerExceptTheCurrentDate(filter);
        if (cursor.getCount() < 1) {
            message.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.GONE);
        } else {
            adapter = new DailyRetailersAdapter(getActivity(), cursor, this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            message.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.GONE);
        }
        setFonts(view);
    }

    private String getRetailers(Cursor todaysRetailerscursor) {
        String result = "(";
        todaysRetailerscursor.moveToFirst();
        int count = todaysRetailerscursor.getCount();
        for (int i = 0; i < count - 1; i++) {
            result += "\"" + todaysRetailerscursor.getString(todaysRetailerscursor.
                    getColumnIndex(MasterContract.RetailerContract.RETAILER_ID)) + "\"" + ", ";
            todaysRetailerscursor.moveToNext();
        }
        result += "\"" + todaysRetailerscursor.getString(todaysRetailerscursor.
                getColumnIndex(MasterContract.RetailerContract.RETAILER_ID)) + "\"" + ")";
        return result;
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
        String week = getThisWeek();
        String day = getCurrentDay();
        if (DatabaseManager.getInstance(getActivity()).changeRetailerVisitingDate(id, Days.getTodayDate(), day, week)) {
            Toast.makeText(getActivity(), getString(R.string.date_changed), Toast.LENGTH_LONG).show();
            bindView();
        }
    }
}
