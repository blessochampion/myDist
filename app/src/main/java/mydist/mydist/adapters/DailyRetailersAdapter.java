package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.utils.DatabaseLogicUtils;
import mydist.mydist.utils.FontManager;

/**
 * Created by Blessing.Ekundayo on 1/9/2018.
 */

public class DailyRetailersAdapter extends CursorAdapter {

    Context context;
    View.OnClickListener listener;

    public DailyRetailersAdapter(Context context, Cursor cursor, View.OnClickListener listener) {
        super(context, cursor, true);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return inflater.inflate(R.layout.fragment_daily_retailer_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String retailerName = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
        String retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_ID));
        String hpvValue = cursor.getString(cursor.getColumnIndex(MasterContract.HighestPurchaseValueContract.VALUE));
        hpvValue = DatabaseLogicUtils.getHighestPurchase(hpvValue);
        String storeTargetValue = DatabaseLogicUtils.getHighestPurchaseEver(hpvValue);
        TextView retailerNameTv = (TextView) view.findViewById(R.id.tv_retailer_name);
        TextView hvp = (TextView) view.findViewById(R.id.tv_hvp_value);
        TextView storeTarget = (TextView) view.findViewById(R.id.stgt_value);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        tvInfo.setTag(FontManager.IMMUTABLE_TYPFACE_USED);
        Typeface fontAwesome = FontManager.getTypeface(context, FontManager.FONT_AWESOME);
        tvInfo.setTypeface(fontAwesome);
        hvp.setText(context.getString(R.string.naira) + String.format("%,.2f", Double.valueOf(hpvValue)));
        storeTarget.setText(context.getText(R.string.naira) + String.format("%,.2f", Double.valueOf(storeTargetValue)));
        tvInfo.setTag(retailerId);
        tvInfo.setOnClickListener(listener);
        retailerNameTv.setText(retailerName);
    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
