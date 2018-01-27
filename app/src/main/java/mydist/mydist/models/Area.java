package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import mydist.mydist.data.MasterContract;

/**
 * Created by Blessing.Ekundayo on 1/27/2018.
 */

public class Area implements Parcelable {
    @JsonProperty("areaId")
    String areaId;
    @JsonProperty("areaName")
    String areaName;

    public Area() {

    }

    public Area(Cursor c) {
        areaId = c.getString(c.getColumnIndex(MasterContract.AreaContract.COLUMN_AREA_ID));
        areaName = c.getString(c.getColumnIndex(MasterContract.AreaContract.COLUMN_NAME));
    }

    protected Area(Parcel in) {
        areaId = in.readString();
        areaName = in.readString();
    }

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String id) {
        this.areaId = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String name) {
        this.areaName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaId);
        dest.writeString(areaName);
    }

    @Override
    public String toString() {
        return areaName;
    }
}
