package mydist.mydist.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import mydist.mydist.R;
import mydist.mydist.data.UserPreference;
import mydist.mydist.printing.CollectionModel;
import mydist.mydist.printing.PrintingActivity;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoCollectionFragment extends Fragment implements View.OnClickListener {

    private RadioButton mCash;
    private RadioButton mCheque;
    private RadioButton mDraft;
    private EditText mNumber;
    private TextView mInvoiceNumber;
    private TextView mTotal;
    private String mode = MODE_CASH;
    private static final String MODE_CASH = "CASH";
    private static final String MODE_CHEQUE = "CHEQUE";
    private static final String MODE_DRAFT = "DRAFT";
    private static final String EMPTY = "";
    private String chequeOrDraftNumber = EMPTY;
    /*Todo: Fix hard coded value*/
    private double total = 34099.345;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info_collection, container, false);
        getReferencesToViews(view);
        setHasOptionsMenu(true);
        setFonts(view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveAndPrint();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndPrint() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        /*Todo: Remove hardcoded name*/
        CollectionModel model = new CollectionModel(mInvoiceNumber.getText().toString(), dateFormat
                .format(new Date()), String.format("%,.2f", total),
                "Blessing", UserPreference.getInstance(getActivity()).getFullName());
        if (mode.equalsIgnoreCase(MODE_CHEQUE)) {
            chequeOrDraftNumber = mNumber.getText().toString().trim();
            if (chequeOrDraftNumber.equalsIgnoreCase(EMPTY)) {
                mNumber.setError("Field can not be empty");
                return;
            }else {
                model.setChequeNumber(chequeOrDraftNumber);
            }
        } else if (mode.equalsIgnoreCase(MODE_DRAFT)) {
            chequeOrDraftNumber = mNumber.getText().toString().trim();
            if (chequeOrDraftNumber.equalsIgnoreCase(EMPTY)) {
                mNumber.setError("Field can not be empty");
                return;
            }else {
                model.setDraftNumber(chequeOrDraftNumber);
            }
        }
        Intent intent = new Intent(getActivity(), PrintingActivity.class);
        intent.putExtra(PrintingActivity.KEY_COLLECTION, model);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);

    }

    private void getReferencesToViews(View view) {
        mCash = (RadioButton) view.findViewById(R.id.cash);
        mCheque = (RadioButton) view.findViewById(R.id.cheque);
        mDraft = (RadioButton) view.findViewById(R.id.draft);
        mInvoiceNumber = (TextView) view.findViewById(R.id.invoiceNo);
        mTotal = (TextView) view.findViewById(R.id.total);
        /*Todo: fix hard coding*/
        mTotal.setText(String.format("%,.2f", total));
        mCash.setOnClickListener(this);
        mCheque.setOnClickListener(this);
        mDraft.setOnClickListener(this);
        mNumber = (EditText) view.findViewById(R.id.number);
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
            default:
                break;
        }
    }
}
