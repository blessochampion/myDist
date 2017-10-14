package mydist.mydist.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import mydist.mydist.R;
import mydist.mydist.fragments.DayReportFragment;
import mydist.mydist.fragments.InvoiceReportFragment;
import mydist.mydist.fragments.OrderReportFragment;
import mydist.mydist.fragments.SKUReportFragment;
import mydist.mydist.fragments.StockReportFragment;

public class ReportDetailsActivity extends AppCompatActivity
{
    public static final int KEY_DAY_REPORT =  0;
    public static final int KEY_ORDER_REPORT = 1;
    public static final int KEY_SKU_REPORT   =  2;
    public static final int KEY_STOCK_REPORT = 3;
    public static final int KEY_INVOICE_REPORT = 4;
    public static final String REPORT_KEY = "report";
    private static int DEFAULT_REPORT_KEY_VALUE = -1;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        int reportKey = getIntent().getIntExtra(REPORT_KEY, DEFAULT_REPORT_KEY_VALUE);
        if( reportKey == DEFAULT_REPORT_KEY_VALUE || reportKey < DEFAULT_REPORT_KEY_VALUE
                || reportKey > KEY_INVOICE_REPORT){
            Toast.makeText(this, getString(R.string.specify_report_key), Toast.LENGTH_LONG).show();
            finish();
        }else {
            displayReport(reportKey);
            setupToolBar();
        }
    }



    private void displayReport(int reportKey)
    {
        Fragment report = getReportWithKey(reportKey);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, report).commitAllowingStateLoss();

    }

    private Fragment getReportWithKey(int reportKey)
    {
        switch (reportKey){
            case KEY_DAY_REPORT:
                title = getString(R.string.day);
                return new DayReportFragment();
            case KEY_ORDER_REPORT:
                title = getString(R.string.order_p_order);
                return new OrderReportFragment();
            case KEY_SKU_REPORT:
                title = getString(R.string.sku_report);
                return new SKUReportFragment();
            case KEY_STOCK_REPORT:
                title = getString(R.string.stock_report);
                return new StockReportFragment();
            default:
                title = getString(R.string.invoice_report);
                return new InvoiceReportFragment();
        }
    }

    private void setupToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
