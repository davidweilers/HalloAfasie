package nl.weilers.david.halloafasie;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView text;
//    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    TextToSpeech speech;
    ImageButton home;
    Spinner spinner;
    String lang = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speech.setLanguage(new Locale("nl_NL"));
//                speech.setLanguage(Locale.getDefault());
            }
        });
//        speech.setLanguage(Locale.ENGLISH);
//        Log.d("hoi", ""+speech.getAvailableLanguages() );

        lang = "Nederlands";

        spinner = (Spinner) findViewById(R.id.lang_spinner);
//        spinner.s
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  lang = spinner.getSelectedItem().toString();
                  Log.d("hoi",""+spinner.getSelectedItem());
                  setText();
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
          });
//        home = (ImageButton)findViewById(R.id.home);
//
//        home.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View view) {
//                String a = new String(""+text.getText()).replaceAll("\\<.*?\\>", "");
//                a = "test test hoi";
//                speech.speak(a, TextToSpeech.QUEUE_FLUSH,null,null);
//            }
//        });
        

                setupSharedPreferences();
        //        toolbar = findViewById(R.id/);

        text = (TextView)findViewById(R.id.text);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.land,
                R.layout.spinner_item);
        spinner.setAdapter(adapter);

        setText();//sharedPreferences.getString("signature", ""));

    }

    private void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.d("hoi","setupSharedPreferences");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.activity_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.about) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void setText() {
        try {
            String[] _lang = getResources().getStringArray(getResources().getIdentifier("land", "array", getPackageName()));
            String[] _langid = getResources().getStringArray(getResources().getIdentifier("landid", "array", getPackageName()));

            for (int i=0;i<_lang.length;i++) {
                if (lang.equals(_lang[i])) {
                    speech.setLanguage(new Locale(_langid[i]+"_"+_langid[i].toUpperCase()));
                }
            }

            String t = getResources().getString(getResources().getIdentifier(lang+"_TEXT", "string", getPackageName()));
    //        String t = getString(R.string.Nederlands_TEXT);
            t = t.replaceAll("NAAM", sharedPreferences.getString("NAAM","Naam"));

            int IK_HEB_1 = getResources().getIdentifier("Nederlands_IK_HEB", "array", getPackageName());
            String[] IK_HEB_2 = getResources().getStringArray(IK_HEB_1);

            int _IK_HEB = getResources().getIdentifier(lang+"_IK_HEB", "array", getPackageName());
            Log.d("hoi","> "+_IK_HEB);
            String[] IK_HEB = getResources().getStringArray(_IK_HEB);

            for (int i=0;i<IK_HEB_2.length;i++) {
                Log.d("hoi","'"+sharedPreferences.getString("IK_HEB","s2")+"' == '"+IK_HEB_2[i]+"'");
                if (sharedPreferences.getString("IK_HEB","s2").equals(IK_HEB_2[i])) {
                    t = t.replaceAll("IK_HEB", IK_HEB[i]);
                    Log.d("hoi",">>"+IK_HEB[i]);
                }
            }

            String[] zinnen = getResources().getStringArray(getResources()
                    .getIdentifier("zinnen_info_uitleg_afasie", "array", getPackageName()));
            try {
                for (int i=0;i<zinnen.length;i++) {
                    Boolean b = sharedPreferences.getBoolean("s"+i,false);
                    if (b==true) {
                        t = t + "<br><br>" + zinnen[i];
                    }
                }
            }   catch (Exception e) {
                Log.e("hoi",""+e);
            }
            //        t = t.replaceAll("IK_HEB", sharedPreferences.getString("IK_HEB","s2"));

//            String BEGRIJP = getResources().getString(getResources().getIdentifier(lang+"_BEGRIJP", "string", getPackageName()));
//            t = t.replaceAll("BEGRIJP", sharedPreferences.getBoolean("BEGRIJP",false) ? BEGRIJP+"<br />"+"<br />" : "");
//            String LANGZAAM = getResources().getString(getResources().getIdentifier(lang+"_LANGZAAM", "string", getPackageName()));
//            t = t.replaceAll("LANGZAAM", sharedPreferences.getBoolean("LANGZAAM",false) ? LANGZAAM : "");
    //        Log.d("hoi",t);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text.setText(Html.fromHtml(t,FROM_HTML_MODE_COMPACT));
            }   else {
                text.setText(Html.fromHtml(t));
            }
        } catch (Exception e) {
            Log.d("hoi",""+e);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

//        Log.d("hoi",""+key);
//        if (key.equals("signature")) {
//            Log.d("hoi",""+sharedPreferences.getAll());
        setText();
//            sharedPreferences.
//            setTextVisible(sharedPreferences.getBoolean("display_text",true));
//        }
    }

    public void locale(View view) {
//        view.
//        switch (view.getId()) {
//            case R.id.locale_nl:
//                speech.setLanguage(new Locale("nl_NL"));
//                break;
//            case R.id.locale_en:
//                speech.setLanguage(new Locale("en_EN"));
//                break;
//            case R.id.locale_fr:
//                speech.setLanguage(new Locale("fr_FR"));
//                break;
//            case R.id.locale_de:
//                speech.setLanguage(new Locale("de_DE"));
//                break;
//            case R.id.locale_es:
//                speech.setLanguage(new Locale("es_ES"));
//                break;
//        }
//        home(view);
//        return false;
    }

    public void home(View view) {
        if (speech.isSpeaking()) {
            speech.stop();
            return;
        }
        String a = new String(""+text.getText()).replaceAll("\\<.*?\\>", "");
        speech.speak(a, TextToSpeech.QUEUE_FLUSH,null,null);
    }
}