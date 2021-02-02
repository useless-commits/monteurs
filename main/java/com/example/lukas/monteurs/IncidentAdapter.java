package com.example.lukas.monteurs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class IncidentAdapter extends ArrayAdapter<IncidentModel> {
    public IncidentAdapter(Context context, ArrayList<IncidentModel> incidenten) {

        super(context, 0, incidenten);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IncidentModel incidentModel =  getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.incident_item_layout, parent, false);
        }

        TextView omschrijving = (TextView) convertView.findViewById(R.id.omschrijving);
        TextView cateogorie = (TextView) convertView.findViewById(R.id.categorie);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        Button removebtn = (Button) convertView.findViewById(R.id.removebtn);
        Button emailbtn = (Button) convertView.findViewById(R.id.emailIncidentbtn);

        removebtn.setTag(position);
        emailbtn.setTag(position);

        cateogorie.setText(incidentModel.categorie);
        omschrijving.setText(incidentModel.omschrijving);
        image.setImageURI(Uri.fromFile(new File(incidentModel.image_path)));


        return convertView;

    }

}
