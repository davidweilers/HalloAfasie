package nl.afasie.therapie.halloafasie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, TextToSpeech.OnInitListener {

    TextView text;
//    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    TextToSpeech speech;
    ImageButton home, revert;
    Spinner spinner;
    String lang = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speech = new TextToSpeech(this,this);

/*        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    Toast.makeText(MainActivity.this,""+status,Toast.LENGTH_SHORT).show();
//                    speech.setLanguage(new Locale("nl_NL"));
                    speech.setLanguage(Locale.US);
                }   else {
                    Toast.makeText(MainActivity.this,""+status,Toast.LENGTH_SHORT).show();
                    Log.d("hoi",""+status);
                }
//                speech.setLanguage(Locale.getDefault());
            }
        });*/
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
        home = (ImageButton)findViewById(R.id.home);

        setupSharedPreferences();
        //        toolbar = findViewById(R.id/);

        text = (TextView)findViewById(R.id.text);

        revert = (ImageButton) findViewById(R.id.revert);
        revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = "";
                setText();
            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.land,
                R.layout.spinner_item);
        spinner.setAdapter(adapter);

        setText();//sharedPreferences.getString("signature", ""));

    }

    @Override
    public void onInit(int i) {
        if(i == TextToSpeech.SUCCESS){
            Log.d("hoi",""+speech.getAvailableLanguages());
            Toast.makeText(this, ""+speech.getAvailableLanguages(), Toast.LENGTH_SHORT).show();
            int result = speech.setLanguage(new Locale("nl_NL"));
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "This language is not supported", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//                speech.setPitch((float) 0.98);
            }
        } else{
            Toast.makeText(this, "Init failed!", Toast.LENGTH_SHORT).show();
        }
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.about);

            String app = getResources().getString(R.string.app_about);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMessage(Html.fromHtml(app,FROM_HTML_MODE_COMPACT));
            }   else {
                Html.fromHtml(String.valueOf(Html.fromHtml(app)));
            }
//            builder.setNeutralButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
        if (id == R.id.communicatie) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.communicatie);
//            final CharSequence[] s = new CharSequence[]{"Standaard"};
            List<String> mStrings = new ArrayList<String>();
            String[] array = getResources().getStringArray(R.array.zinnen_communicatie_tips);
            mStrings.add("Standaard");
            mStrings.addAll(Arrays.asList(array));
            final String[] strings = new String[mStrings.size()];
            for(int i=0;i<mStrings.size();i++) {
                strings[i] = mStrings.get(i);
            }
//            strings = mStrings.toArray(strings);
            builder.setItems(strings, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // This is the place where you need to execute the logic
//                    Toast.makeText(getApplicationContext(), "the value: " + getResources().getStringArray(R.array.zinnen_communicatie_tips)[item], Toast.LENGTH_LONG).show();
//                    string = getResources().getStringArray(R.array.zinnen_communicatie_tips)[item];
                    if (item == 0) {
                        string = "";
                    }   else {
                        string = strings[item];
                    }
                    setText();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    String string = "";

    private void setText() {
        try {
            if (!string.isEmpty()) {
                revert.setVisibility(View.VISIBLE);
                text.setText(string);
                return;
            }
            revert.setVisibility(View.INVISIBLE);
            String[] _lang = getResources().getStringArray(getResources().getIdentifier("land", "array", getPackageName()));
            String[] _langid = getResources().getStringArray(getResources().getIdentifier("landid", "array", getPackageName()));

            for (int i=0;i<_lang.length;i++) {
                if (lang.equals(_lang[i])) {
                    speech.setLanguage(new Locale(_langid[i]+"_"+_langid[i].toUpperCase()));
                }
            }

            String t = getResources().getString(getResources().getIdentifier(lang+"_TEXT", "string", getPackageName()));
    //        String t = getString(R.string.Nederlands_TEXT);
            t = t.replaceAll("NAAM", sharedPreferences.getString("NAAM","naam"));

            int IK_HEB_1 = getResources().getIdentifier("Nederlands_IK_HEB", "array", getPackageName());
            String[] IK_HEB_2 = getResources().getStringArray(IK_HEB_1);

            int _IK_HEB = getResources().getIdentifier(lang+"_IK_HEB", "array", getPackageName());
            Log.d("hoi","> "+_IK_HEB);
            String[] IK_HEB = getResources().getStringArray(_IK_HEB);

            for (int i=0;i<IK_HEB_2.length;i++) {
                Log.d("hoi","'"+sharedPreferences.getString("IK_HEB",IK_HEB[0])+"' == '"+IK_HEB_2[i]+"'");
                if (sharedPreferences.getString("IK_HEB",IK_HEB[0]).equals(IK_HEB_2[i])) {
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

    public void home(View view) {
        if (speech.isSpeaking()) {
            speech.stop();
            return;
        }
        String a = new String(""+text.getText()).replaceAll("\\<.*?\\>", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            speech.speak(a, TextToSpeech.QUEUE_FLUSH,null,this.hashCode() + "");
        }   else {
            speech.speak(a, TextToSpeech.QUEUE_FLUSH,null);
        }
    }
}
