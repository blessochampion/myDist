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
import mydist.mydist.models.Channel;
import mydist.mydist.models.SubChannel;

/**
 * Created by Blessing.Ekundayo on 11/26/2017.
 */

public class SubChannelSpinnerAdapter extends ArrayAdapter<SubChannel> {
    List<SubChannel> subChannels;
    public SubChannelSpinnerAdapter(@NonNull Context context, List<SubChannel> subChannels) {
        super(context, R.layout.spinner_item, R.id.name);
        this.subChannels = subChannels;
    }

    @Override
    public int getCount() {
        return subChannels.size();
    }


    @Nullable
    @Override
    public SubChannel getItem(int position) {
        return subChannels.get(position);
    }
}
