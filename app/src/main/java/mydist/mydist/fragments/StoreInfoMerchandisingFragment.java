package mydist.mydist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mydist.mydist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoMerchandisingFragment extends Fragment {


    public StoreInfoMerchandisingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_info_merchandising, container, false);
    }

}
