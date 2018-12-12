package com.example.trevor.forgetfultransfer;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;




public class EditDataActivity extends AppCompatActivity {

    public  String setLocation = "Set Location";
    public String IDLatitude = "";
    public String IDLongitude = "";

    private FusedLocationProviderClient locClient;

    private static final String TAG = "EditDataActivity";

    private Button btnEdit, btnDelete, btnSetItemAsDest;
    private EditText editable_item;

    ListDataActivity mListDataActivity;
    DatabaseHelper mDatabaseHelper;
    OpenGLESRenderer mRenderClass;

    private String selectedName;
    private int selectedID;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_data_layout);
            btnEdit = findViewById(R.id.btnEdit);
            btnDelete =  findViewById(R.id.btnDelete);
            btnSetItemAsDest = findViewById(R.id.btnSetItemAsDest);
            editable_item = findViewById(R.id.editText_dataText);
            mDatabaseHelper = new DatabaseHelper(this);

            locClient = LocationServices.getFusedLocationProviderClient(this);

            mRenderClass = new OpenGLESRenderer();







            requestPermission();

            Intent receivedIntent = getIntent();

            selectedID = receivedIntent.getIntExtra("id", -1);

            selectedName = receivedIntent.getStringExtra("Name");

            editable_item.setText(selectedName);

            btnEdit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String item = editable_item.getText().toString();
                    if(!item.equals("")){
                        mDatabaseHelper.updateName(item, selectedID, selectedName);
                    } else {
                        toastMessage("You gotta enter a name, dude.");
                    }

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabaseHelper.deleteName(selectedID, selectedName);
                    editable_item.setText("");
                    toastMessage("removed from database");
                }


            });

            btnSetItemAsDest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String selectedIDString = Integer.toString(selectedID);

                    final Cursor destDataLocation = mDatabaseHelper.setDestFromChosenID(selectedIDString);



                        if (ActivityCompat.checkSelfPermission(EditDataActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }

                        locClient.getLastLocation().addOnSuccessListener(EditDataActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Location locInfo = location;
                                    while(destDataLocation.moveToNext()) {
                                        IDLatitude = destDataLocation.getString(2);
                                        IDLongitude = destDataLocation.getString(3);
                                        Log.d("latitude = ", IDLatitude);
                                        Log.d("longitude = ", IDLongitude);
//                    chosenNameID = IDData.getString(2);
                                        Double DIDLatitude = Double.parseDouble(IDLatitude);
                                        Double DIDLongitude = Double.parseDouble(IDLongitude);

                                        String destLatitude = DIDLatitude.toString();
                                        String destLongitude = DIDLongitude.toString();

                                        Location targetDestination = TargetDestination(DIDLatitude,DIDLongitude);
//                                        Location targetDestination = TargetDestination(300.76,277.88);



//                                        mDatabaseHelper.getTargetDestination(DIDLatitude, DIDLongitude);
                                                locInfo.bearingTo(targetDestination);
                                                float locDirection = locInfo.bearingTo(targetDestination);
                                                String locDirectionString = Float.toString(locDirection);
                                                Log.d("LOCDIRECTIONSTRING =", locDirectionString);

                                        GeomagneticField geomagneticField = new GeomagneticField((float)location.getLatitude(),
                                                (float)location.getLongitude(), (float)location.getAltitude(),location.getTime());

                                        float northDeclination = geomagneticField.getDeclination();
                                        String NorthernDeclinationString = Float.toString(northDeclination);


                                        Intent sendBearing = new Intent (EditDataActivity.this, OpenGLESRenderer.class);

                                        sendBearing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        sendBearing.putExtra("set Bearing", locDirectionString);
                                        sendBearing.putExtra("set Declination", NorthernDeclinationString);
                                        sendBearing.putExtra("LatDestination", destLatitude);
                                        sendBearing.putExtra("LongDestination", destLongitude);
                                        startActivity(sendBearing);

                                                Log.d("EditData bearing", locDirectionString);

                                                if(sendBearing!=null) {

                                                    Intent StartMyGLSurvaceView = new Intent(EditDataActivity.this, OpenGLESRenderer.class);

                                                    startService(StartMyGLSurvaceView);

                                                }
                                    }

                                }

                            }
                        });
                    }
            });


}

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    public Location TargetDestination (Double selectedLat, Double selectedLong) {

        Location targetDestination = new Location("");

        if(selectedLat != null) {
            targetDestination.setLatitude(selectedLat);
            targetDestination.setLongitude(selectedLong);
        }

        return targetDestination;

    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);


    }



}
