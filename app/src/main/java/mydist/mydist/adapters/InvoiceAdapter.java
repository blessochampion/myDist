package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
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

        return inflater.inflate(R.layout.fragment_edit_invoice, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String orderId = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.INVOICE_ID));
        TextView tv_Order_Id = (TextView) view.findViewById(R.id.invoice_id);
        tv_Order_Id.setText(orderId);

        TextView editIcon = (TextView) view.findViewById(R.id.icon_invoice_edit);
        TextView deleteIcon = (TextView) view.findViewById(R.id.icon_invoice_delete);
        editIcon.setTag(FontManager.IMMUTABLE_TYPFACE_USED);
        deleteIcon.setTag(FontManager.IMMUTABLE_TYPFACE_USED);
        Typeface fontAwesome = FontManager.getTypeface(context, FontManager.FONT_AWESOME);
        editIcon.setTypeface(fontAwesome);
        deleteIcon.setTypeface(fontAwesome);
        editIcon.setOnClickListener(listener);
        deleteIcon.setOnClickListener(listener);
        editIcon.setTag(orderId);
        deleteIcon.setTag(orderId);

    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
