package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.models.Retailer;

/**
 * Created by Blessing.Ekundayo on 1/9/2018.
 */

public class DailyRetailersAdapter extends CursorAdapter {

    Context context;

    public DailyRetailersAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return inflater.inflate(R.layout.fragment_daily_retailer_item, parent, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String retailerName = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
        TextView retailerNameTv = (TextView) view.findViewById(R.id.tv_retailer_name);

        retailerNameTv.setText(retailerName);
    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
