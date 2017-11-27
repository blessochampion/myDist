package mydist.mydist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Channel;

/**
 * Created by Blessing.Ekundayo on 11/26/2017.
 */

public class DeviateSpinnerAdapter extends ArrayAdapter<String> {
    String[] deviations;
    public DeviateSpinnerAdapter(@NonNull Context context, String[] deviations) {
        super(context, R.layout.spinner_item, R.id.name);
        this.deviations = deviations;
    }

    @Override
    public int getCount() {
        return deviations.length;
    }


    @Nullable
    @Override
    public String getItem(int position) {
        return deviations[position];
    }
}
