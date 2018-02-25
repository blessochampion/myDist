package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Invoice;
import mydist.mydist.utils.FontManager;

/**
 * Created by Blessing.Ekundayo on 2/24/2018.
 */

public class HistoryAdapter extends CursorAdapter {
    Context context;

    public HistoryAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.history_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String orderId = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID));
        TextView tvOrderId = (TextView) view.findViewById(R.id.tv_invoice_id);
        tvOrderId.setText(orderId);
        TextView tvDateAdded = (TextView) view.findViewById(R.id.tv_date);
        String dateAdded = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.DATE_ADDED));
        tvDateAdded.setText(dateAdded);
        TextView tvTotalLabel = (TextView) view.findViewById(R.id.tv_total_label);
        TextView tvTotal = (TextView) view.findViewById(R.id.tv_total_value);
        String total = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL));
        tvTotal.setText(context.getString(R.string.naira) + String.format("%,.2f", Double.valueOf(total)));
        TextView tvCollectionAmountLabel = (TextView) view.findViewById(R.id.tv_collection_amount_label);
        TextView tvCollectionAmount = (TextView) view.findViewById(R.id.tv_collection_amount_value);
        TextView tvCancelled = (TextView) view.findViewById(R.id.tv_cancelled);
        String collectionAmount = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID));
        tvCollectionAmount.setText(context.getString(R.string.naira) + String.format("%,.2f", Double.valueOf(collectionAmount)));
        int status = cursor.getInt(cursor.getColumnIndex(MasterContract.InvoiceContract.STATUS));
        if (status == Invoice.KEY_STATUS_SUCCESS) {
            tvTotalLabel.setVisibility(View.VISIBLE);
            tvTotal.setVisibility(View.VISIBLE);
            tvCollectionAmountLabel.setVisibility(View.VISIBLE);
            tvCollectionAmount.setVisibility(View.VISIBLE);
            tvCancelled.setVisibility(View.GONE);

        } else {
            tvTotalLabel.setVisibility(View.GONE);
            tvTotal.setVisibility(View.GONE);
            tvCollectionAmountLabel.setVisibility(View.GONE);
            tvCollectionAmount.setVisibility(View.GONE);
            tvCancelled.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
