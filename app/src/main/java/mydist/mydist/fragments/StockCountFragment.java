package mydist.mydist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.LoginActivity;
import mydist.mydist.activities.StockCountReviewActivity;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.StockCount;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;
import static mydist.mydist.activities.LoginActivity.EMPTY_STRING;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockCountFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, StoreFilterDialogFragment.FilterItemListener {
    private static final String KEY_RETAILER_ID = "retailerId";
    private static final int LOADER_ID = 4849494;
    private static final int FILTER_LOADER_ID = 784;
    private TableLayout mTableLayout;
    private TableLayout mPagination;
    private View containerView;
    private boolean isInFilterMode = false;
    private static final int OC_POSITION = 2;
    private static final String DELIMITER = ":";
    public static final String FILTER_ALL = "ALL";
    private static String filter;
    Cursor stockCountCursor;
    Cursor productsCursor;
    Cursor filteredProductsCursor;
    String retailerId;
    HashMap<String, StockCount> savedStockCountMap = new HashMap<>();
    Context context;
    int currentPage = 0;

    public StockCountFragment() {
        // Required empty public constructor
    }

    private void initPagination() {
        mPagination.removeAllViews();
        TableRow pager = new TableRow(context);
        mPagination.setPadding(32, 0, 32, 0);
        int pageCount = (int) Math.ceil(getCursor().getCount() / 10.0);
        loadPageToRow(pager, pageCount);
        mPagination.addView(pager);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.forward) {
            ArrayList<StockCount> stockCounts = getSelectedStockCounts();
            if (stockCounts.size() > 0) {
                Intent intent = new Intent(getActivity(), StockCountReviewActivity.class);
                intent.putParcelableArrayListExtra(StockCountReviewActivity.KEY_STOCK_COUNT, stockCounts);
                intent.putExtra(StockCountReviewActivity.KEY_RETAILER_ID, retailerId);
                startActivity(intent);
            } else {
                AlertDialog mDialog = new AlertDialog.Builder(context).
                        setMessage(context.getString(R.string.select_at_least_one_product)).
                        setPositiveButton(context.getString(R.string.ok), null).create();
                mDialog.show();
            }
            return true;
        }else if (item.getItemId() == R.id.filter) {
            StoreFilterDialogFragment storeFilterDialogFragment = new StoreFilterDialogFragment();
            storeFilterDialogFragment.setListener(this);
            storeFilterDialogFragment.show(getActivity().getSupportFragmentManager(), "");
            return true;
        }else if (item.getItemId() == android.R.id.home && isInFilterMode) {
            refreshProductsDisplayed();
            return true;
        }else if(item.getItemId() == android.R.id.home ){
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_skucount, container, false);
        mTableLayout = (TableLayout) containerView.findViewById(R.id.tl_products);
        mPagination = (TableLayout) containerView.findViewById(R.id.tl_pagination);
        retailerId = getArguments().getString(KEY_RETAILER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        context = getActivity();
        return containerView;
    }

    public static StockCountFragment getNewInstance(String retailerId) {
        StockCountFragment fragment = new StockCountFragment();
        Bundle fragmentArgument = new Bundle();
        fragmentArgument.putString(KEY_RETAILER_ID, retailerId);
        fragment.setArguments(fragmentArgument);
        return fragment;
    }

    private void cacheStockToMap() {
        int count = stockCountCursor.getCount();
        if (count > 0) {
            stockCountCursor.moveToFirst();
            while (count > 0) {
                savedStockCountMap.put
                        (getString(MasterContract.StockCountContract.PRODUCT_ID),
                                new StockCount(getString(MasterContract.StockCountContract.PRODUCT_ID),
                                        getString(MasterContract.ProductContract.COLUMN_NAME),
                                        stockCountCursor.getInt(stockCountCursor.getColumnIndex(MasterContract.StockCountContract.OC))));
                stockCountCursor.moveToNext();
                count--;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {

        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                DatabaseManager manager = DatabaseManager.getInstance(getActivity());
                if (id == LOADER_ID) {
                    stockCountCursor = manager.getStockCount(retailerId, Days.getTodayDate());
                    return manager.queryAllProduct();
                }else if(id == FILTER_LOADER_ID){
                    return manager.queryAllProduct(filter);
                }
               return null;
            }
        };
    }

    private void loadPageToRow(TableRow pager, int pageCount) {
        for (int i = 0; i < pageCount; i++) {
            TextView page = new TextView(context);
            page.setTextSize(12);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 45, 1);
            layoutParams.setMargins(0, 0, 15, 0);
            page.setLayoutParams(layoutParams);
            page.setGravity(CENTER);
            page.setMinWidth(70);
            page.setTextColor(Color.WHITE);
            if (i > 0) {
                unFill(page);
            } else {
                fill(page);
            }
            page.setText(i + 1 + "");
            pager.addView(page);
            page.setTag(i);
            page.setOnClickListener(this);
        }
    }

    private void fill(TextView page) {
        page.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        page.setTextColor(Color.WHITE);
    }

    private void unFill(TextView page) {
        page.setBackground(getResources().getDrawable(R.drawable.download_button));
        page.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_ID) {
            cacheStockToMap();
            productsCursor = data;
            bindView();
        }else if(loader.getId() == FILTER_LOADER_ID){
            cacheStockToMap();
            filteredProductsCursor = data;
            bindView();
        }
    }

    private void bindView() {
        initPagination();
        initHeader();
        loadProducts();
        setFonts();
    }
    private Cursor getCursor(){
        if(isInFilterMode){
            return filteredProductsCursor;
        }
        return productsCursor;
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getActivity(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(containerView.findViewById(R.id.parent_layout), ralewayFont);
    }

    private void loadProducts() {
        TableRow productRow;
        int start = currentPage * 10;
        int end = start + 10;
        if (end > getCursor().getCount()) {
            end = getCursor().getCount();
        }
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        int index = 0;
        int childCount = mTableLayout.getChildCount();
        if (childCount > 1) {
            mTableLayout.removeViews(1, childCount - 1);
        }
        for (; start < end; start++, index++) {
            productRow = new TableRow(context);
            getCursor().moveToPosition(start);
            loadProductIntoRow(productRow, index);
            productRow.setLayoutParams(layoutParams);
            mTableLayout.addView(productRow);
        }
    }

    private void loadProductIntoRow(TableRow productRow, int position) {
        //product name
        String productId = getProductString(MasterContract.StockCountContract.PRODUCT_ID);
        String productNameStr = getProductString(MasterContract.ProductContract.COLUMN_NAME);
        productRow.setMinimumHeight(104);
        TextView productName = new TextView(context);
        productName.setText(getProductString(MasterContract.ProductContract.COLUMN_NAME));
        productName.setTextSize(17);
        productName.setTextColor(Color.parseColor("#212121"));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
        layoutParams.setMargins(0, 16, 0, 16);
        productName.setLayoutParams(layoutParams);
        productRow.addView(productName);
        CheckBox selectCheckbox = generateCheckbox(context);
        selectCheckbox.setMinHeight(80);
        selectCheckbox.setTag(productId + ":" + (position + 1) + ":" + productNameStr);
        selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClicked((CheckBox) v);
            }
        });

        productRow.addView(selectCheckbox);

        layoutParams.gravity = CENTER_VERTICAL;
        //oc
        EditText ocEditText = new EditText(context);
        ocEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ocEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        ocEditText.setLayoutParams(layoutParams);
        ocEditText.setMinHeight(80);
        ocEditText.setPadding(4, 0, 4, 0);
        ocEditText.setGravity(CENTER);
        ocEditText.setEnabled(false);
        productRow.addView(ocEditText);
        if (savedStockCountMap.containsKey(productId)) {
            selectCheckbox.setChecked(true);
            ocEditText.setEnabled(true);
            ocEditText.setText(String.valueOf(savedStockCountMap.get(productId).getOc()));
        }
    }

    private String getProductString(String name) {
        return getCursor().getString(productsCursor.getColumnIndex(name));
    }

    private void checkBoxClicked(CheckBox checkBox) {
        String[] values = ((String) checkBox.getTag()).split(DELIMITER);
        final String productId = values[0];
        int position = Integer.valueOf(values[1]);
        final String productName = values[2];
        TableRow selectedRow = (TableRow) mTableLayout.getChildAt(position);
        if (checkBox.isChecked()) {
            View view = selectedRow.getChildAt(OC_POSITION);
            view.setEnabled(true);
            final EditText editText = ((EditText) view);
            if (savedStockCountMap.containsKey(productId)) {
                editText.setEnabled(true);
                editText.setText(String.valueOf(savedStockCountMap.get(productId)));
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int length = s.toString().length();
                    if (length >= 1) {
                        char lastChar = s.toString().charAt(length - 1);
                        if (!('0' <= lastChar && lastChar <= '9')) {
                            editText.setText(s.toString().substring(0, count - 1));
                        } else {
                            savedStockCountMap.put(productId,
                                    new StockCount(productId, productName, Integer.valueOf(s.toString())));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty() && savedStockCountMap.containsKey(productId)) {
                        savedStockCountMap.remove(productId);
                    }
                }
            });
        } else {
            View view = selectedRow.getChildAt(OC_POSITION);
            ((EditText) view).setText(EMPTY_STRING);
            view.setEnabled(false);
        }
    }

    private CheckBox generateCheckbox(Context context) {
        CheckBox checkbox = new CheckBox(context);
        TableRow.LayoutParams lpCheckbox = new TableRow.LayoutParams(
                toDp(56), TableRow.LayoutParams.MATCH_PARENT);
        lpCheckbox.setMargins(6, 0, 6, 0);
        checkbox.setLayoutParams(lpCheckbox);
        lpCheckbox.gravity = CENTER;
        checkbox.setTextColor(Color.parseColor("#212121"));
        return checkbox;
    }

    private int toDp(int pixel) {
        float scale = getResources().getDisplayMetrics().density;
        int pixelsAsDp = (int) (pixel * scale + 0.5f);
        return pixel;
    }

    private String getString(String columnName) {
        return stockCountCursor.getString(stockCountCursor.getColumnIndex(columnName));
    }

    private void initHeader() {
        TableRow header = new TableRow(context);
        mTableLayout.setPadding(16, 0, 16, 0);
        header.setBackgroundColor(Color.WHITE);
        //product header
        TextView productHeader = getHeader(getResources().getString(R.string.product));
        header.addView(productHeader);
        //Select
        TextView selectHeader = getHeader(getResources().getString(R.string.select));
        header.addView(selectHeader);
        //OC
        TextView ocHeader = getHeader(getResources().getString(R.string.oc));
        header.addView(ocHeader);
        mTableLayout.addView(header);
        mTableLayout.setColumnStretchable(0, true);
        mTableLayout.setColumnStretchable(1, true);
        mTableLayout.setColumnStretchable(2, true);
    }

    private TextView getHeader(String name) {
        TextView productHeader = new TextView(context);
        productHeader.setTextSize(14);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 70, 1);
        layoutParams.setMargins(0, 0, 6, 0);
        productHeader.setLayoutParams(layoutParams);
        productHeader.setGravity(CENTER);
        productHeader.setMinWidth(150);
        productHeader.setPadding(16, 16, 16, 16);
        productHeader.setText(name);
        productHeader.setTextColor(Color.WHITE);
        productHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        return productHeader;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();
        TextView oldChild = (TextView) ((TableRow) mPagination.getChildAt(0)).getChildAt(currentPage);
        unFill(oldChild);
        TextView newChild = (TextView) ((TableRow) mPagination.getChildAt(0)).getChildAt(index);
        fill(newChild);
        currentPage = index;
        loadProducts();
        setFonts();
    }

    public ArrayList<StockCount> getSelectedStockCounts() {
        ArrayList<StockCount> result = new ArrayList();
        for (String key : savedStockCountMap.keySet()) {
            result.add(savedStockCountMap.get(key));
        }
        return result;
    }

    @Override
    public void onFilterItemListener(String brandId) {
        if (brandId.equalsIgnoreCase(FILTER_ALL)) {
            refreshProductsDisplayed();
        } else {
            filter = String.valueOf((int) Double.parseDouble(brandId));
            currentPage = 0;
            isInFilterMode = true;
            getActivity().getSupportLoaderManager().restartLoader(FILTER_LOADER_ID, null, this);
        }
    }

    private void refreshProductsDisplayed() {
        isInFilterMode = false;
        currentPage = 0;
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
