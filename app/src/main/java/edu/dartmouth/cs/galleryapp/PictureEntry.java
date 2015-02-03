package edu.dartmouth.cs.galleryapp;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by nahokitade on 2/3/15.
 */
public class PictureEntry {
  public static final String DATE_FORMAT_STRING = "HH:mm:ss MMM dd yyyy";
  private Long id;
  private Calendar mDateTime;    // When does this entry happen
  private double mLatitude; // Location list
  private double mLongitude;
  private Bitmap bitmapPicture;

  public void setmDateTime(String dateTimeString) {
    try {
      Calendar mDateTime = new GregorianCalendar();
      SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
      Date datetime = dateFormat.parse(dateTimeString);
      mDateTime.setTime(datetime);
      this.mDateTime = mDateTime;
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getmDateTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
    return dateFormat.format(this.mDateTime.getTime());
  }

  public void setmDateTime(Calendar mDateTime) {
    this.mDateTime = mDateTime;
  }

  public double getmLatitude() {
    return mLatitude;
  }

  public void setmLatitude(double mLatitude) {
    this.mLatitude = mLatitude;
  }

  public double getmLongitude() {
    return mLongitude;
  }

  public void setmLongitude(double mLongitude) {
    this.mLongitude = mLongitude;
  }

  public Bitmap getBitmapPicture() {
    return bitmapPicture;
  }

  public void setBitmapPicture(Bitmap bitmapPicture) {
    this.bitmapPicture = bitmapPicture;
  }
}
