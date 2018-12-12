package com.example.trevor.forgetfultransfer;

import android.content.ContentValues;
import android.content.Context;
//import android.database.DatabaseErrorHandler;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    Double destLatData;
    Double destLongData;

    private static final String TABLE_NAME = "CappedLocations_Table";
    private static final String COL1 = "ID";
    private static final String COL2 = "SavedTextDesc";
    private static final String COL3 = "latitude";
    private static final String COL4 = "longitude";
    public Double selectedLatitude;
    public Double selectedLongitude;






    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
              COL2 +" TEXT, " +
              COL3 +" TEXT, " +
              COL4 +" TEXT" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, item);


        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean addLocData(String locLat, String locLong){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues locLatValues = new ContentValues();
//        ContentValues locLongValue = new ContentValues();


        locLatValues.put(COL3, locLat);
        locLatValues.put(COL4, locLong);
        locLatValues.put(COL2, "Current Location_Lat");
//        locLongValue.put(COL4, locLong);

//        Log.d(TAG, "addLocData: Adding " + locLat + " " + locLong + " to " + TABLE_NAME);
        long locResult = db.insert(TABLE_NAME, null, locLatValues);



        if(locResult == -1){
            return false;
        }else {
            return true;
        }
    }

//    public boolean addLocLongData(String locLongi){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues locLongValues = new ContentValues();
//        ContentValues locLongValue = new ContentValues();
//
//
//        locLongValues.put(COL4, locLongi);
//        locLongValues.put(COL2, "Current Location_Long");
//        locLongValue.put(COL4, locLong);
//
//        Log.d(TAG, "addLocData: Adding " + locLat + " " + locLong + " to " + TABLE_NAME);
//        long locResult = db.insert(TABLE_NAME, null, locLongValues);
//
//
//
//        if(locResult == -1){
//            return false;
//        }else {
//            return true;
//        }
//    }




    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data= db.rawQuery(query, null);
        return data;


    }

    public Cursor getItemID(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query,null);
        return data;






    }

    public Cursor setDestFromChosenID(String nameID){

//        String[] args = {COL3, COL4};


        int nameIDInt = Integer.parseInt(nameID);
       String nameIDIntString = Integer.toString(nameIDInt);

        Log.d("DBHelper IDInt = ", nameIDIntString);

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL1 + " = '" + nameIDInt + "'";
        Cursor destData = db.rawQuery(query, null);

       int debugcolumnCount;
//       if(destData.isNull(2)) {
//           String debugData = Integer.toString(debugcolumnCount);
//
//            String debugError = "Column data is null...";
//           Log.d("DBHelper error", debugError);


//       }


//
//        String destLatDataString = destData.getString(2);
//        String destLongDataString = destData.getString(3);
//
        Log.d("DBHelper", "Passed Query");
//
//        destLatData = Double.valueOf(destLatDataString);
//        destLongData = Double.valueOf(destLongDataString);
//
//        selectedLatitude = destLatData;
//
//        selectedLongitude = destLongData;

if(selectedLatitude != null) {
//    getTargetDestination();


}






        return destData;
    }


    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);

    }

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);

    }


        public Location getTargetDestination (Double selectedLat, Double selectedLong) {

            Location targetDestination = new Location("");

            if(selectedLat != null) {
                targetDestination.setLatitude(selectedLat);
                targetDestination.setLongitude(selectedLong);
            }

        return targetDestination;

    }

}

