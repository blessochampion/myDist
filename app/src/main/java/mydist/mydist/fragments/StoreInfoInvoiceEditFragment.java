package mydist.mydist.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import mydist.mydist.R;
import mydist.mydist.activities.InvoiceContentActivity;
import mydist.mydist.activities.InvoiceEditActivity;
import mydist.mydist.adapters.InvoiceAdapter;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.ProductLogic;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.Product;
import mydist.mydist.printing.PrintingActivity;
import mydist.mydist.printing.PrintingModel;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.activities.StoreOverviewActivity.retailerId;


public class StoreInfoInvoiceEditFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    InvoiceAdapter adapter;
    ListView listView;
    TextView invoiceMessage;
    LinearLayout contentContainer;
    String invoiceId = "";
    View page;
    private static final int LOADER_ID_REPRINT = 1000;
    private static final int LOADER_ID_DETAILS = 999;
    public static final String KEY_RETAILER_ID = "retailer_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_info_invoice_cancel, container, false);
        invoiceMessage = (TextView) view.findViewById(R.id.invoice_message);
        contentContainer = (LinearLayout) view.findViewById(R.id.parent_layout);
        listView = (ListView) view.findViewById(R.id.list_view);
        page = view;
        bindView();
        return view;
    }

    private void bindView() {
        Cursor cursor = DataUtils.getAllInvoice(retailerId, -1, getActivity());
        if (cursor.getCount() > 0) {
            adapter = new InvoiceAdapter(getActivity(), cursor, this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            invoiceMessage.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);
        } else {
            invoiceMessage.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        }
        setFonts(page);
    }

    public static StoreInfoInvoiceEditFragment getNewInstance(String retailerId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RETAILER_ID, retailerId);
        StoreInfoInvoiceEditFragment storeInfoInvoiceEditFragment = new StoreInfoInvoiceEditFragment();
        storeInfoInvoiceEditFragment.setArguments(bundle);
        return storeInfoInvoiceEditFragment;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    @Override
    public void onClick(View v) {
        invoiceId = v.getTag().toString();
        if (v.getId() == R.id.icon_invoice_edit) {
        } else if (v.getId() == R.id.btn_reprint) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID_REPRINT, null, this);
        } else if (v.getId() == R.id.btn_details) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID_DETAILS, null, this);
        } else if (v.getId() == R.id.icon_invoice_delete) {
            AlertDialog mDialog = new AlertDialog.Builder(getActivity()).
                    setMessage(getString(R.string.inoice_delete_question)).
                    setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean result = DataUtils.cancelInvoice(invoiceId, getContext());
                                    if (result) {
                                        Toast.makeText(getActivity(), getString(R.string.invoice_delete_success), Toast.LENGTH_SHORT).show();
                                        bindView();
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.invoice_delete_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            mDialog.show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {
        return new CursorLoader(getActivity()) {

            @Override
            public Cursor loadInBackground() {
                switch (id) {
                    case LOADER_ID_REPRINT:
                        loadSavedInvoice();
                        break;
                    case LOADER_ID_DETAILS:
                        loadSavedInvoice();
                        break;
                    default:
                        return null;
                }
                return null;
            }

            private void loadSavedInvoice() {
                Cursor cursor = DataUtils.getProductsOrderByInvoiceId(invoiceId, getActivity());
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    String total = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TABLE_NAME + "." + MasterContract.InvoiceContract.TOTAL));
                    String retailerName = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String salesRep = UserPreference.getInstance(getActivity()).getFullName().split(" ")[0];
                    PrintingModel printingModel = new PrintingModel(retailerName, salesRep, invoiceId,
                            dateFormat.format(new Date()));
                    DataUtils.printingModel = printingModel;
                    HashMap<String, ProductLogic> productLogicHashMap = new HashMap<>();
                    ProductLogic productLogic;
                    Product product;
                    String productId;
                    for (int i = 0; i < count; i++) {
                        product = getProductFromCursor(cursor);
                        productId = cursor.getString(cursor.getColumnIndex(MasterContract.ProductOrderContract.TABLE_NAME + "." + MasterContract.ProductOrderContract.PRODUCT_ID));
                        productLogic = new ProductLogic(product);
                        productLogic.oc = cursor.getInt(cursor.getColumnIndex(MasterContract.ProductOrderContract.OC));
                        productLogic.op = cursor.getInt(cursor.getColumnIndex(MasterContract.ProductOrderContract.OP));
                        productLogicHashMap.put(productId, productLogic);
                        cursor.moveToNext();
                    }
                    DataUtils.setSelectedProducts(productLogicHashMap);
                    DataUtils.setTotalAmountToBePaid(Double.valueOf(total));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.unable_to_process_request), Toast.LENGTH_SHORT).show();
                }
            }

            private Product getProductFromCursor(Cursor cursor) {
                return new Product(
                        cursor.getString(cursor.getColumnIndex(MasterContract.ProductOrderContract.TABLE_NAME + "." + MasterContract.ProductOrderContract.PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndex(MasterContract.ProductOrderContract.TABLE_NAME + "." + MasterContract.ProductOrderContract.PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(MasterContract.ProductContract.COLUMN_CASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(MasterContract.ProductContract.COLUMN_PIECE_PRICE))
                );
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_ID_DETAILS) {
            Intent intent = new Intent(getActivity(), InvoiceContentActivity.class);
            startActivity(intent);
        } else if (loader.getId() == LOADER_ID_REPRINT) {
            Intent intent = new Intent(getActivity(), PrintingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
