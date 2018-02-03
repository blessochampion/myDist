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
 * Created by Blessing.Ekundayo on 1/13/2018.
 */

public class InvoiceAdapter extends CursorAdapter {
    Context context;
    View.OnClickListener listener;

    public InvoiceAdapter(Context context, Cursor c, View.OnClickListener listener) {
        super(context, c, true);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.invoice_adapter_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String orderId = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID));
        TextView tv_Order_Id = (TextView) view.findViewById(R.id.invoice_id);
        tv_Order_Id.setText(orderId);
        TextView deleteIcon = (TextView) view.findViewById(R.id.icon_invoice_delete);
        TextView message = (TextView) view.findViewById(R.id.invoice_cancelled);
        Button reprint = (Button) view.findViewById(R.id.btn_reprint);
        reprint.setOnClickListener(listener);
        reprint.setTag(orderId);
        int status = cursor.getInt(cursor.getColumnIndex(MasterContract.InvoiceContract.STATUS));
        if (status == Invoice.KEY_STATUS_SUCCESS) {
            deleteIcon.setOnClickListener(listener);
            deleteIcon.setTag(FontManager.IMMUTABLE_TYPFACE_USED);
            deleteIcon.setTag(orderId);
            message.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.VISIBLE);
        } else {
            deleteIcon.setOnClickListener(null);
            deleteIcon.setTag(null);
            deleteIcon.setText(R.string.cancelled);
            deleteIcon.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            reprint.setVisibility(View.GONE);
        }
        Typeface fontAwesome = FontManager.getTypeface(context, FontManager.FONT_AWESOME);
        deleteIcon.setTypeface(fontAwesome);
    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
