package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.utils.DatabaseLogicUtils;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

/**
 * Created by Blessing.Ekundayo on 1/9/2018.
 */

public class DailyRetailersAdapter extends CursorAdapter {

    Context context;
    View.OnClickListener listener;
    List<String> performingRetailerIds;
    HashMap<String, String> retailersMerchandizing;
    HashMap<String,String> pskuTargetMap;
    HashMap<String, String> todayPskuTargetMap = new HashMap<>();
    String merchandizingCount;

    public DailyRetailersAdapter(Context context, Cursor cursor, View.OnClickListener listener, HashMap<String, String> retailersMerchandizing,
                                 String merchandizingCount,  HashMap<String,String> pskuTargetMap, HashMap<String, String> todayPskuTargetMap ) {
        super(context, cursor, true);
        this.context = context;
        this.listener = listener;
        this.retailersMerchandizing = retailersMerchandizing;
        this.merchandizingCount = merchandizingCount;
        this.pskuTargetMap = pskuTargetMap;
        this.todayPskuTargetMap = todayPskuTargetMap;
    }

    public DailyRetailersAdapter(Context context, Cursor cursor, View.OnClickListener listener, List<String> ids, HashMap<String, String> retailersMerchandizing,
                                 String merchandizingCount,  HashMap<String,String> pskuTargetMap, HashMap<String, String> todayPskuTargetMap) {
        super(context, cursor, true);
        this.context = context;
        this.listener = listener;
        this.performingRetailerIds = ids;
        this.retailersMerchandizing = retailersMerchandizing;
        this.merchandizingCount = merchandizingCount;
        this.pskuTargetMap = pskuTargetMap;
        this.todayPskuTargetMap = todayPskuTargetMap;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.fragment_daily_retailer_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String retailerName = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_NAME));
        retailerName = UIUtils.capitalizefirstLetter(retailerName);
        String retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_ID));
        String hpvValue = cursor.getString(cursor.getColumnIndex(MasterContract.HighestPurchaseValueContract.VALUE));
        hpvValue = DatabaseLogicUtils.getHighestPurchase(hpvValue);
        String storeTargetValue = DatabaseLogicUtils.getHighestPurchaseEver(hpvValue);
        TextView retailerNameTv = (TextView) view.findViewById(R.id.tv_retailer_name);
        TextView hvp = (TextView) view.findViewById(R.id.tv_hvp_value);
        TextView storeTarget = (TextView) view.findViewById(R.id.stgt_value);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        TextView tvMerchandising = (TextView) view.findViewById(R.id.tv_merch_value);
        TextView tvDist = (TextView) view.findViewById(R.id.tv_dst_value);
        tvInfo.setTag(FontManager.IMMUTABLE_TYPFACE_USED);
        Typeface fontAwesome = FontManager.getTypeface(context, FontManager.FONT_AWESOME);
        tvInfo.setTypeface(fontAwesome);
        hvp.setText(context.getString(R.string.naira) + String.format("%,.2f", Double.valueOf(hpvValue)));
        storeTarget.setText(context.getText(R.string.naira) + String.format("%,.2f", Double.valueOf(storeTargetValue)));
        tvInfo.setTag(retailerId);
        tvInfo.setOnClickListener(listener);
        retailerNameTv.setText(retailerName);
        retailerNameTv.setTextColor(context.getResources().getColor(R.color.crispy_green));
        if (performingRetailerIds != null && performingRetailerIds.contains(retailerId)) {
            retailerNameTv.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
        if(retailersMerchandizing.containsKey(retailerId)) {
            tvMerchandising.setText(context.getString(R.string.slashFormat,
                    retailersMerchandizing.get(retailerId), merchandizingCount));
        }else {
            tvMerchandising.setText(context.getString(R.string.slashFormat, "0", merchandizingCount));
        }
        if(pskuTargetMap.containsKey(retailerId)){
            if(todayPskuTargetMap.containsKey(retailerId)){
                tvDist.setText(context.getString(R.string.slashFormat, todayPskuTargetMap.get(retailerId), pskuTargetMap.get(retailerId)));
            }else {
                tvDist.setText(context.getString(R.string.slashFormat, "0", pskuTargetMap.get(retailerId)));
            }
        }else{
            if(todayPskuTargetMap.containsKey(retailerId)){
                tvDist.setText(context.getString(R.string.slashFormat, todayPskuTargetMap.get(retailerId),
                        todayPskuTargetMap.get(retailerId)));
            }else {
                tvDist.setText(context.getString(R.string.slashFormat, "0", "0"));
            }

        }
    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }
}
