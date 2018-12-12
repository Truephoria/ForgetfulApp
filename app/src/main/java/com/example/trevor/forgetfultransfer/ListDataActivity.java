package com.example.trevor.forgetfultransfer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;
    public int chosenNameID;
    public String chosenName;
    public String name;
    public String chosenNameIDString;
    public String IDLatitude = "";
    public String IDLongitude = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = findViewById(R.id.locListView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView(){
    Log.d(TAG, "populateListView: Displaying data in the ListView.");

    final Cursor data = mDatabaseHelper.getData();

    ArrayList<String> listData = new ArrayList<>();
    while(data.moveToNext()){
        listData.add(data.getString(1));
    }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 name = adapterView.getItemAtPosition(i).toString();
                int itemID = i;
                String itemIDString = Integer.toString(itemID);

//    String debugIDString = data.getString(0);
//                              adapterView.getItemAtPosition(i).toString();
    Log.d(TAG, "onItemClick: You Clicked on " + name);

//    Log.d("data debug = ", debugIDString);



                final Cursor IDData = mDatabaseHelper.getItemID(name);
                final Cursor destDataLocation = mDatabaseHelper.setDestFromChosenID(itemIDString);

//                 IDData = mDatabaseHelper.getItemID(name);


                Log.d(TAG, itemIDString);
                while(destDataLocation.moveToNext()) {

                    IDLatitude = destDataLocation.getString(2);
                    IDLongitude = destDataLocation.getString(3);
                    Log.d("latitude = ", IDLatitude);
                    Log.d("longitude = ", IDLongitude);
//                    chosenNameID = IDData.getString(2);
                  Double DIDLatitude = Double.parseDouble(IDLatitude);
                  Double DIDLongitude = Double.parseDouble(IDLongitude);
                    mDatabaseHelper.getTargetDestination(DIDLatitude, DIDLongitude);

//                    IDData.getColumnIndex("SavedTextDesc")
//                    itemID = IDData.getInt(i);

//                }
                }

                if (destDataLocation != null) {

                    do {
//                            String IDLatitude = destDataLocation.getString(2);
//                        String IDLongitude = destDataLocation.getString(3);

//                        Log.d(TAG, IDLatitude + IDLongitude);
//                        itemID = IDData.getInt(i);
                        chosenName = name;

                        if (chosenName != null) {
                            chosenNameIDString = Integer.toString(itemID);
//                            mDatabaseHelper.setDestFromChosenID(chosenNameIDString);
                            Log.d(TAG + "=", chosenNameIDString);
                        }


                    }

                    while (IDData.moveToNext());

                }

                if(itemID > -1){
                    int chosenID = itemID;
                    Log.d(TAG, "chosenID = " + chosenID);
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);



                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("Name", name);
                    startActivity(editScreenIntent);
                } else {
                    toastMessage("No ID associated with that name...");

                }
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
