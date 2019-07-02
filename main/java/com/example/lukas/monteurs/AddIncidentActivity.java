package com.example.lukas.monteurs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddIncidentActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    LocationManager locationManager;
    String lattitude,longitude;

    private static final int REQUEST_LOCATION = 1;
    private final int SELECT_PHOTO = 4;
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private String _currentPhotoPath;

    public final String[] categorieen = new String[] {"Grondkabels", "Luchtkabels", "Hoogspanningsmasten", "Schakelkasten"};
    public Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner categorie_dropdown= findViewById(R.id.categorie);

        //Dropdown items categorie
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.categorieen);
        categorie_dropdown.setAdapter(adapter);

        //Select picture
        Button pickImage = (Button) findViewById(R.id.foto_toevoegen);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        //Take picture
        Button takeImage = (Button) findViewById(R.id.foto_maken);
        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        //Get location
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        _currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                this.imageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File getImageFile() {
        File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFiles[] = f.listFiles();

        if (imageFiles == null || imageFiles.length == 0) {
            return null;
        }

        File lastModifiedFile = imageFiles[0];
        for (int i = 1; i < imageFiles.length; i++) {
            if (lastModifiedFile.lastModified() < imageFiles[i].lastModified()) {
                lastModifiedFile = imageFiles[i];
            }
        }
        return lastModifiedFile;
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    File imageFile = getImageFile();

                    this.imageUri = Uri.fromFile(new File(imageFile.getAbsolutePath()));
                    Button btn = findViewById(R.id.foto_popup);
                    btn.setEnabled(true);
                    Toast.makeText(this,"Foto toegevoegd",Toast.LENGTH_SHORT).show();

                }
                break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    this.imageUri= imageReturnedIntent.getData();
                    File file = new File(getPath(imageUri));
                    //We reload the Uri from a file since the above code will make a copy which we can use in our application.
                    this.imageUri = Uri.fromFile(file);
                    Button btn = findViewById(R.id.foto_popup);
                    btn.setEnabled(true);
                    Toast.makeText(this,"Foto toegevoegd",Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
    public void saveIncident() {

    }
    public void onSaveClick(View view) {
        IncidentDBManager incident =  new IncidentDBManager(this);

        Spinner categorie = findViewById(R.id.categorie);
        String chosen_categorie = categorie.getSelectedItem().toString();

        EditText omschrijving =  findViewById(R.id.omschrijving);
        String omschrijving_str = omschrijving.getText().toString();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Zet jouw GPS aan zodat we de locatie van het incident kunnen vastleggen", Toast.LENGTH_LONG).show();
        }
        if (this.imageUri == null) {
            Toast.makeText(this, "Voeg een foto toe aan het incident", Toast.LENGTH_SHORT).show();
        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();

            if (longitude == null || lattitude == null) {
                Toast.makeText(this,"Kan geen locatie krijgen",Toast.LENGTH_SHORT).show();
                return;
            }

            boolean result = incident.addIncident(chosen_categorie, omschrijving_str, imageUri.getPath(), Float.parseFloat(longitude), Float.parseFloat(lattitude));

            if (result) {
                Intent returnHome = new Intent(this, Home.class);
                startActivity(returnHome);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public void onClick(View v) {

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(AddIncidentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (AddIncidentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AddIncidentActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            }
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
            String imagePath = (String)savedInstanceState.getSerializable("imageUri");

            if (imagePath != null) {
                this.imageUri = Uri.fromFile(new File(imagePath));

                if (this.imageUri != null) {
                    Button btn = findViewById(R.id.foto_popup);
                    btn.setEnabled(true);
                }
            }
        }
        catch (Exception e){

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try{
            // To save the image when the screen rotates or is in sleep mode.
            if (this.imageUri == null)
                outState.putSerializable("imageUri", null);
            else
                outState.putSerializable("imageUri", this.imageUri.getPath());

            super.onSaveInstanceState(outState);
        }
        catch (Exception e) {

        }
    }
    public void showImage(View view) {
        Dialog builder = new Dialog(this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                this.dismiss();
                return true;
            }
        };
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);

        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.show();
    }
}
