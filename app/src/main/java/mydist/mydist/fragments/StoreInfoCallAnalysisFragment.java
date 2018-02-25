package mydist.mydist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import mydist.mydist.R;
import mydist.mydist.activities.StoreInfoDetailsActivity;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.models.MerchandizingVerification;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;


public class StoreInfoCallAnalysisFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID_SALES_VALUE = 1;
    private static final int LOADER_ID_DISTRIBUTION_VALUE = 1000;
    private static final int LOADER_ID_MERCHANDIZING_VALUE = 2000;
    private static final double NO_DOUBLE_VALUE = 0.00;
    private TextView mSales;
    private TextView mCollectionAmount;
    private TextView mMerchandizing;
    private String todaySKUTarget = "0", skuTarget = "0";
    private String todayMerch = "0", merch = "0";
    TextView mDistributionRate;
    View mParentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_store_info_call_analysis, container, false);
        mSales = (TextView) mParentView.findViewById(R.id.tv_sales_value);
        mCollectionAmount = (TextView) mParentView.findViewById(R.id.tv_collection_amount_value);
        mDistributionRate = (TextView) mParentView.findViewById(R.id.tv_sbd_distribution_rate_value);
        mMerchandizing = (TextView) mParentView.findViewById(R.id.tv_initiative_merch_rate_value);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_SALES_VALUE, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_DISTRIBUTION_VALUE, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_MERCHANDIZING_VALUE, null, this);
        setFonts(mParentView);
        return mParentView;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                switch (id) {
                    case LOADER_ID_SALES_VALUE:
                        return DataUtils.getAllOrderTotal(StoreOverviewActivity.retailerId, Invoice.KEY_STATUS_SUCCESS, getActivity());
                    case LOADER_ID_DISTRIBUTION_VALUE:
                        getPSKUMap(null, StoreOverviewActivity.retailerId);
                        getPSKUMap(Days.getTodayDate(), StoreOverviewActivity.retailerId);
                        return null;
                    case LOADER_ID_MERCHANDIZING_VALUE:
                        getMerchandizingVerificationMap(Days.getTodayDate(), StoreOverviewActivity.retailerId);
                        return null;
                }

                return null;
            }

            private void getMerchandizingVerificationMap(String date, String retailerId) {
                String result = "0";
                Cursor cursor = DatabaseManager.getInstance(getActivity()).getMerchandisingVerificationGroupByRetailerId(date,
                        MerchandizingVerification.STATUS_AVAILABLE, retailerId);
                merch =  DatabaseManager.getInstance(getActivity()).getMerchandizingCount(date);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    todayMerch = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.COUNT));
                }
            }

            private void getPSKUMap(String date, String retailerId) {
                String result = "0";
                Cursor cursor = DatabaseManager.getInstance(getActivity()).getDistributionRate(date, retailerId);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    result = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                }
                if (date != null) {
                    todaySKUTarget = result;
                } else {
                    skuTarget = result;
                }
            }
        };
    }

    public void bindValueToView(int id, Cursor cursor) {
        String value;
        switch (id) {
            case LOADER_ID_SALES_VALUE:
                float totalEarned = 0.00f;
                float amountCollected = 0.00f;
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS))) {
                            totalEarned = cursor.
                                    getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                        }
                        if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID_ALIAS))) {
                            amountCollected = cursor.
                                    getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID_ALIAS));
                        }
                        value = getString(R.string.naira) + String.format("%,.2f", totalEarned);
                        mSales.setText(value);
                        value = getString(R.string.naira) + String.format("%,.2f", amountCollected);
                        mCollectionAmount.setText(value);
                    } else {
                        mSales.setText(getString(R.string.naira) + String.format("%,.2f", NO_DOUBLE_VALUE));
                        mCollectionAmount.setText(getString(R.string.naira) + String.format("%,.2f", NO_DOUBLE_VALUE));
                    }

                } catch (Exception e) {

                }
                break;
            case LOADER_ID_DISTRIBUTION_VALUE:
                mDistributionRate.setText(getString(R.string.slashFormat, todaySKUTarget, skuTarget));
                break;
            case LOADER_ID_MERCHANDIZING_VALUE:
                mMerchandizing.setText(getString(R.string.slashFormat, todayMerch, merch));
                break;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bindValueToView(loader.getId(), data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
