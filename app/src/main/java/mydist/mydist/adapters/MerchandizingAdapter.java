package mydist.mydist.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Merchandize;

/**
 * Created by Blessing.Ekundayo on 11/26/2017.
 */

public class MerchandizingAdapter extends ArrayAdapter<Merchandize>
{

    public MerchandizingAdapter(@NonNull Context context, List<Merchandize> merchandizes) {
        super(context, 0, merchandizes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchandizing_item, parent, false);
        }
        TextView brandName = (TextView) convertView.findViewById(R.id.brand_name);
        TextView item = (TextView) convertView.findViewById(R.id.merch_item);
        Merchandize merchandize = getItem(position);
        brandName.setText(merchandize.getBrandName());
        item.setText(merchandize.getMerchandizeItem());

        return convertView;
    }
}
