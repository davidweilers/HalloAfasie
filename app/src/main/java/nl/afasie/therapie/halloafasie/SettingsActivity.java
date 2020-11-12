package nl.afasie.therapie.halloafasie;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            Log.d("hoi",""+rootKey);

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            PreferenceScreen p = this.getPreferenceScreen();
//            setPreferencesFromResource(R.xml.settings_pref);
            final ListPreference listPreference = (ListPreference) findPreference("IK_HEB");
            if(listPreference.getValue()==null) {
                // to ensure we don't get a null value
                // set first value by default
                listPreference.setValueIndex(0);
            }
            listPreference.setSummary(listPreference.getEntry());
//            listPreference.
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    String s = newValue.toString();
                    preference.setSummary(newValue.toString());
                    return true;
                }
            });

//            SwitchPreference switchPreference = (SwitchPreference) findPreference("BEGRIJP");
//            switchPreference.setTitle(new String(""+switchPreference.getTitle()).replaceAll("\\<.*?\\>", ""));

            int zinnen_communicatie_tips = getResources()
                    .getIdentifier("zinnen_info_uitleg_afasie", "array", getContext().getPackageName());
            Log.d("hoi",""+zinnen_communicatie_tips);
            String[] zinnen = getResources().getStringArray(zinnen_communicatie_tips);

            SwitchPreference a;
            for (int i=0;i<zinnen.length;i++) {
                a = new SwitchPreference(getContext());
                a.setTitle(zinnen[i]);
                a.setKey("s"+i);
                p.addPreference(a);
            }
        }
    }
}