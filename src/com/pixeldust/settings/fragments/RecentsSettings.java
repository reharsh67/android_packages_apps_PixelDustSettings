/*
 * Copyright (C) 2017-2020 The PixelDust Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pixeldust.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.SearchIndexableResource;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.pixeldust.settings.preferences.SystemSettingMasterSwitchPreference;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class RecentsSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener, Indexable {

    private static final String PREF_STOCK_RECENTS_CATEGORY = "stock_recents_category";
    private static final String PREF_ALTERNATIVE_RECENTS_CATEGORY = "alternative_recents_category";
    private static final String PREF_SWIPE_UP_ENABLED = "swipe_up_enabled_warning";

    private PreferenceCategory mStockRecentsCategory;
    private PreferenceCategory mAlternativeRecentsCategory;
    private Context mContext;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.pixeldust_settings_recents);

        mStockRecentsCategory = (PreferenceCategory) findPreference(PREF_STOCK_RECENTS_CATEGORY);
        mAlternativeRecentsCategory =
                (PreferenceCategory) findPreference(PREF_ALTERNATIVE_RECENTS_CATEGORY);

        // Alternative recents en-/disabling
        Preference.OnPreferenceChangeListener alternativeRecentsChangeListener =
                new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateDependencies(preference, (Boolean) newValue);
                return true;
            }
        };
        for (int i = 0; i < mAlternativeRecentsCategory.getPreferenceCount(); i++) {
            Preference preference = mAlternativeRecentsCategory.getPreference(i);
            if (preference instanceof SystemSettingMasterSwitchPreference) {
                preference.setOnPreferenceChangeListener(alternativeRecentsChangeListener);
            }
        }

        updateDependencies();

        // Warning for alternative recents when gesture navigation is enabled,
        // which directly controls quickstep (launcher) recents.
        final int navigationMode = getActivity().getResources()
                .getInteger(com.android.internal.R.integer.config_navBarInteractionMode);
        // config_navBarInteractionMode:
        //  0: 3 button mode (supports slim recents)
        //  1: 2 button mode (currently does not support alternative recents)
        //  2: gesture only (currently does not support alternative recents)
        if (navigationMode != 0) {
            for (int i = 0; i < mAlternativeRecentsCategory.getPreferenceCount(); i++) {
                Preference preference = mAlternativeRecentsCategory.getPreference(i);
                if (PREF_SWIPE_UP_ENABLED.equals(preference.getKey())) {
                    // We want to have that one enabled
                    continue;
                }
                preference.setEnabled(false);
            }
        } else {
            mAlternativeRecentsCategory.removePreference(findPreference(PREF_SWIPE_UP_ENABLED));
        }
    }

    private void updateDependencies() {
        updateDependencies(null, null);
    }

    private void updateDependencies(Preference updatedPreference, Boolean newValue) {
        // Disable stock recents category if alternative enabled
        boolean alternativeRecentsEnabled = newValue != null && newValue;
        if (!alternativeRecentsEnabled) {
            for (int i = 0; i < mAlternativeRecentsCategory.getPreferenceCount(); i++) {
                Preference preference = mAlternativeRecentsCategory.getPreference(i);
                if (preference == updatedPreference) {
                    // Already used newValue
                    continue;
                }
                if (preference instanceof SystemSettingMasterSwitchPreference
                        && ((SystemSettingMasterSwitchPreference) preference).isChecked()) {
                    alternativeRecentsEnabled = true;
                    break;
                }
            }
        }
        if (mStockRecentsCategory != null) {
            mStockRecentsCategory.setEnabled(!alternativeRecentsEnabled);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.PIXELDUST;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                            boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.pixeldust_settings_misc;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
            };
}
