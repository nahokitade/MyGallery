package edu.dartmouth.cs.galleryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by nahokitade on 1/29/15.
 */
public class GallerySQLiteHelper extends SQLiteOpenHelper {

  private SQLiteDatabase database;

  public static final String TABLE_PICTURES = "pictures";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_DATE_TIME = "date_time";
  public static final String COLUMN_LATITUDE = "latitude";
  public static final String COLUMN_LONGITUDE = "longitude";
  public static final String COLUMN_PICTURE = "gps_picture";


  private static final String DATABASE_NAME = "exercises.db";
  private static final int DATABASE_VERSION = 1;
  private  String [] allColumns = new String[]{COLUMN_ID,
      COLUMN_DATE_TIME, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_PICTURE};


  // Database creation sql statement
  private static final String CREATE_TABLE_ENTRIES = "create table "
      + TABLE_PICTURES + "("
      + COLUMN_ID + " integer primary key autoincrement, "
  //    + COLUMN_DATE_TIME + " datetime not null, "
      + COLUMN_LATITUDE + " real, "
      + COLUMN_LONGITUDE + " real,"
      + COLUMN_PICTURE + " blob"
      + ");";


  // Constructor
  public GallerySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Create table schema if not exists
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_ENTRIES);
  }

  // Insert a item given each column value
  public long insertEntry(PictureEntry entry) {
    database = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_DATE_TIME, entry.getmDateTime());
    values.put(COLUMN_LATITUDE, entry.getmLatitude());
    values.put(COLUMN_LONGITUDE, entry.getmLongitude());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    entry.getBitmapPicture().compress(Bitmap.CompressFormat.PNG, 100, bos);
    values.put(COLUMN_PICTURE, bos.toByteArray());
    long insertId = database.insert(GallerySQLiteHelper.TABLE_PICTURES, null, values);
    database.close(); // Closing database connection
    return insertId;
  }

  // Remove an entry by giving its index
  public void removeEntry(long rowIndex) {
    database = getWritableDatabase();
    String id = String.valueOf(rowIndex);
    int delete = database.delete(TABLE_PICTURES, COLUMN_ID + " = ?", new String[] {id});
    Log.d("SQL", "deleted entry " + delete);
    database.close();
  }

  // Query a specific entry by its index.
  public PictureEntry fetchEntryByIndex(long rowId) {
    database = getReadableDatabase();
    Cursor cursor = database.query(TABLE_PICTURES, allColumns, COLUMN_ID +"=?",
        new String[]{ String.valueOf(rowId) }, null, null, null, null);
    PictureEntry exerciseEnt = null;
    if (cursor != null && cursor.moveToFirst()) {
      exerciseEnt = cursorToPicture(cursor);
      cursor.close();
    }
    else {
        Log.d("SQL", "cannot find row with id " + rowId);
    }
    database.close();
    return exerciseEnt;
  }

  // Query the entire table, return all rows
  public ArrayList<PictureEntry> fetchEntries() {
    database = getReadableDatabase();
    ArrayList<PictureEntry> exercises = new ArrayList<>();
    Cursor cursor = database.query(GallerySQLiteHelper.TABLE_PICTURES,
        allColumns, null, null, null, null, null);

    if (cursor != null && cursor.moveToFirst()) {
      while (!cursor.isAfterLast()) {
        PictureEntry exerciseEnt = cursorToPicture(cursor);
        exercises.add(exerciseEnt);
        cursor.moveToNext();
      }
    }
    cursor.close();
    return exercises;
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(GallerySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);
    onCreate(db);
  }



//  strImageID[i] = cursor.getString(cursor.getColumnIndex("_id"));
//  byte[] data = cursor.getBlob(cursor.getColumnIndex("image"));
//  Bitmap thumbnail = BitmapFactory.decodeByteArray(data, 0, data.length);
//  images.AddImage(thumbnail);

//  Bitmap bitmap;
//  ByteArrayOutputStream os = new ByteArrayOutputStream();
//  bitmap.(Bitmap.CompressFormat.PNG, 100, os);


  private PictureEntry cursorToPicture(Cursor cursor) {
    PictureEntry pictureEnt = new PictureEntry();
    pictureEnt.setId(cursor.getLong(0));
    pictureEnt.setmDateTime(cursor.getString(1));
    pictureEnt.setmLatitude(cursor.getDouble(2));
    pictureEnt.setmLongitude(cursor.getDouble(3));
    byte[] picBlob = cursor.getBlob(4);
    pictureEnt.setBitmapPicture(BitmapFactory.decodeByteArray(picBlob, 0, picBlob.length));
    return pictureEnt;
  }
}