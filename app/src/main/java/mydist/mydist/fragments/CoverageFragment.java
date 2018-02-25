package mydist.mydist.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.DailyRetailersAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

import static mydist.mydist.utils.Days.getCurrentDay;
import static mydist.mydist.utils.Days.getThisWeek;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoverageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String KEY_WEEK = "week";
    private static final String KEY_DAY = "day";
    private static final String DELIMITER = ":";
    private static final int LOAD_RETAILERS_ID = 1;
    private static final int FILTER_RETAILERS_ID = 2;
    public static final String QUERY_ALL = "all";
    private String week;
    private String day;
    private DailyRetailersAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private String weekDay;
    private ListView mListView;
    private TextView mMessage;
    private View parentView;
    private static String searchFilter;
    List<String> retailerIds = new ArrayList<>();

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
        parentView = inflater.inflate(R.layout.fragment_coverage, container, false);
        mListView = (ListView) parentView.findViewById(R.id.list_view);
        mMessage = (TextView) parentView.findViewById(R.id.tv_message);
        mLoadingIndicator = (ProgressBar) parentView.findViewById(R.id.pb_progress_loading_indicator);
        Bundle bundle = getArguments();
        if (bundle != null) {
            week = bundle.getString(KEY_WEEK);
            day = bundle.getString(KEY_DAY);
            weekDay = week + DELIMITER + day;
            getActivity().getSupportLoaderManager().initLoader(LOAD_RETAILERS_ID, null, this);
        } else {
            throw new RuntimeException("Unable to Create Fragment, pass DAY and Week");
        }
        return parentView;
    }

    private void bindView(Cursor cursor) {
        if (cursor.getCount() < 1) {
            mMessage.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.GONE);
        } else {
            String week = getThisWeek();
            String day = getCurrentDay();
            if ((week + DELIMITER + day).equals(weekDay)) {
                adapter = new DailyRetailersAdapter(getActivity(), cursor, this, retailerIds);
            }else{
                adapter = new DailyRetailersAdapter(getActivity(), cursor, this);
            }
            mLoadingIndicator.setVisibility(View.GONE);
            mMessage.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
        }
        setFonts(parentView);
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

    @Override
    public void onPause() {
        super.onPause();

    }

    public void filter(String query){
        if(query.equals(QUERY_ALL)){
            getActivity().getSupportLoaderManager().restartLoader(LOAD_RETAILERS_ID, null, this);
        }else {
            searchFilter = query;
            getActivity().getSupportLoaderManager().restartLoader(FILTER_RETAILERS_ID, null, this);
        }
    }

    private void launchStoreDetailsActivity(String retailerId) {
        Intent intent = new Intent(getActivity(), StoreOverviewActivity.class);
        intent.putExtra(StoreOverviewActivity.KEY_RETAILER_ID, retailerId);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity().getSupportLoaderManager().restartLoader(LOAD_RETAILERS_ID, null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                if (id == LOAD_RETAILERS_ID) {
                    Cursor productiveRetailers = DataUtils.getRetailerIdsForTodaysCoverage(
                            Days.getTodayDate(), Invoice.KEY_STATUS_SUCCESS, getActivity());
                    int count = productiveRetailers.getCount();
                    if (count > 0) {
                        productiveRetailers.moveToFirst();
                        for(int i = 0 ; i < count; i++){
                            productiveRetailers.moveToPosition(i);
                            retailerIds.add(productiveRetailers.getString(productiveRetailers.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID)));
                            productiveRetailers.moveToNext();
                        }
                    }
                    return DatabaseManager.getInstance(getActivity()).
                            getRetailerByVisitingInfo(week, day);
                }else if(id == FILTER_RETAILERS_ID){
                    Cursor productiveRetailers = DataUtils.getRetailerIdsForTodaysCoverage(
                            Days.getTodayDate(), Invoice.KEY_STATUS_SUCCESS, getActivity(), searchFilter);
                    int count = productiveRetailers.getCount();
                    if (count > 0) {
                        productiveRetailers.moveToFirst();
                        for(int i = 0 ; i < count; i++){
                            productiveRetailers.moveToPosition(i);
                            retailerIds.add(productiveRetailers.getString(productiveRetailers.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID)));
                            productiveRetailers.moveToNext();
                        }
                    }
                    return DatabaseManager.getInstance(getActivity()).
                            getRetailerByVisitingInfo(week, day, searchFilter);
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOAD_RETAILERS_ID || loader.getId() ==  FILTER_RETAILERS_ID) {
            bindView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
