package mydist.mydist.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.ProductLogic;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.Invoice;
import mydist.mydist.models.ProductOrder;
import mydist.mydist.printing.PrintingActivity;
import mydist.mydist.printing.PrintingModel;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.DatabaseLogicUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static java.lang.String.valueOf;

public class InvoiceContentActivity extends AuthenticatedActivity {
    private static final String TAG = InvoiceContentActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 0;
    TableLayout mInvoiceTableLayout;
    HashMap<String, ProductLogic> selectedProducts;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_content);
        setupToolBar();
        context = this;
        mInvoiceTableLayout = (TableLayout) findViewById(R.id.tl_invoices);
        selectedProducts = DataUtils.getSelectedProducts();
        initProductsHeader();
        loadProducts();
        setFonts();
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.invoice_review);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadProducts() {
        TableRow productRow;
        ProductLogic currentProductLogic;
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        for (String key : selectedProducts.keySet()) {
            productRow = new TableRow(context);
            currentProductLogic = selectedProducts.get(key);
            loadProductIntoRow(productRow, currentProductLogic);
            productRow.setLayoutParams(layoutParams);
            mInvoiceTableLayout.addView(productRow);
        }
    }

    private void loadProductIntoRow(TableRow productRow, ProductLogic currentProductLogic) {
        productRow.setMinimumHeight(70);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        TextView productName = new TextView(context);
        productName.setText(currentProductLogic.getProduct().getProductName());
        productName.setTextSize(17);
        productName.setTextColor(Color.parseColor("#212121"));
        productName.setLayoutParams(layoutParams);

        productRow.addView(productName);

        TextView oc = new TextView(context);
        oc.setText(valueOf(currentProductLogic.oc));
        oc.setTextSize(17);
        oc.setGravity(CENTER_HORIZONTAL);
        oc.setTextColor(Color.parseColor("#212121"));
        oc.setLayoutParams(layoutParams);

        productRow.addView(oc);

        TextView op = new TextView(context);
        op.setText(valueOf(currentProductLogic.op));
        op.setTextSize(17);
        op.setGravity(CENTER_HORIZONTAL);
        op.setTextColor(Color.parseColor("#212121"));
        op.setLayoutParams(layoutParams);

        productRow.addView(op);
        TextView total = new TextView(context);
        total.setText(getString(R.string.naira) + String.format("%,.2f", currentProductLogic.getTotal()));
        total.setTextSize(17);
        total.setGravity(CENTER_HORIZONTAL);
        total.setTextColor(Color.parseColor("#212121"));
        total.setLayoutParams(layoutParams);

        productRow.addView(total);

    }

    private void initProductsHeader() {
        TableRow header = new TableRow(context);
        mInvoiceTableLayout.setPadding(16, 0, 16, 0);
        header.setBackgroundColor(Color.WHITE);

        //product header
        TextView productHeader = getHeader(getResources().getString(R.string.product));
        header.addView(productHeader);

        //OC
        TextView ocHeader = getHeader(getResources().getString(R.string.oc));
        header.addView(ocHeader);

        //OP
        TextView opHeader = getHeader(getResources().getString(R.string.op));
        header.addView(opHeader);

        //Total
        TextView totalHeader = getHeader(getResources().getString(R.string.total));
        header.addView(totalHeader);

        mInvoiceTableLayout.addView(header);

        mInvoiceTableLayout.setColumnStretchable(0, true);
        mInvoiceTableLayout.setColumnStretchable(1, true);
        mInvoiceTableLayout.setColumnStretchable(2, true);
        mInvoiceTableLayout.setColumnStretchable(3, true);

    }

    private TextView getHeader(String name) {
        TextView productHeader = new TextView(context);
        productHeader.setTextSize(14);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 70, 1);
        layoutParams.setMargins(0, 0, 6, 0);
        productHeader.setLayoutParams(layoutParams);
        //productHeader.setHeight(90);
        productHeader.setGravity(CENTER);
        productHeader.setMinWidth(200);
        productHeader.setPadding(16, 16, 16, 16);
        productHeader.setText(name);
        productHeader.setTextColor(Color.WHITE);
        productHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        return productHeader;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void gotoOverviewActivity() {
        Intent newIntent = new Intent(InvoiceContentActivity.this, StoreInfoDetailsActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
    }

    private String generateInvoiceNumber(String retailerId) {
        String salesRepFullName = UserPreference.getInstance(this).getFullName();
        String names[] = salesRepFullName.split(" ");
        String initials = names[0].charAt(0) + String.valueOf(names[1].charAt(0));
        String retailerInitials = "R" + initials;
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        SimpleDateFormat hourMinute = new SimpleDateFormat("HHmm");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(retailerInitials);
        stringBuilder.append("/");
        stringBuilder.append(dateFormat.format(new Date()));
        stringBuilder.append("/");
        stringBuilder.append(hourMinute.format(new Date()));
        stringBuilder.append("/");
        UserPreference userPreference = UserPreference.getInstance(this);
        int lastInvoiceIndex = userPreference.lastInvoiceIndex();

        String invoiceNumber = valueOf(++lastInvoiceIndex);
        if (invoiceNumber.length() == 1) {
            stringBuilder.append("00").append(invoiceNumber);
        } else if (invoiceNumber.length() == 2) {
            stringBuilder.append("0").append(invoiceNumber);
        } else {
            stringBuilder.append(invoiceNumber);
        }
        userPreference.setInvoiceLastIndex(lastInvoiceIndex);
        return stringBuilder.toString();
    }
}
