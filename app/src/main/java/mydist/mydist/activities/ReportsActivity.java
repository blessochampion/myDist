package mydist.mydist.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import mydist.mydist.R;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.activities.ReportDetailsActivity.*;

public class ReportsActivity extends AuthenticatedActivity implements View.OnClickListener {
    LinearLayout dayReport;
    LinearLayout orderReport;
    LinearLayout skuReport;
    LinearLayout invoiceReport;
    LinearLayout close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getReferencesToViews();
        setOnClickListeners();
        setupToolBar();
        setFonts();
    }

    private void setOnClickListeners()
    {
        dayReport.setOnClickListener(this);
        orderReport.setOnClickListener(this);
        skuReport.setOnClickListener(this);
        invoiceReport.setOnClickListener(this);
        close.setOnClickListener(this);

    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
        setIcons();
    }

    private void setIcons() {
        Typeface fontAwesome = FontManager.getTypeface(getApplicationContext(), FontManager.FONT_AWESOME);
        FontManager.setFontsForView(findViewById(R.id.icon_day), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_order_p_order), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_sku_report), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_invoice_report), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_invoice_close), fontAwesome );
    }

    public void getReferencesToViews() {
        dayReport = (LinearLayout) findViewById(R.id.ll_day);
        orderReport = (LinearLayout) findViewById(R.id.ll_order);
        skuReport = (LinearLayout) findViewById(R.id.ll_sku);
        invoiceReport = (LinearLayout) findViewById(R.id.ll_invoice);
        close = (LinearLayout) findViewById(R.id.ll_close);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.reports);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_day:
                launchReportDetails(KEY_DAY_REPORT);
                break;
            case R.id.ll_order:
                launchReportDetails(KEY_ORDER_REPORT);
                break;
            case R.id.ll_sku:
                launchReportDetails(KEY_SKU_REPORT);
                break;

            case R.id.ll_invoice:
                launchReportDetails(KEY_INVOICE_REPORT);
                break;
            case R.id.ll_close:
                onBackPressed();
                break;
            default:
                return;
        }
    }

    private void launchReportDetails(int reportKey) {
        Intent intent = new Intent(ReportsActivity.this, ReportDetailsActivity.class);
        intent.putExtra(REPORT_KEY, reportKey);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }
}
