package mydist.mydist.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.StockCount;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;

public class StockCountReviewActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_RETAILER_ID = "retailerId";
    public static final String KEY_STOCK_COUNT = "stock_count";
    ArrayList<StockCount> stockCounts;
    private TableLayout mTableLayout;
    private TableLayout mPagination;
    private static final int OC_POSITION = 1;
    String retailerId;
    private int currentPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_count_review);
        getReferencesToViews();
        setupToolbar();
        Bundle bundle = getIntent().getExtras();
        stockCounts = bundle.getParcelableArrayList(KEY_STOCK_COUNT);
        retailerId = bundle.getString(KEY_RETAILER_ID);
        initPagination();
        initHeader();
        loadProducts();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.stock_count));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initPagination() {
        mPagination.removeAllViews();
        TableRow pager = new TableRow(this);
        mPagination.setPadding(32, 0, 32, 0);
        int pageCount = (int) Math.ceil(stockCounts.size() / 10.0);
        loadPageToRow(pager, pageCount);
        mPagination.addView(pager);
    }

    private void loadPageToRow(TableRow pager, int pageCount) {
        for (int i = 0; i < pageCount; i++) {
            TextView page = new TextView(this);
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


    private void initHeader() {
        TableRow header = new TableRow(this);
        mTableLayout.setPadding(16, 0, 16, 0);
        header.setBackgroundColor(Color.WHITE);
        //product header
        TextView productHeader = getHeader(getResources().getString(R.string.product));
        header.addView(productHeader);
        //OC
        TextView ocHeader = getHeader(getResources().getString(R.string.oc));
        header.addView(ocHeader);
        mTableLayout.addView(header);
        mTableLayout.setColumnStretchable(0, true);
        mTableLayout.setColumnStretchable(1, true);
    }

    private TextView getHeader(String name) {
        TextView productHeader = new TextView(this);
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
    public void onClick(View v) {

    }

    private void fill(TextView page) {
        page.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        page.setTextColor(Color.WHITE);
    }

    private void unFill(TextView page) {
        page.setBackground(getResources().getDrawable(R.drawable.download_button));
        page.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void loadProducts() {
        TableRow productRow;
        int start = currentPage * 10;
        int end = start + 10;
        if (end > stockCounts.size()) {
            end = stockCounts.size();
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
            productRow = new TableRow(this);
            loadProductIntoRow(productRow, index);
            productRow.setLayoutParams(layoutParams);
            mTableLayout.addView(productRow);
        }
    }

    private void loadProductIntoRow(TableRow productRow, int position) {
        //product name
        StockCount product = stockCounts.get(position);
        productRow.setMinimumHeight(104);
        TextView productName = new TextView(this);
        productName.setText(product.getProductName());
        productName.setTextSize(17);
        productName.setTextColor(Color.parseColor("#212121"));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
        layoutParams.setMargins(0, 16, 0, 16);
        productName.setLayoutParams(layoutParams);
        productRow.addView(productName);
        layoutParams.gravity = CENTER_VERTICAL;
        //oc
        TextView ocTextView = new TextView(this);
        ocTextView.setLayoutParams(layoutParams);
        ocTextView.setMinHeight(80);
        ocTextView.setPadding(4, 0, 4, 0);
        ocTextView.setGravity(CENTER);
        ocTextView.setText(String.valueOf(product.getOc()));
        productRow.addView(ocTextView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getReferencesToViews() {
        mTableLayout = (TableLayout) findViewById(R.id.tl_products);
        mPagination = (TableLayout) findViewById(R.id.tl_pagination);
    }
}
