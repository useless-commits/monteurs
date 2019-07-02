package com.example.lukas.monteurs;

import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

import android.content.ContentValues;

import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class IncidentDBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3
            ;
    private static final String DATABASE_NAME = "incidentenDB.db";
    public static final String TABLE_NAME = "Incident";
    public static final String COLUMN_ID = "IncidentID";
    public static final String COLUMN_CATEGORIE = "IncidentCategorie";
    public static final String COLUMN_OMSCHRIJVING = "IncidentOmschrijving";
    public static final String COLUMN_IMAGEPATH = "IncidentImagepath";
    public static final String COLUMN_DATE = "IncidentDatum";
    public static final String COLUMN_LOCATION_LATITUDE = "IncidentLatitude";
    public static final String COLUMN_LOCATION_LONGITUDE = "IncidentLongitude";

    private static final String CREATE_QUERY = String.format("CREATE TABLE %1$s (%2$s INTEGER PRIMARY KEY, %3$s TEXT, %4$s TEXT, %5$s TEXT, %6$s TEXT,%7$s REAL,%8$s REAL)"
            ,TABLE_NAME, COLUMN_ID, COLUMN_CATEGORIE, COLUMN_OMSCHRIJVING, COLUMN_IMAGEPATH, COLUMN_DATE, COLUMN_LOCATION_LATITUDE, COLUMN_LOCATION_LONGITUDE);
    private SQLiteDatabase database;

    public IncidentDBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database =  getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    public boolean addIncident(String categorie, String omschrijving, String imagePath, float location_longitude, float location_latitude) {
        try{
            SQLiteDatabase db = database;
            ContentValues contentValues =  new ContentValues();
            contentValues.put(COLUMN_CATEGORIE, categorie);
            contentValues.put(COLUMN_OMSCHRIJVING, omschrijving);
            contentValues.put(COLUMN_IMAGEPATH, imagePath);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            contentValues.put(COLUMN_DATE, dateFormat.format(date).toString());

            contentValues.put(COLUMN_LOCATION_LATITUDE, location_latitude);
            contentValues.put(COLUMN_LOCATION_LONGITUDE, location_longitude);
            long result = db.insertOrThrow(TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            }
            return true;
        }
        catch (Exception e){
            return false;
        }

        //Failed

    }

    public boolean removeIncidentById(int db_id) {
        try{
            database.delete(TABLE_NAME, COLUMN_ID + "=" + String.valueOf(db_id), null);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public ArrayList<IncidentModel> getAllIncidents() {

        Cursor  cursor = database.rawQuery("select * from  " + TABLE_NAME + " ORDER BY " +COLUMN_DATE + " DESC"  ,null);
        ArrayList<IncidentModel> incidenten =  new ArrayList<IncidentModel>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String omschrijving = cursor.getString(cursor.getColumnIndex(COLUMN_OMSCHRIJVING));
                String categorie = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIE));
                String image_path = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEPATH));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                float longitude = cursor.getFloat(cursor.getColumnIndex(COLUMN_LOCATION_LONGITUDE));
                float latitude = cursor.getFloat(cursor.getColumnIndex(COLUMN_LOCATION_LATITUDE));

                incidenten.add(new IncidentModel(id,omschrijving, categorie, image_path,latitude, longitude, date));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return incidenten;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL(CREATE_QUERY);
    }
}
