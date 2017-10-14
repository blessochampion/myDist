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
public class StoreProfileFragment extends Fragment {


    public StoreProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_profile, container, false);
    }

}
