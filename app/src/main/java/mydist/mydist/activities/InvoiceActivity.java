package mydist.mydist.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import mydist.mydist.R;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.ProductLogic;
import mydist.mydist.data.UserPreference;
import mydist.mydist.printing.PrintingActivity;
import mydist.mydist.printing.PrintingModel;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;

public class InvoiceActivity extends AuthenticatedActivity implements View.OnClickListener {
    TableLayout mInvoiceTableLayout;
    HashMap<String, ProductLogic> selectedProducts;
    double totalAmountTobePaid;
    Context context;
    TextView mTotalAmount;
    Button mEdit;
    Button mSave;
    Button mSaveAndPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        setupToolBar();
        context = this;
        mTotalAmount = (TextView) findViewById(R.id.total_amount);
        mInvoiceTableLayout = (TableLayout) findViewById(R.id.tl_invoices);
        mEdit = (Button) findViewById(R.id.edit);
        mEdit.setOnClickListener(this);
        mSave = (Button) findViewById(R.id.save);
        mSave.setOnClickListener(this);
        mSaveAndPrint = (Button) findViewById(R.id.save_and_print);
        mSaveAndPrint.setOnClickListener(this);

        selectedProducts = DataUtils.getSelectedProducts();
        totalAmountTobePaid = DataUtils.getTotalAmountToBePaid();
        mTotalAmount.setText(String.format("%,.2f", totalAmountTobePaid));
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
        oc.setText(String.valueOf(currentProductLogic.oc));
        oc.setTextSize(17);
        oc.setGravity(CENTER_HORIZONTAL);
        oc.setTextColor(Color.parseColor("#212121"));
        oc.setLayoutParams(layoutParams);

        productRow.addView(oc);

        TextView op = new TextView(context);
        op.setText(String.valueOf(currentProductLogic.op));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                onBackPressed();
                break;
            case R.id.save:
                break;
            case R.id.save_and_print:
                launchPrintActivity();
                break;
            default:
                break;
        }
    }

    private void launchPrintActivity() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String salesRep = UserPreference.getInstance(this).getFullName().split(" ")[0];
        Cursor cursor = DatabaseManager.getInstance(this).getRetailerById(StoreOverviewActivity.retailerId);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
        PrintingModel printingModel = new PrintingModel(name, salesRep,
                "INV/SS001/100118/001", dateFormat.format(new Date()));
        DataUtils.setPrintingModel(printingModel);
        Intent intent = new Intent(context, PrintingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }
}
