package com.example.lukas.monteurs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class EnterName extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences(MonteurName.PREV, MODE_PRIVATE);
        String monteurName = pref.getString(MonteurName.MONTEUR_NAME_KEY, null);

        if (monteurName != null) {
            Intent home = new Intent(this, Home.class);
            startActivity(home);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
    }

    public void onSave(View view) {
        EditText monteur_name = findViewById(R.id.monteur_name);
        String name = monteur_name.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences(MonteurName.PREV, MODE_PRIVATE).edit();

        editor.putString(MonteurName.MONTEUR_NAME_KEY, name);
        editor.apply();
        Intent home = new Intent(this, Home.class);
        startActivity(home);
    }
}
