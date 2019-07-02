package com.example.lukas.monteurs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.text.Html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EmailIncident {
    private Context context;

    public EmailIncident(Context context) {
        this.context=context;
    }

    public Intent CreateEmailIntent(IncidentModel incidentModel) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");

        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocation(incidentModel.latitude, incidentModel.longitude, 1);

            String locatie = "Locatie is niet beschikbaar";
            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);
                locatie = "";
                locatie += address.getPostalCode() + " ";
                locatie += address.getThoroughfare() + " ";
                locatie += address.getAdminArea() + " ";
                locatie += address.getCountryName();
            }

            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(incidentModel.image_path)));
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Incident over " + incidentModel.categorie + " om " + incidentModel.date);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, incidentModel.omschrijving + "\n\n" +
                                                                                          "Categorie: " + incidentModel.categorie + "\n" +
                                                                                          "Datum: " + incidentModel.date + "\n" +
                                                                                          "Monteur naam: " + new MonteurName().getName(context) + "\n" +
                                                                                          "Locatie: " + locatie + "\n" +
                                                                                          "Locatie op google maps: " + "http://maps.google.com/maps?q="+ incidentModel.latitude + "," + incidentModel.longitude
                                                                                          );
            return emailIntent;
        }
        catch (Exception e){
        }


        return null;
    }
}
