package mydist.mydist.fragments;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DayReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView mTotalEarnedValue;
    TextView mDistributionExtension;
    TextView mSales;


    private static final int LOADER_ID_TOTAL_EARNED_VALUE = 1;
    private static final int LOADER_ID_DISTRIBUTION_EXTENSION = 2;
    private static final int LOADER_ID_SALES = 3;
    private static final String NO_INT_VALUE = "0";
    private static final double NO_DOUBLE_VALUE = 0.00;

    public DayReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_report, container, false);
        mTotalEarnedValue = (TextView) view.findViewById(R.id.tv_total_earned_value);
        mDistributionExtension = (TextView) view.findViewById(R.id.tv_distribution_extension);
        mSales = (TextView) view.findViewById(R.id.tv_sales);
       // getActivity().getSupportLoaderManager().initLoader(LOADER_ID_TOTAL_EARNED_VALUE, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_DISTRIBUTION_EXTENSION, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_SALES, null, this);
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
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    value = getString(R.string.naira) + String.format("%,.2f", cursor.
                            getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS)));
                    mTotalEarnedValue.setText(value);
                } else {
                    mTotalEarnedValue.setText(getString(R.string.naira)+ String.format("%,.2f",NO_DOUBLE_VALUE));
                }
                break;
            case LOADER_ID_DISTRIBUTION_EXTENSION:
                if(cursor.getCount() > 0){
                    value = String.valueOf(cursor.getCount());
                    mDistributionExtension.setText(value);
                }else {
                    mDistributionExtension.setText(NO_INT_VALUE);
                }
                break;
            case LOADER_ID_SALES:
                if(cursor.getCount()>0){
                    cursor.moveToFirst();
                    value = getString(R.string.naira) + String.format("%,.2f", cursor.
                            getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS)));
                    mSales.setText(value);
                }else {
                    mSales.setText((getString(R.string.naira)+ String.format("%,.2f",NO_DOUBLE_VALUE)));
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
                        DatabaseManager.getInstance(getActivity()).queryCollectionTotal(Days.getTodayDate(), Invoice.KEY_STATUS_SUCCESS );
                    case LOADER_ID_DISTRIBUTION_EXTENSION:
                        return DatabaseManager.getInstance(getActivity()).getAllNewRetailers(Days.getTodayDate());
                    case LOADER_ID_SALES:
                        return DataUtils.getAllOrderTotal(Invoice.KEY_STATUS_SUCCESS, getActivity());
                    default:
                        return  null;
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
