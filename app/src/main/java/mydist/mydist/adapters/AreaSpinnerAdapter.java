package mydist.mydist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Area;
import mydist.mydist.models.Channel;

/**
 * Created by Blessing.Ekundayo on 11/26/2017.
 */

public class AreaSpinnerAdapter extends ArrayAdapter<Area> {
    List<Area> areas;
    public AreaSpinnerAdapter(@NonNull Context context, List<Area> areas) {
        super(context, R.layout.spinner_item, R.id.name);
        this.areas = areas;
    }

    @Override
    public int getCount() {
        return areas.size();
    }


    @Nullable
    @Override
    public Area getItem(int position) {
        return areas.get(position);
    }
}
