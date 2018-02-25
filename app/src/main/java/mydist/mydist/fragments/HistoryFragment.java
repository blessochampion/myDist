package mydist.mydist.fragments;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.Toast;

import mydist.mydist.R;
import mydist.mydist.adapters.HistoryAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String KEY_RETAILER_ID = "id";
    public static final int LOAD_RETAILER_HISTORY = 123;
    ListView mListView;
    TextView mMessage;
    ProgressBar mLoadingIndicator;
    HistoryAdapter adapter;
    View parentView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mMessage = (TextView) view.findViewById(R.id.tv_message);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_progress_loading_indicator);
        parentView = view;
        Bundle bundle = getArguments();
        if (bundle != null) {
            getActivity().getSupportLoaderManager().initLoader(LOAD_RETAILER_HISTORY, bundle, this);
        } else {
            throw new RuntimeException("Unable to Create Fragment, pass DAY and Week");
        }

        return view;
    }

    private void bindView(Cursor cursor) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (cursor.getCount() < 1) {
            mMessage.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mMessage.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            adapter = new HistoryAdapter(getActivity(), cursor);
            mListView.setAdapter(adapter);
        }
        setFonts(parentView);
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    public static HistoryFragment getNewInstance(String retailerId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RETAILER_ID, retailerId);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                if (id == LOAD_RETAILER_HISTORY) {
                    return DatabaseManager.
                            getInstance(getActivity()).queryAllInvoiceByRetailerId(args.getString(KEY_RETAILER_ID));
                }
                return super.loadInBackground();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOAD_RETAILER_HISTORY) {
            bindView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
