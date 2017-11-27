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

public class ChannelSpinnerAdapter extends ArrayAdapter<Channel> {
    List<Channel> channels;
    public ChannelSpinnerAdapter(@NonNull Context context, List<Channel> channels) {
        super(context, R.layout.spinner_item, R.id.name);
        this.channels = channels;
    }

    @Override
    public int getCount() {
        return channels.size();
    }


    @Nullable
    @Override
    public Channel getItem(int position) {
        return channels.get(position);
    }
}
