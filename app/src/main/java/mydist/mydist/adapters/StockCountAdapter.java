package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;

/**
 * Created by Blessing.Ekundayo on 03/03/2018.
 */

public class StockCountAdapter extends CursorAdapter {
    Context context;

    public StockCountAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.stock_count_item, parent, false);
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
