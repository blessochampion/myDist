package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static mydist.mydist.data.MasterContract.ChannelContract;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class Channel implements Parcelable
{
    @JsonProperty("channelId")
    public String channelId;
    @JsonProperty("channelName")
    public String channelName;

    public Channel() {
        super();
    }

    protected Channel(Parcel in) {
        channelId = in.readString();
        channelName = in.readString();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Channel(Cursor cursor){
        this.channelId = cursor.getString(cursor.getColumnIndex(ChannelContract.COLUMN_CHANNEL_ID));
        this.channelName = cursor.getString(cursor.getColumnIndex(ChannelContract.COLUMN_NAME));

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelId);
        dest.writeString(channelName);
    }

    @Override
    public String toString() {
        return this.getChannelName();
    }
}
