package com.example.trevor.forgetfultransfer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.util.Log.println;


public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private Button btnAdd, btnViewData;
    private EditText editText2;
    String locLatitudeString = "";
    String locLongitudeString = "";
    String emptyDefault = "";
    DatabaseHelper mDatabaseHelper;
    ListDataActivity mListDataActivity;

    public static final String EXTRA_MESSAGE = "com.example.ForgetfulTransfer.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabaseHelper = new DatabaseHelper(this);
        mListDataActivity = new ListDataActivity();
        btnAdd = findViewById(R.id.btnAdd);
        btnViewData =  findViewById(R.id.btnViewData);
        requestPermission();



        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText editText2 = findViewById(R.id.editText2);
                String newEntry = editText2.getText().toString();
                if(editText2.length() != 0) {
                    AddData(newEntry);
                    editText2.setText("");
                } else {
                    toastMessage("You must put something in the field, yo.");
                }
            }
        });



        btnViewData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

        LocationServices.getGeofencingClient(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.captureCurrentLocation);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                    if(location!=null){
                        TextView textView = findViewById(R.id.CurrentLocation);
                        textView.setText(location.toString());
//                        AddLocationData(locLatitudeString);


//        mDatabaseHelper.


                       Location locInfo = location;
                       locInfo.getBearing();








//                       double locLatitude = locInfo.getLatitude();
                       locLatitudeString = Double.toString(location.getLatitude());
                       locLongitudeString = Double.toString(locInfo.getLongitude());
                        AddLocationLatitudeData(locLatitudeString, locLongitudeString);
//                        AddLocationLongitudeData(locLongitudeString);

                       println(0, "latitude", locLatitudeString);
                        println(1, "longitude", locLongitudeString);

                        if(mListDataActivity.IDLatitude!="") {
                            String targetLocLat = mListDataActivity.IDLatitude;
                            String targetLocLong = mListDataActivity.IDLongitude;

                            Double DTargetLocLat = Double.parseDouble(targetLocLat);
                            Double DTargetLocLong = Double.parseDouble(targetLocLong);

                            if (mDatabaseHelper.getTargetDestination(DTargetLocLat, DTargetLocLong) != null) {

                                Location targetLocation = mDatabaseHelper.getTargetDestination(DTargetLocLat, DTargetLocLong);

                                float locDirection = locInfo.bearingTo(targetLocation);

                                String locDirectionString = Float.toString(locDirection);

                                Log.d("bearing", locDirectionString);

                            }
                        }
                    }

                    }
                });
            }
        });


    }

    public void AddLocationLatitudeData(String locLatEntry, String locLongEntry){
        boolean insertLocLatData = mDatabaseHelper.addLocData(locLatEntry, locLongEntry);
        if (insertLocLatData){
            toastMessage("Location Successfully Inserted");
        } else {
            toastMessage("Something done fucked up");
        }
    }

//    public void AddLocationLongitudeData(String locLongEntry){
//        boolean insertLocLongData = mDatabaseHelper.addLocLongData(locLongEntry);
//        if (insertLocLongData){
//            toastMessage("Location Successfully Inserted");
//        } else {
//            toastMessage("Something done fucked up");
//        }
//    }


    public void AddData(String newEntry){
        boolean insertData = mDatabaseHelper.addData(newEntry);
            if (insertData){
                toastMessage("Data Successfully Inserted");
            } else {
                toastMessage("Something done fucked up");
            }
    }

    private void toastMessage(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void sendMessage(View view){
Intent displayMessage = new Intent(this, DisplayMessageActivity.class);
EditText editMessageText = findViewById(R.id.editText);
String message = editMessageText.getText().toString();
displayMessage.putExtra(EXTRA_MESSAGE, message);
startActivity(displayMessage);

    }

    public void showGLActivity(View view){
        Intent openGLESActivity = new Intent(this, OpenGLESRenderer.class);

        startActivity(openGLESActivity);

    }

    public void requestPermission(){
ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);


    }
}



