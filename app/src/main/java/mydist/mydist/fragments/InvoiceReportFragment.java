package mydist.mydist.fragments;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.adapters.InvoiceAdapter;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceReportFragment extends Fragment implements View.OnClickListener {
    InvoiceAdapter adapter;
    TextView invoiceMessage;
    ListView listView;
    View view;

    public InvoiceReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invoice_report, container, false);
        invoiceMessage = (TextView) view.findViewById(R.id.invoice_message);
        listView = (ListView) view.findViewById(R.id.list_view);
        bindView();
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    private void bindView() {
        Cursor cursor = DataUtils.getAllInvoice(null, Invoice.KEY_STATUS_SUCCESS, getActivity());
        if (cursor.getCount() > 0) {
            adapter = new InvoiceAdapter(getActivity(), cursor, this) {
                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    TextView value = (TextView) view.findViewById(R.id.value);
                    TextView invoice_Number = (TextView) view.findViewById(R.id.invoice_number);
                    String valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL));
                    value.setText(getString(R.string.naira) + String.format("%,.2f", Double.valueOf(valueStr)));
                    valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID));
                    invoice_Number.setText(valueStr);
                }

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {

                    return LayoutInflater.from(getActivity()).inflate(R.layout.invoice_item, parent, false);
                }
            };
            listView.setAdapter(adapter);
            invoiceMessage.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            invoiceMessage.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        setFonts(view);
    }

    @Override
    public void onClick(View v) {

    }
}
