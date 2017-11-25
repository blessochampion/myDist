package mydist.mydist.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import mydist.mydist.R;
import mydist.mydist.activities.StoreOverviewActivity;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCoverageFragment extends Fragment implements View.OnClickListener {


    public AllCoverageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_coverage, container, false);
        ImageView info1 = (ImageView) view.findViewById(R.id.iv_info1);
        ImageView info2 = (ImageView) view.findViewById(R.id.iv_info2);
        RelativeLayout itemContainer = (RelativeLayout) view.findViewById(R.id.item);
        itemContainer.setOnClickListener(this);
        info1.setOnClickListener(this);
        info2.setOnClickListener(this);
        setFonts(view);
        return view;
    }
    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.item){
            new StoreDeviateDialogFragment().show(getActivity().getSupportFragmentManager(), "");
        }else {
            new StoreProfileHistoryDialogFragment().show(getActivity().getSupportFragmentManager(), "");
        }
    }

    private void launchStoreDetailsActivity()
    {
        Intent intent = new Intent(getActivity(), StoreOverviewActivity.class);
        startActivity(intent);
    }
}
