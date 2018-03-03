package mydist.mydist.fragments;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.security.PublicKey;

import mydist.mydist.R;
import mydist.mydist.adapters.StockCountAdapter;
import mydist.mydist.data.DatabaseManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockCountFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    TextView mMessage;
    ListView mSKUList;
    LinearLayout container;
    View divider;
    View contentView;
    private static final String KEY_RETAILER_ID = "retailer_id";
    private static final int LOADER_ID = 109398;
    String retailerId;

    public StockCountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_stockcount, container, false);
        getReferencesToWidgets();
        retailerId = getArguments().getString(KEY_RETAILER_ID);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return contentView;
    }

    public void getReferencesToWidgets() {
        mMessage = (TextView) contentView.findViewById(R.id.tv_message);
        mSKUList = (ListView) contentView.findViewById(R.id.lv_sku_list);
        container = (LinearLayout) contentView.findViewById(R.id.container);
        divider = (View) contentView.findViewById(R.id.divider);
    }

    void bindView(Cursor cursor) {
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                container.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                mMessage.setVisibility(View.VISIBLE);
            } else {
                contentView.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
                mMessage.setVisibility(View.GONE);
                mSKUList.setAdapter(new StockCountAdapter(getActivity(), cursor));
            }
        }
    }

    public static StockCountFragment getNewInstance(String retailerId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RETAILER_ID, retailerId);
        StockCountFragment stockCountFragment = new StockCountFragment();
        stockCountFragment.setArguments(bundle);
        return stockCountFragment;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                return DatabaseManager.getInstance(getActivity()).getStockCount(retailerId);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_ID) {
            bindView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
