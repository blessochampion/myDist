package mydist.mydist.fragments;


import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.StoreProfileAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreDeviateDialogFragment extends DialogFragment implements View.OnClickListener {


    public StoreDeviateDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.deviationDialogFragmentStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_deviate, container,false);
        Button cancelButton = (Button) view.findViewById(R.id.bt_cancel);
        Button okayButton = (Button) view.findViewById(R.id.bt_ok);
        cancelButton.setOnClickListener(this);
        okayButton.setOnClickListener(this);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return  view;

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
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onClick(View v) {
        getDialog().cancel();
    }
}
