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
import mydist.mydist.models.Invoice;
import mydist.mydist.models.ProductOrder;
import mydist.mydist.utils.FontManager;

/**
 * Created by Blessing.Ekundayo on 1/13/2018.
 */

public class SkuAdapter extends CursorAdapter {
    Context context;

    public SkuAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return inflater.inflate(R.layout.sku_report_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String productName = cursor.getString(cursor.getColumnIndex(MasterContract.ProductOrderContract.PRODUCT_NAME));
        int productCount = cursor.getInt(cursor.getColumnIndex(MasterContract.ProductOrderContract.PRODUCT_COUNT));
        TextView mProductName = (TextView) view.findViewById(R.id.product_name);
        TextView mProductCount = (TextView) view.findViewById(R.id.product_count);
        mProductName.setText(productName);
        mProductCount.setText(String.valueOf(productCount));

    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
