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
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;


public class StoreInfoCallAnalysisFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ID_SALES_VALUE = 1;
    private static final int LOADER_ID_COLLECTION_VALUE = 2;
    private static final double NO_DOUBLE_VALUE = 0.00;
    private TextView mSales;
    private TextView mCollectionAmount;
    View mParentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView = inflater.inflate(R.layout.fragment_store_info_call_analysis, container, false);
        mSales = (TextView) mParentView.findViewById(R.id.tv_sales_value);
        mCollectionAmount = (TextView) mParentView.findViewById(R.id.tv_collection_amount_value);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_SALES_VALUE, null, this);
        //().getSupportLoaderManager().initLoader(LOADER_ID_COLLECTION_VALUE, null, this);
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
                    case  LOADER_ID_COLLECTION_VALUE:
                        return DataUtils.getAllOrderTotal(Invoice.KEY_STATUS_SUCCESS, getActivity());
                }

                return null;
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

            case LOADER_ID_COLLECTION_VALUE:
                float totalCollection = 0.00f;
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS))) {
                            totalCollection = cursor.
                                    getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                        }
                        value = getString(R.string.naira) + String.format("%,.2f", totalCollection);
                        mCollectionAmount.setText(value);
                    } else {
                        mCollectionAmount.setText(getString(R.string.naira) + String.format("%,.2f", NO_DOUBLE_VALUE));
                    }

                } catch (Exception e) {

                }
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
