package mydist.mydist.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import mydist.mydist.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }


}