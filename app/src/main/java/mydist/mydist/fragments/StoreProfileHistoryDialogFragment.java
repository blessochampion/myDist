package mydist.mydist.fragments;


import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.StoreProfileAdapter;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.fragments.StoreProfileFragment.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreProfileHistoryDialogFragment extends DialogFragment
{
    public StoreProfileHistoryDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_profile_dialog, container,false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_container);

        Bundle bundle = getArguments();
        String id = bundle.getString(KEY_ID);
        StoreProfileAdapter adapter = new StoreProfileAdapter(getChildFragmentManager(), getFragments(id), getTitles());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setFonts(view);
        return  view;

    }

    public static StoreProfileHistoryDialogFragment getNewInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        StoreProfileHistoryDialogFragment fragment = new StoreProfileHistoryDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    public List<Fragment> getFragments(String id) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(StoreProfileFragment.getNewInstance(id));
        fragments.add(new HistoryFragment());
        return fragments;
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.profile));
        titles.add(getString(R.string.history));
        return titles;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
