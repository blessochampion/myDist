package mydist.mydist.fragments;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract.*;
import mydist.mydist.data.RouteDbHelper;
import mydist.mydist.models.Retailer;
import mydist.mydist.models.SubChannel;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    TextView mName;
    TextView mContactPerson;
    TextView mAddress;
    TextView mPhone;
    TextView mChannel;
    TextView mSubChannel;
    TextView mAreaName;
    Button mButton;
    DialogFragment parent;
    View parentView;
    public static final String KEY_ID = "merchantId";
    public static final int KEY_LOAD_PROFILE = 90000;

    public StoreProfileFragment() {
        // Required empty public constructor
    }

    public void setParent(DialogFragment fragment) {
        parent = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_profile, container, false);
        mName = (TextView) view.findViewById(R.id.et_name);
        mContactPerson = (TextView) view.findViewById(R.id.et_contact_person);
        mAddress = (TextView) view.findViewById(R.id.et_address);
        mPhone = (TextView) view.findViewById(R.id.et_phone);
        mChannel = (TextView) view.findViewById(R.id.et_channel);
        mSubChannel = (TextView) view.findViewById(R.id.et_sub_channel);
        mAreaName = (TextView) view.findViewById(R.id.tv_area_name);
        mButton = (Button) view.findViewById(R.id.btn_start_visit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.getDialog().cancel();
            }
        });
        parentView = view;
        Bundle bundle = getArguments();
        if (bundle != null) {
            getActivity().getSupportLoaderManager().initLoader(KEY_LOAD_PROFILE, bundle, this);
        } else {
            throw new RuntimeException("Unable to Create Fragment, pass DAY and Week");
        }

        return view;
    }

    private void bindView(Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(RetailerContract.RETAILER_NAME));
            name = UIUtils.capitalizefirstLetter(name);
            String contactPerson = cursor.getString(cursor.getColumnIndex(RetailerContract.CONTACT_PERSON_NAME));
            contactPerson = UIUtils.capitalizefirstLetter(contactPerson);
            String address = cursor.getString(cursor.getColumnIndex(RetailerContract.ADDRESS));
            address = UIUtils.capitalizefirstLetter(address);
            String phone = cursor.getString(cursor.getColumnIndex(RetailerContract.PHONE));
            phone = UIUtils.capitalizefirstLetter(phone);
            String channelName = cursor.getString(cursor.getColumnIndex(  ChannelContract.COLUMN_NAME));
            channelName = UIUtils.capitalizefirstLetter(channelName);
            String subChannelName = cursor.getString(cursor.getColumnIndex(SubChannelContract.COLUMN_NAME));
            subChannelName = UIUtils.capitalizefirstLetter(subChannelName);
            String areaName = cursor.getString(cursor.getColumnIndex(AreaContract.COLUMN_NAME));
            areaName = UIUtils.capitalizefirstLetter(areaName);
            mName.setText(name);
            mContactPerson.setText(contactPerson);
            mAddress.setText(address);
            mPhone.setText(phone);
            mChannel.setText(channelName);
            mSubChannel.setText(subChannelName);
            mAreaName.setText(areaName);
            setFonts(parentView);
        }
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(getActivity()){
            @Override
            public Cursor loadInBackground() {
                if(id == KEY_LOAD_PROFILE){
                    return DatabaseManager.getInstance(getActivity()).getRetailerProfileById(args.getString(KEY_ID));
                }
                return super.loadInBackground();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == KEY_LOAD_PROFILE){
            bindView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
