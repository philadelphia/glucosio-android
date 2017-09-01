/*
 * Copyright (C) 2016 Glucosio Foundation
 *
 * This file is part of Glucosio.
 *
 * Glucosio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Glucosio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Glucosio.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package org.glucosio.android.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import org.glucosio.android.GlucosioApplication;
import org.glucosio.android.R;
import org.glucosio.android.analytics.Analytics;
import org.glucosio.android.tools.network.GlucosioExternalLinks;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_about);

        getFragmentManager().beginTransaction()
                .replace(R.id.aboutPreferencesFrame, new MyPreferenceFragment()).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.preferences_about_glucosio));
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private int easterEggCount;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.about_preference);

            Preference licencesPref = findPreference("preference_licences");
            Preference ratePref = findPreference("preference_rate");
            Preference feedbackPref = findPreference("preference_feedback");
            Preference privacyPref = findPreference("preference_privacy");
            Preference termsPref = findPreference("preference_terms");
            Preference versionPref = findPreference("preference_version");
            Preference thanksPref = findPreference("preference_thanks");

            termsPref.setOnPreferenceClickListener(this);

            licencesPref.setOnPreferenceClickListener(this);

            ratePref.setOnPreferenceClickListener(this);

            feedbackPref.setOnPreferenceClickListener(this);

            privacyPref.setOnPreferenceClickListener(this);

            thanksPref.setOnPreferenceClickListener(this);

            versionPref.setOnPreferenceClickListener(this);

        }

        private void addTermsAnalyticsEvent(String action) {
            Analytics analytics = ((GlucosioApplication) getActivity().getApplication()).getAnalytics();

            analytics.reportAction("Preferences", action);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "preference_feedback":
                    sendFeedBack();
                    return true;
                case "preference_rate":
                    rate();
                    return true;
                case "preference_terms":
                    openUseItems();
                    return true;
                case "preference_privacy":
                    checkPrivacy();
                    return true;
                case "preference_licences":
                    checkLicense();
                    return true;
                case "preference_thanks":
                    acknowledge();
                    return true;
                case "preference_version":
                    showVersion();
                    return true;

                default:
                    return false;
            }
        }

        private void sendFeedBack() {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:hello@glucosio.org"));
            boolean activityExists = emailIntent.resolveActivityInfo(getActivity().getPackageManager(), 0) != null;

            if (activityExists) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.menu_support_error1), Toast.LENGTH_LONG).show();
            }
        }

        private void rate() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.glucosio.android"));
            startActivity(intent);
        }

        private void openUseItems() {
            ExternalLinkActivity.launch(
                    getActivity(),
                    getString(R.string.preferences_terms),
                    GlucosioExternalLinks.TERMS);
            addTermsAnalyticsEvent("Glucosio Terms opened");
        }

        private void checkPrivacy() {
            ExternalLinkActivity.launch(
                    getActivity(),
                    getString(R.string.preferences_privacy),
                    GlucosioExternalLinks.PRIVACY);
            addTermsAnalyticsEvent("Glucosio Privacy opened");
        }

        private void acknowledge() {
            ExternalLinkActivity.launch(
                    getActivity(),
                    getString(R.string.preferences_contributors),
                    GlucosioExternalLinks.THANKS);
            addTermsAnalyticsEvent("Glucosio Contributors opened");
        }

        private void checkLicense() {
            ExternalLinkActivity.launch(
                    getActivity(),
                    getString(R.string.preferences_licences_open),
                    GlucosioExternalLinks.LICENSES);
            addTermsAnalyticsEvent("Glucosio Licence opened");
        }

        //        此处会有一个彩蛋
        private void showVersion() {
            if (easterEggCount == 6) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 40.794010, 17.124583);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
                easterEggCount = 0;
            } else {
                easterEggCount = easterEggCount + 1;
            }
        }

    }
}
