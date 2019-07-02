package com.example.lukas.monteurs;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MonteurName {
    public static final String PREV = "MonteurNaam";
    public static final String MONTEUR_NAME_KEY =  "MONTEUR_NAME_KEY";

    public String getName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREV, MODE_PRIVATE);

        return (String) pref.getString(MONTEUR_NAME_KEY, "");
    }
}
