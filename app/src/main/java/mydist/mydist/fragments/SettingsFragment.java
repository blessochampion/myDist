package mydist.mydist.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;

import mydist.mydist.R;
import mydist.mydist.utils.FontManager;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }


}
