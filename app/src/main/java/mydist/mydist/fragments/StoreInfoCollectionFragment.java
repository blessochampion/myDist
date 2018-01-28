package mydist.mydist.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.InvoiceAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.Invoice;
import mydist.mydist.printing.CollectionModel;
import mydist.mydist.printing.PrintingActivity;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.models.Invoice.MODE_CASH;
import static mydist.mydist.models.Invoice.MODE_CHEQUE;
import static mydist.mydist.models.Invoice.MODE_DRAFT;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoCollectionFragment extends Fragment implements View.OnClickListener {
    InvoiceAdapter adapter;
    private RadioButton mCash;
    private RadioButton mCheque;
    private RadioButton mDraft;
    private EditText mNumber;
    private EditText mAmount;
    private String mode = MODE_CASH;
    private static final String EMPTY = "";
    private String chequeOrDraftNumber = EMPTY;
    private static final String SEPARATOR = ":";
    private LinearLayout mAmountInputContainer;
    private Button mSave;
    String retailerId;
    ListView mListView;
    private TextView mMessage;
    private String invoiceNumber;
    private HashMap<String, CollectionModel> collectionModelHashMap = new HashMap<>();
    private int selectedPosition;
    View page;
    Cursor cursor;
    private double invoiceAmount;

    public StoreInfoCollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retailerId = StoreOverviewActivity.retailerId;
        View view = inflater.inflate(R.layout.fragment_store_info_collection, container, false);
        page = view;
        getReferencesToViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getRetailerName(String retailerId) {
        Cursor cursor = DatabaseManager.getInstance(getActivity()).getRetailerById(retailerId);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
    }


    private void saveAndPrint() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String retailerName = getRetailerName(retailerId);
        String amount = mAmount.getText().toString();
        CollectionModel model = new CollectionModel(invoiceNumber, dateFormat
                .format(new Date()), String.format("%,.2f", Double.valueOf(amount)),
                retailerName, UserPreference.getInstance(getActivity()).getFullName(), invoiceAmount);
        if (amount.equals(EMPTY)) {
            mAmount.setError("Field Cannot be empty");
            return;
        }
        if (mode.equalsIgnoreCase(MODE_CHEQUE)) {
            chequeOrDraftNumber = mNumber.getText().toString().trim();
            if (chequeOrDraftNumber.equalsIgnoreCase(EMPTY)) {
                mNumber.setError("Field can not be empty");
                return;
            } else {
                model.setChequeNumber(chequeOrDraftNumber);
            }
        } else if (mode.equalsIgnoreCase(MODE_DRAFT)) {
            chequeOrDraftNumber = mNumber.getText().toString().trim();
            if (chequeOrDraftNumber.equalsIgnoreCase(EMPTY)) {
                mNumber.setError("Field can not be empty");
                return;
            } else {
                model.setDraftNumber(chequeOrDraftNumber);
            }
        }

        boolean result = DataUtils.saveCollectionForInvoice(getActivity(), invoiceNumber, amount, mode, chequeOrDraftNumber);
        if (result) {
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            bindView();
        } else {
            Toast.makeText(getActivity(), "Unable to save", Toast.LENGTH_SHORT).show();
        }
        mAmountInputContainer.setVisibility(View.GONE);
        collectionModelHashMap.put(retailerId, model);

    }

    private void getReferencesToViews(View view) {
        mCash = (RadioButton) view.findViewById(R.id.cash);
        mCheque = (RadioButton) view.findViewById(R.id.cheque);
        mDraft = (RadioButton) view.findViewById(R.id.draft);
        mCash.setOnClickListener(this);
        mCheque.setOnClickListener(this);
        mDraft.setOnClickListener(this);
        mNumber = (EditText) view.findViewById(R.id.number);
        mAmountInputContainer = (LinearLayout) view.findViewById(R.id.amount_input_container);
        mSave = (Button) view.findViewById(R.id.btn_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndPrint();
            }
        });

        mListView = (ListView) view.findViewById(R.id.list_view);
        mMessage = (TextView) view.findViewById(R.id.tv_message);
        bindView();
        mAmount = (EditText) view.findViewById(R.id.collection_amount);
        setFonts(page);
    }


    private void bindView() {
        cursor = DataUtils.getAllInvoice(retailerId, Invoice.KEY_STATUS_SUCCESS, getActivity());
        if (cursor.getCount() > 0) {
            adapter = new InvoiceAdapter(getActivity(), cursor, this) {
                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    TextView value = (TextView) view.findViewById(R.id.invoice_value);
                    TextView invoice_Number = (TextView) view.findViewById(R.id.invoice_number);
                    Button payButton = (Button) view.findViewById(R.id.btn_pay);
                    Button printButton = (Button) view.findViewById(R.id.btn_print);
                    String amount = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL));
                    value.setText(
                            getString(R.string.naira) + String.format("%,.2f", Double.valueOf(amount))
                    );
                   String valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID));
                    invoice_Number.setText(valueStr);
                    printButton.setTag(valueStr+SEPARATOR + cursor.getPosition()+ SEPARATOR + amount);
                    payButton.setTag(valueStr + SEPARATOR + cursor.getPosition() + SEPARATOR + amount);
                    payButton.setOnClickListener(StoreInfoCollectionFragment.this);
                    printButton.setOnClickListener(StoreInfoCollectionFragment.this);
                    TextView amountValue = (TextView) view.findViewById(R.id.amount_value);
                    valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID));
                    amountValue.setText(getString(R.string.naira) + String.format("%,.2f", Double.valueOf(valueStr)));
                }

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {

                    return LayoutInflater.from(getActivity()).inflate(R.layout.collection_item, parent, false);
                }
            };
            mListView.setAdapter(adapter);
            mMessage.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mMessage.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        setFonts(page);
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


    private void hideEditText() {
        mNumber.setVisibility(View.INVISIBLE);
        mode = MODE_CASH;
    }

    private void showDraftEditText() {
        mNumber.setVisibility(View.VISIBLE);
        mNumber.setHint(getString(R.string.draft_number));
        mode = MODE_DRAFT;
    }

    private void showChequeEditText() {
        mNumber.setVisibility(View.VISIBLE);
        mNumber.setHint(getString(R.string.cheque_number));
        mode = MODE_CHEQUE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cash:
                hideEditText();
                break;
            case R.id.cheque:
                showChequeEditText();
                break;
            case R.id.draft:
                showDraftEditText();
                break;
            case R.id.btn_pay:
                mAmountInputContainer.setVisibility(View.VISIBLE);
                String[] tags = v.getTag().toString().split(SEPARATOR);
                invoiceNumber = tags[0];
                selectedPosition = Integer.valueOf(tags[1]);
                invoiceAmount = Double.valueOf(tags[2]);
                break;
            case R.id.btn_print:
                String[] printTags = v.getTag().toString().split(SEPARATOR);
                invoiceNumber = printTags[0];
                selectedPosition = Integer.valueOf(printTags[1]);
                invoiceAmount = Double.valueOf(printTags[2]);
                launchPrinting();
            default:
                break;
        }
    }

    private void launchPrinting() {
        CollectionModel model;
        if (collectionModelHashMap.isEmpty()) {
            cursor.moveToPosition(selectedPosition);
            String amount = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID));
            String retailerName = getRetailerName(retailerId);
            model = new CollectionModel(invoiceNumber, Days.getTodayDate().toString()
                    , String.format("%,.2f", Double.valueOf(amount)),
                    retailerName, UserPreference.getInstance(getActivity()).getFullName(), Double.valueOf(invoiceAmount));
            String modeOfPayment = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.PAYMENT_MODE));
            String modeOfPaymentValue = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.PAYMENT_MODE_VALUE));
            if(modeOfPayment.equals(Invoice.MODE_CHEQUE)){
                model.setChequeNumber(modeOfPaymentValue);
            }else if(modeOfPayment.equals(Invoice.MODE_DRAFT)){
                model.setDraftNumber(modeOfPaymentValue);
            }
        } else {
            model = collectionModelHashMap.get(retailerId);
        }
        Intent intent = new Intent(getActivity(), PrintingActivity.class);
        intent.putExtra(PrintingActivity.KEY_COLLECTION, model);
        startActivity(intent);

    }
}
