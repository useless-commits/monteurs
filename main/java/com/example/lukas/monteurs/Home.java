package com.example.lukas.monteurs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class Home extends AppCompatActivity {
    private IncidentAdapter adapter;
    private ArrayList<IncidentModel> incidenten;
    private IncidentDBManager dbManagerIncident;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.dbManagerIncident = new IncidentDBManager(this);
        this.incidenten = new ArrayList<IncidentModel>();
        incidenten.add(new IncidentModel(22,"123","sambal", "/storage/emulated/0/Android/data/com.example.lukas.monteurs/files/Pictures/JPEG_20190522_124719_757650843.jpg", 0, 0 ,"asd"));
        incidenten.add(new IncidentModel(22,"123","balsam", "/storage/emulated/0/Android/data/com.example.lukas.monteurs/files/Pictures/JPEG_20190522_124719_757650843.jpg", 0, 0 ,"asd"));

        incidenten = dbManagerIncident.getAllIncidents();
        this.adapter = new IncidentAdapter(this, incidenten);

        ListView listView = findViewById(R.id.incidenten_listview);
        listView.setAdapter(this.adapter);
    }

    public void addIncident(View view) {
        Intent addPage = new Intent(this, AddIncidentActivity.class);
        startActivity(addPage);
    }

    public void RemoveItem(View view) {
        int incident_index = (int)view.getTag();
        IncidentModel incidentModel = incidenten.get(incident_index);

        incidenten.remove(incident_index);
        adapter.notifyDataSetChanged();

        boolean result = dbManagerIncident.removeIncidentById(incidentModel.id);
    }

    public void EmailItem(View view) {
        IncidentModel incidenttomail = incidenten.get((int)view.getTag());
        startActivity(Intent.createChooser(new EmailIncident(this).CreateEmailIntent(incidenttomail), "Email:"));
    }

}
