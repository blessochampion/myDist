package mydist.mydist.fragments;


import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.DeviateSpinnerAdapter;
import mydist.mydist.adapters.StoreProfileAdapter;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreDeviateDialogFragment extends DialogFragment implements View.OnClickListener {

    public static  String KEY_RETAILER ="retailer";
    private static String retailerId;
    RetailerDateChangeListener listener;

    public interface RetailerDateChangeListener {
        public void onDateChangeRequested(String id);
    }

    public StoreDeviateDialogFragment() {
        // Required empty public constructor
    }

    public void setListener(RetailerDateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.deviationDialogFragmentStyle);
    }

    public static StoreDeviateDialogFragment getInstance(String id){
        StoreDeviateDialogFragment fragment = new StoreDeviateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RETAILER,id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retailerId = getArguments().getString(KEY_RETAILER);
        View view = inflater.inflate(R.layout.fragment_store_deviate, container, false);
        Button cancelButton = (Button) view.findViewById(R.id.bt_cancel);
        Button okayButton = (Button) view.findViewById(R.id.bt_ok);
        Spinner mDeviations = (Spinner) view.findViewById(R.id.sp_deviations);
        String[] deviations = new String[]{"Store is closed", "Request for Express Delivery", "Other"};
        mDeviations.setAdapter(new DeviateSpinnerAdapter(getActivity(), deviations));
        cancelButton.setOnClickListener(this);
        okayButton.setOnClickListener(this);
        setFonts(view);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;

    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getString(R.string.planning));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onClick(View v) {
        getDialog().cancel();
        if (v.getId() == R.id.bt_ok) {
            listener.onDateChangeRequested(retailerId);
        }
    }
}
