package mydist.mydist.fragments;


import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract.*;
import mydist.mydist.data.RouteDbHelper;
import mydist.mydist.models.Retailer;
import mydist.mydist.models.SubChannel;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreProfileFragment extends Fragment {

    TextView mName;
    TextView mContactPerson;
    TextView mAddress;
    TextView mPhone;
    TextView mChannel;
    TextView mSubChannel;
    public static final String KEY_ID = "merchantId";

    public StoreProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_profile, container, false);
        mName = (TextView) view.findViewById(R.id.et_name);
        mContactPerson = (TextView) view.findViewById(R.id.et_contact_person);
        mAddress = (TextView) view.findViewById(R.id.et_address);
        mPhone = (TextView) view.findViewById(R.id.et_phone);
        mChannel = (TextView) view.findViewById(R.id.et_channel);
        mSubChannel = (TextView) view.findViewById(R.id.et_sub_channel);
        bindView();
        setFonts(view);
        return view;
    }

    private void bindView() {
        Bundle bundle = getArguments();
        String id = bundle.getString(KEY_ID);
        Cursor cursor = DatabaseManager.getInstance(getActivity()).getRetailerProfileById(id);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(RetailerContract.RETAILER_NAME));
        String contactPerson = cursor.getString(cursor.getColumnIndex(RetailerContract.CONTACT_PERSON_NAME));
        String address = cursor.getString(cursor.getColumnIndex(RetailerContract.ADDRESS));
        String phone = cursor.getString(cursor.getColumnIndex(RetailerContract.PHONE));
        String channelName = cursor.getString(cursor.getColumnIndex(ChannelContract.TABLE_NAME+"."+ChannelContract.COLUMN_NAME));
        String subChannelName = cursor.getString(cursor.getColumnIndex(SubChannelContract.TABLE_NAME + "."+ SubChannelContract.COLUMN_NAME));

        mName.setText(name);
        mContactPerson.setText(contactPerson);
        mAddress.setText(address);
        mPhone.setText(phone);
        mChannel.setText(channelName);
        mSubChannel.setText(subChannelName);
    }

    public static StoreProfileFragment getNewInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        StoreProfileFragment fragment = new StoreProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


}
