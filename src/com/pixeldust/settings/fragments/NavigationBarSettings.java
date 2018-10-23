package com.pixeldust.settings.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.pixeldust.settings.preferences.SecureSettingSwitchPreference;

public class NavigationBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String NAVIGATION_BAR_VISIBLE = "navigation_bar_visible";

    private SecureSettingSwitchPreference mNavigationBarShow;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.pixeldust_settings_navigation);

        // navigation bar show
        mNavigationBarShow = (SecureSettingSwitchPreference) findPreference(NAVIGATION_BAR_VISIBLE);
        mNavigationBarShow.setOnPreferenceChangeListener(this);
        int navigationBarShow = Settings.Secure.getInt(getContentResolver(),
                NAVIGATION_BAR_VISIBLE, 0);
        mNavigationBarShow.setChecked(navigationBarShow != 0);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mNavigationBarShow) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(),
		NAVIGATION_BAR_VISIBLE, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.PIXELDUST;
    }
}
