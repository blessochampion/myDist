package mydist.mydist.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import mydist.mydist.R;
import mydist.mydist.fragments.StoreInfoCallAnalysisFragment;
import mydist.mydist.fragments.StoreInfoCollectionFragment;
import mydist.mydist.fragments.StoreInfoInvoiceCancelFragment;
import mydist.mydist.fragments.StoreInfoInvoiceFragment;
import mydist.mydist.fragments.StoreInfoMerchandisingFragment;
import mydist.mydist.fragments.StoreInfoReviewFragment;

public class StoreInfoDetailsActivity extends AppCompatActivity {
    public static final int KEY_REVIEW =  0;
    public static final int KEY_INVOICE = 1;
    public static final int KEY_COLLECTION   =  2;
    public static final int KEY_INVOICE_CANCEL = 3;
    public static final int KEY_SBD_MERCHANDISING = 4;
    public static final int KEY_CALL_ANALYSIS = 5;
    public static final String KEY_STORE_INFO = "storeinfo";
    private static int DEFAULT_REPORT_KEY_VALUE = -1;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info_details);
        int storeKey = getIntent().getIntExtra(KEY_STORE_INFO, DEFAULT_REPORT_KEY_VALUE);
        if (storeKey == DEFAULT_REPORT_KEY_VALUE || storeKey < DEFAULT_REPORT_KEY_VALUE
                || storeKey > KEY_CALL_ANALYSIS) {
            finish();
        } else {
            displayStoreDetails(storeKey);
            setupToolBar();
        }
    }

    private void displayStoreDetails(int storeKey)
    {
        Fragment report = getStoreWithKey(storeKey);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, report).commitAllowingStateLoss();

    }

    private Fragment getStoreWithKey(int storeKey)
    {
        switch (storeKey)
        {
            case KEY_REVIEW :
                title = getString(R.string.review);
                return new StoreInfoReviewFragment();
            case KEY_INVOICE:
                title = getString(R.string.invoice);
                return new StoreInfoInvoiceFragment();
            case KEY_COLLECTION:
                title  = getString(R.string.collection);
                return new StoreInfoCollectionFragment();
            case KEY_INVOICE_CANCEL:
                title = getString(R.string.invoice_cancel);
                return new StoreInfoInvoiceCancelFragment();
            case KEY_SBD_MERCHANDISING:
                title = getString(R.string.sbd_merchandising);
                return new StoreInfoMerchandisingFragment();
            default:
                title = getString(R.string.call_analysis);
                return new StoreInfoCallAnalysisFragment();

        }
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
