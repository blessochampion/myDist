package mydist.mydist.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static java.lang.String.valueOf;

public class InvoiceActivity extends AuthenticatedActivity implements View.OnClickListener {
    TableLayout mInvoiceTableLayout;
    HashMap<String, ProductLogic> selectedProducts;
    double totalAmountTobePaid;
    Context context;
    TextView mTotalAmount;
    Button mEdit;
    Button mSave;
    Button mSaveAndPrint;
    boolean invoiceSaved = false;
    String invoiceNumber;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                onBackPressed();
                break;
            case R.id.save:
                if (!invoiceSaved) {
                    invoiceSaved = saveInvoice(
                            invoiceNumber == null ?
                                    generateInvoiceNumber(StoreOverviewActivity.retailerId)
                                    : invoiceNumber, StoreOverviewActivity.retailerId);
                } else {
                    makeToast(getString(R.string.invoice_saved));
                }
                break;
            case R.id.save_and_print:
                saveAndPrint();
                break;
            default:
                break;
        }
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveAndPrint() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String salesRep = UserPreference.getInstance(this).getFullName().split(" ")[0];
        String retailerId = StoreOverviewActivity.retailerId;
        String retailerName = getRetailerName(retailerId);

        if (!invoiceSaved) {
            invoiceNumber = invoiceNumber == null ? generateInvoiceNumber(retailerId) : invoiceNumber;
            invoiceSaved = saveInvoice(invoiceNumber, retailerId);

        }

        PrintingModel printingModel = new PrintingModel(retailerName, salesRep,
                invoiceNumber, dateFormat.format(new Date()));
        DataUtils.setPrintingModel(printingModel);

        Intent intent = new Intent(context, PrintingActivity.class);
        startActivity(intent);
    }

    private boolean saveInvoice(String invoiceId, String retailerId) {
        List<ProductOrder> productOrders = getProductOrders(Days.getTodayDate(), invoiceId);
        Invoice invoice = new Invoice(invoiceId, retailerId, Days.getTodayDate()
                , String.valueOf(DataUtils.getTotalAmountToBePaid()), productOrders);

        boolean success = DataUtils.saveInvoice(invoice, this);
        if (!success) {
            UserPreference userPreference = UserPreference.getInstance(this);
            userPreference.setInvoiceLastIndex(userPreference.lastInvoiceIndex() - 1);
            makeToast(getString(R.string.invoice_not_saved));

        } else {
            mSaveAndPrint.setText(getString(R.string.print));
            makeToast(getString(R.string.invoice_saved));
        }

        return success;
    }

    private String generateInvoiceNumber(String retailerId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        StringBuilder stringBuilder = new StringBuilder("INV");
        stringBuilder.append("/");
        stringBuilder.append(retailerId);
        stringBuilder.append("/");
        stringBuilder.append(dateFormat.format(new Date()));
        stringBuilder.append("/");
        UserPreference userPreference = UserPreference.getInstance(this);
        int lastInvoiceIndex = userPreference.lastInvoiceIndex();

        String invoiceNumber = valueOf(lastInvoiceIndex);
        if (invoiceNumber.length() == 1) {
            stringBuilder.append("00").append(invoiceNumber);
        } else if (invoiceNumber.length() == 2) {
            stringBuilder.append("0").append(invoiceNumber);
        } else {
            stringBuilder.append(invoiceNumber);
        }

        lastInvoiceIndex++;
        userPreference.setInvoiceLastIndex(lastInvoiceIndex);
        return stringBuilder.toString();
    }

    private String getRetailerName(String retailerId) {
        Cursor cursor = DatabaseManager.getInstance(this).getRetailerById(retailerId);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
    }

    public List<ProductOrder> getProductOrders(String dateAdded, String invoiceId) {
        List<ProductOrder> productOrders = new ArrayList<>();
        HashMap<String, ProductLogic> products = DataUtils.getSelectedProducts();
        ProductLogic productLogic;
        ProductOrder productOrder;
        for (String productKey : products.keySet()) {
            productLogic = products.get(productKey);
            productOrder = new ProductOrder(
                    dateAdded,
                    invoiceId,
                    String.format("%.2f",productLogic.getTotal()),
                    productLogic.getProduct().productName,
                    productLogic.getProduct().productId,
                    productLogic.getProduct().getBrandId(),
                    productLogic.oc,
                    productLogic.op
            );
            productOrders.add(productOrder);
        }

        return productOrders;
    }
}
