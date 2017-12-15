package mydist.mydist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;

/**
 * Created by Blessing.Ekundayo on 11/26/2017.
 */

public class BrandSpinnerAdapter extends ArrayAdapter<Brand> {
    List<Brand> brands;
    public BrandSpinnerAdapter(@NonNull Context context, List<Brand> brands) {
        super(context, R.layout.spinner_item, R.id.name);
        this.brands = brands;
    }

    @Override
    public int getCount() {
        return brands.size();
    }


    @Nullable
    @Override
    public Brand getItem(int position) {
        return brands.get(position);
    }
}
