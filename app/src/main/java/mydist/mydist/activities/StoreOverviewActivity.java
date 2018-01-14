package mydist.mydist.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.activities.StoreInfoDetailsActivity.*;


import static mydist.mydist.activities.StoreInfoDetailsActivity.KEY_STORE_INFO;

public class StoreOverviewActivity extends AuthenticatedActivity implements View.OnClickListener {
    public static final String KEY_RETAILER_ID = "retailerId";
    LinearLayout mReview;
    LinearLayout mInvoice;
    LinearLayout mCollection;
    LinearLayout mInvoiceCancel;
    LinearLayout mSBDMerchandising;
    LinearLayout mCallAnalysis;
    public static String retailerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_overview);
        setupToolBar();
        getReferencesToWidget();
        setOnClickListeners();
        setFonts();
        if (getIntent().hasExtra(KEY_RETAILER_ID)) {
            retailerId = getIntent().getStringExtra(KEY_RETAILER_ID);
        } else {
            finish();
        }


    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
        setIcons();
    }

    private void setIcons() {
        Typeface fontAwesome = FontManager.getTypeface(getApplicationContext(), FontManager.FONT_AWESOME);
        FontManager.setFontsForView(findViewById(R.id.icon_review), fontAwesome);
        FontManager.setFontsForView(findViewById(R.id.icon_invoice), fontAwesome);
        FontManager.setFontsForView(findViewById(R.id.icon_collection), fontAwesome);
        FontManager.setFontsForView(findViewById(R.id.icon_invoice_edit), fontAwesome);
        FontManager.setFontsForView(findViewById(R.id.icon_sbd_merchandising), fontAwesome);
        FontManager.setFontsForView(findViewById(R.id.icon_call_analysis), fontAwesome);
    }

    private void setOnClickListeners() {
        mReview.setOnClickListener(this);
        mInvoice.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mInvoiceCancel.setOnClickListener(this);
        mSBDMerchandising.setOnClickListener(this);
        mCallAnalysis.setOnClickListener(this);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.store);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getReferencesToWidget() {
        mReview = (LinearLayout) findViewById(R.id.ll_review);
        mInvoice = (LinearLayout) findViewById(R.id.ll_invoice);
        mCollection = (LinearLayout) findViewById(R.id.ll_collection);
        mInvoiceCancel = (LinearLayout) findViewById(R.id.ll_invoice_edit);
        mSBDMerchandising = (LinearLayout) findViewById(R.id.ll_sbd_merchandising);
        mCallAnalysis = (LinearLayout) findViewById(R.id.ll_call_analysis);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_review:
                launchStoreInfoDetails(KEY_REVIEW);
                break;
            case R.id.ll_invoice:
                launchStoreInfoDetails(KEY_INVOICE);
                break;
            case R.id.ll_collection:
                launchStoreInfoDetails(KEY_COLLECTION);
                break;
            case R.id.ll_invoice_edit:
                Cursor cursor = DataUtils.getAllInvoice(StoreOverviewActivity.retailerId, this);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    Log.e("merchantId", cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID)));
                    launchStoreInfoDetails(KEY_INVOICE_EDIT);
                }
                break;
            case R.id.ll_sbd_merchandising:
                launchStoreInfoDetails(KEY_SBD_MERCHANDISING);
                break;
            default:
                launchStoreInfoDetails(KEY_CALL_ANALYSIS);

        }
    }

    private void launchStoreInfoDetails(int storeKey) {
        Intent intent = new Intent(StoreOverviewActivity.this, StoreInfoDetailsActivity.class);
        intent.putExtra(KEY_STORE_INFO, storeKey);
        startActivity(intent);
        //overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }
}