package mydist.mydist.fragments;


import android.annotation.SuppressLint;
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
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.utils.Days.getCurrentDay;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView mTotalEarnedValue;
    TextView mDistributionExtension;
    TextView mSales;
    TextView mTotalCalls;
    TextView mProductiveCalls;
    TextView mMonthlyAchieved;

    private static final int LOADER_ID_TOTAL_EARNED_VALUE = 1;
    private static final int LOADER_ID_DISTRIBUTION_EXTENSION = 2;
    private static final int LOADER_ID_COVERAGE = 3;
    private static final int LOADER_ID_MONTHLY_ACHIEVED = 4;
    private static final String NO_INT_VALUE = "0";
    private static final double NO_DOUBLE_VALUE = 0.00;
    private static final String DEMARCATION = "/";
    private static int coverageCount = 0;

    public DayReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_report, container, false);
        mTotalEarnedValue = (TextView) view.findViewById(R.id.tv_total_earned_value);
        mDistributionExtension = (TextView) view.findViewById(R.id.tv_distribution_extension);
        mSales = (TextView) view.findViewById(R.id.tv_sales);
        mTotalCalls = (TextView) view.findViewById(R.id.tv_total_calls);
        mProductiveCalls = (TextView) view.findViewById(R.id.tv_productive_call);
        mMonthlyAchieved = (TextView)view.findViewById(R.id.month_achieved);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_TOTAL_EARNED_VALUE, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_DISTRIBUTION_EXTENSION, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_COVERAGE, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_MONTHLY_ACHIEVED, null, this);
        setFonts(view);
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    public void bindValueToView(int id, Cursor cursor) {
        String value;
        switch (id) {
            case LOADER_ID_TOTAL_EARNED_VALUE:
                float totalEarned = 0.00f;
                float collectionAmount = 0.00f;
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS))) {
                            totalEarned = cursor.
                                    getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                        }
                        if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID_ALIAS))) {
                            collectionAmount = cursor.
                                    getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID_ALIAS));
                        }
                        value = getString(R.string.naira) + String.format("%,.2f", totalEarned);
                        mTotalEarnedValue.setText(value);
                        value = getString(R.string.naira) + String.format("%,.2f", collectionAmount);
                        mSales.setText(value);
                    } else {
                        mTotalEarnedValue.setText(getString(R.string.naira) + String.format("%,.2f", NO_DOUBLE_VALUE));
                    }

                } catch (Exception e) {

                }
                break;

            case LOADER_ID_DISTRIBUTION_EXTENSION:
                if (cursor.getCount() > 0) {
                    value = String.valueOf(cursor.getCount());
                    mDistributionExtension.setText(value);
                } else {
                    mDistributionExtension.setText(NO_INT_VALUE);
                }
                break;

            case LOADER_ID_COVERAGE:
                value = String.valueOf(NO_INT_VALUE);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    value = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                }
                mTotalCalls.setText(String.valueOf(coverageCount));
                mProductiveCalls.setText(value + DEMARCATION + coverageCount);
                break;
            case LOADER_ID_MONTHLY_ACHIEVED:
                float monthlyAchieved = 0.00f;
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS))) {
                            monthlyAchieved = cursor.
                                    getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                        }
                        value = getString(R.string.naira) + String.format("%,.2f", monthlyAchieved);
                        mMonthlyAchieved.setText(value);

                    } else {
                        mMonthlyAchieved.setText(getString(R.string.naira) + String.format("%,.2f", NO_DOUBLE_VALUE));
                    }

                } catch (Exception e) {

                }
        }
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                switch (id) {
                    case LOADER_ID_TOTAL_EARNED_VALUE:
                        return DataUtils.getAllOrderTotal(Invoice.KEY_STATUS_SUCCESS, getActivity());
                    case LOADER_ID_DISTRIBUTION_EXTENSION:
                        return DatabaseManager.getInstance(getActivity()).getAllNewRetailers(Days.getRetailerDate());
                    case LOADER_ID_COVERAGE:
                        String week = Days.getThisWeek();
                        String day = getCurrentDay();
                        Cursor cursor = DatabaseManager.getInstance(getActivity()).
                                getRetailerByVisitingInfo(week, day);
                        coverageCount = cursor.getCount();
                        return DataUtils.getCoverageCount(Days.getTodayDate(), Invoice.KEY_STATUS_SUCCESS, getActivity());
                    case LOADER_ID_MONTHLY_ACHIEVED:
                        String[] firstDay = Days.getFirstDateOfTheMonth();
                        Log.e("ddd", firstDay[0]);
                        Log.e("ddd", firstDay[1]);
                        Log.e("ddd", firstDay[2]);
                       return DatabaseManager.getInstance(getActivity()).queryAchievedThisMonth(firstDay[0], firstDay[1], firstDay[2]);
                    default:
                        return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bindValueToView(loader.getId(), data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
