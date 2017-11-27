package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import static mydist.mydist.data.MasterContract.SubChannelContract;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class SubChannel implements Parcelable
{
    @JsonProperty("subChannelId")
    public String subChannelId;
    @JsonProperty("subChannelName")
    public String subChannelName;


    public SubChannel() {
    }

    public SubChannel(Cursor cursor){
        this.subChannelId = cursor.getString(cursor.getColumnIndex(SubChannelContract.COLUMN_SUB_CHANNEL_ID));
        this.subChannelName = cursor.getString(cursor.getColumnIndex(SubChannelContract.COLUMN_NAME));
    }

    protected SubChannel(Parcel in) {
        subChannelId = in.readString();
        subChannelName = in.readString();
    }

    public static final Creator<SubChannel> CREATOR = new Creator<SubChannel>() {
        @Override
        public SubChannel createFromParcel(Parcel in) {
            return new SubChannel(in);
        }

        @Override
        public SubChannel[] newArray(int size) {
            return new SubChannel[size];
        }
    };

    public String getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(String subChannelId) {
        this.subChannelId = subChannelId;
    }

    public String getSubChannelName() {
        return subChannelName;
    }

    public void setSubChannelName(String subChannelName) {
        this.subChannelName = subChannelName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subChannelId);
        dest.writeString(subChannelName);
    }

    @Override
    public String toString() {
        return this.getSubChannelName();
    }
}
