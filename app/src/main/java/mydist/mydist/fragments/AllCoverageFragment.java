package mydist.mydist.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import java.util.HashMap;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.DailyRetailersAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.MerchandizingVerification;
import mydist.mydist.models.Retailer;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.fragments.CoverageFragment.QUERY_ALL;
import static mydist.mydist.utils.Days.getCurrentDay;
import static mydist.mydist.utils.Days.getDay;
import static mydist.mydist.utils.Days.getThisWeek;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCoverageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        StoreDeviateDialogFragment.RetailerDateChangeListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    DailyRetailersAdapter adapter;
    Cursor cursor;
    View view;
    ListView listView;
    TextView message;
    private static String searchFilter;
    private ProgressBar mLoadingIndicator;
    private static final int LOAD_RETAILERS_ID = 20001;
    private static final int FILTER_RETAILERS_ID = 30001;
    private static final int CHANGE_REQUESTED_RETAILERS_ID = 40001;
    private static final String KEY_ID = "id";
    String filter = null;
    String merchandizingCount;
    HashMap<String, String> retailersMerchandizing = new HashMap<>();
    HashMap<String,String> pskuTargetMap = new HashMap<>();
    HashMap<String, String> todayPskuTargetMap = new HashMap<>();

    public AllCoverageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coverage, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        message = (TextView) view.findViewById(R.id.tv_message);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_progress_loading_indicator);
        getActivity().getSupportLoaderManager().restartLoader(LOAD_RETAILERS_ID, null, this);
        return view;
    }

    private void bindView(Cursor cursor) {

        if (cursor.getCount() < 1) {
            message.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.GONE);
        } else {
            adapter = new DailyRetailersAdapter(getActivity(), cursor, this, retailersMerchandizing, merchandizingCount,
                    pskuTargetMap, todayPskuTargetMap);
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
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        getActivity().getSupportLoaderManager().restartLoader(CHANGE_REQUESTED_RETAILERS_ID, bundle, this);
    }

    public void filter(String query) {
        if (query.equals(QUERY_ALL)) {
            getActivity().getSupportLoaderManager().restartLoader(LOAD_RETAILERS_ID, null, this);
        } else {
            searchFilter = query;
            getActivity().getSupportLoaderManager().restartLoader(FILTER_RETAILERS_ID, null, this);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                String week = getThisWeek();
                String day = getCurrentDay();
                Cursor todaysRetailerscursor = DatabaseManager.getInstance(getActivity()).
                        getRetailerByVisitingInfo(week, day);

                if (todaysRetailerscursor.getCount() > 0) {
                    filter = getRetailers(todaysRetailerscursor);
                }
                getMerchandizingVerificationMap();
                getPSKUMap(null);
                getPSKUMap(Days.getTodayDate());
                merchandizingCount = DatabaseManager.getInstance(getActivity()).getMerchandizingCount(Days.getTodayDate());
                if (id == LOAD_RETAILERS_ID) {
                    return DatabaseManager.getInstance(getActivity()).getAllRetailerExceptTheCurrentDate(filter);
                } else if (id == FILTER_RETAILERS_ID) {
                    return DatabaseManager.getInstance(getActivity()).getAllRetailerExceptTheCurrentDate(filter, searchFilter);
                } else if (id == CHANGE_REQUESTED_RETAILERS_ID) {
                    if (DatabaseManager.getInstance(getActivity()).changeRetailerVisitingDate(args.getString(KEY_ID), Days.getTodayDate(), day, week)) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), getString(R.string.date_changed), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    todaysRetailerscursor = DatabaseManager.getInstance(getActivity()).
                            getRetailerByVisitingInfo(week, day);
                    filter = getRetailers(todaysRetailerscursor);
                    return DatabaseManager.getInstance(getActivity()).getAllRetailerExceptTheCurrentDate(filter);
                }
                return null;
            }
            private void getMerchandizingVerificationMap() {
                Cursor cursor = DatabaseManager.getInstance(getActivity()).getMerchandisingVerificationGroupByRetailerId(Days.getTodayDate(),
                        MerchandizingVerification.STATUS_AVAILABLE);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        retailersMerchandizing.put(cursor.getString(
                                cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.RETAILER_ID)),
                                cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.COUNT)));
                        cursor.moveToNext();
                    }
                }
            }
            private void getPSKUMap(String date) {
                HashMap<String , String> container;
                if(date!= null){
                    container = todayPskuTargetMap;
                }else {
                    container = pskuTargetMap;
                }
                Cursor cursor = DatabaseManager.getInstance(getActivity()).getDistributionRate(null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        container.put(cursor.getString(
                                cursor.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID)), cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS)));
                        cursor.moveToNext();
                    }
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOAD_RETAILERS_ID || loader.getId() == CHANGE_REQUESTED_RETAILERS_ID || loader.getId() == FILTER_RETAILERS_ID) {
            bindView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
