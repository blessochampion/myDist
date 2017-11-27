package mydist.mydist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Brand;
import mydist.mydist.models.Merchandize;

/**
 * Created by Blessing.Ekundayo on 11/26/2017.
 */

public class FilterAdapter extends ArrayAdapter<Brand> {

    public FilterAdapter(@NonNull Context context, List<Brand> brands) {
        super(context, 0, brands);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filter_item, parent, false);
        }
        Brand brand = getItem(position);
        TextView textView = (TextView)convertView.findViewById(R.id.tv_name);
        textView.setText(brand.getBrandName());
        return convertView;
    }
}
