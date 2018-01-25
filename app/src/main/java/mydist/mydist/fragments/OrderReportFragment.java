package mydist.mydist.fragments;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import mydist.mydist.R;
import mydist.mydist.adapters.InvoiceAdapter;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderReportFragment extends Fragment {
    InvoiceAdapter adapter;
    ListView listView;
    TextView orderMessage;
    TextView mSumTotal;
    View view;

    public OrderReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_report, container, false);
        mSumTotal = (TextView) view.findViewById(R.id.tv_sum_total);
        orderMessage = (TextView) view.findViewById(R.id.invoice_message);
        listView = (ListView) view.findViewById(R.id.list_view);
        bindView();
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    private void bindView() {
        Cursor cursor = DataUtils.getAllOrder(Invoice.KEY_STATUS_SUCCESS, getActivity());
        Cursor cursorTotal = DataUtils.getAllOrderTotal(Invoice.KEY_STATUS_SUCCESS, getActivity());
        if (cursorTotal.getCount() > 0) {
            cursorTotal.moveToFirst();
            float sum = cursorTotal.getFloat(cursorTotal.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
            mSumTotal.setText( getString(R.string.naira) + String.format("%,.2f", Double.valueOf(sum)));

        } else {
            mSumTotal.setText( getString(R.string.naira) + String.format("%,.2f", Double.valueOf("0.00")));
        }
        if (cursor.getCount() > 0) {
            adapter = new InvoiceAdapter(getActivity(), cursor, null) {
                @Override
                public void bindView(View view, Context context, Cursor cursor) {

                    TextView name = (TextView) view.findViewById(R.id.store_name);
                    TextView value = (TextView) view.findViewById(R.id.total_amount);
                    TextView invoice_Number = (TextView) view.findViewById(R.id.invoice_number);

                    String valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL));
                    value.setText(
                            getString(R.string.naira) + String.format("%,.2f", Double.valueOf(valueStr))
                    );
                    valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID));
                    invoice_Number.setText(valueStr);
                    valueStr = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
                    name.setText(valueStr);
                }

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {

                    return LayoutInflater.from(getActivity()).inflate(R.layout.p_order_item, parent, false);
                }
            };
            listView.setAdapter(adapter);
            orderMessage.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            orderMessage.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        setFonts(view);
    }

}
