package mydist.mydist.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.adapters.MerchandizingAdapter;
import mydist.mydist.models.Merchandize;
import mydist.mydist.models.MerchandizingVerification;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoMerchandisingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    ListView mMerchandisingList;
    Button mSave;
    private static final int KEY_MERCHANT_ID = 0;
    private static final int KEY_BRAND_ID = 1;
    HashMap<String, MerchandizingVerification> merchandizingTable = new HashMap<>();

    public StoreInfoMerchandisingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info_merchandising, container, false);
        mSave = (Button) view.findViewById(R.id.btn_save);
        mSave.setOnClickListener(this);

        mMerchandisingList = (ListView) view.findViewById(R.id.lv_merchandisingList);
        List<Merchandize> merchandizes = DataUtils.getAllMerchandize(getActivity());
        merchandizingTable = DataUtils.getMerchandizingVerification(StoreOverviewActivity.retailerId, Days.getTodayDate(), getActivity());
        mMerchandisingList.setAdapter(new MerchandizingAdapter(getActivity(), merchandizes) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchandizing_list_item, parent, false);

                }
                TextView brandName = (TextView) convertView.findViewById(R.id.brand_name);
               final CheckBox item = (CheckBox) convertView.findViewById(R.id.product_name);
                Merchandize merchandize = getItem(position);
                brandName.setText(merchandize.getBrandName());
                item.setText(merchandize.getMerchandizeItem());


                if (merchandizingTable.containsKey(merchandize.getMerchantId() + "_" + merchandize.getBrandId()) &&
                        merchandizingTable.get(merchandize.getMerchantId() + "_" + merchandize.getBrandId()).getAvailable()
                                == MerchandizingVerification.STATUS_AVAILABLE) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setChecked(true);
                        }
                    }, 100);

                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setChecked(false);
                        }
                    }, 100);
                }
                item.setOnCheckedChangeListener(StoreInfoMerchandisingFragment.this);
                item.setTag(merchandize.getMerchantId() + "_" + merchandize.getBrandId());
                return convertView;
            }
        });
        setFonts(view);
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String[] tag = buttonView.getTag().toString().split("_");
        String merchandizingId = tag[0];
        String brandId = tag[1];
        int available = isChecked ? MerchandizingVerification.STATUS_AVAILABLE :
                MerchandizingVerification.STATUS_NOT_AVAILABLE;
        if (merchandizingTable.containsKey(merchandizingId + "_" + brandId)) {
            merchandizingTable.get(merchandizingId + "_" + brandId).setAvailable(available);
        } else {
            MerchandizingVerification verification = new MerchandizingVerification(Days.getTodayDate(),
                    merchandizingId, brandId, StoreOverviewActivity.retailerId, available);
            merchandizingTable.put(merchandizingId + "_" + brandId, verification);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            if (merchandizingTable.size() > 0) {
                boolean result = DataUtils.persistMerchandizingVerification(merchandizingTable, getActivity());
                if (result) {
                    makeToast(getString(R.string.merchandizing_saving_success));
                } else {
                    makeToast(getString(R.string.merchandizing_saving_error));
                }
            }

        }
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
