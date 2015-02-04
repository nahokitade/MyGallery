package edu.dartmouth.cs.galleryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewImageActivity extends Activity {

  private static final int REQUEST_DELETE = 2;
  protected static final String DB_DELETE = "db_delete";
  GallerySQLiteHelper gallerySQLiteHelper = new GallerySQLiteHelper(this);
  private long position;
  Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);
    intent = getIntent();
    position = intent.getIntExtra(MainActivity.DB_EXTRA, 0) + 1;
    PictureEntry entry = gallerySQLiteHelper.fetchEntryByIndex(position);

    ImageView image = (ImageView) findViewById(R.id.image);
    TextView textView = (TextView) findViewById(R.id.photo_text);

    textView.setText(entry.getmDateTime() +
        " GPS:" + entry.getmLatitude() + ", " + entry.getmLongitude() );
    image.setImageBitmap(entry.getBitmapPicture());
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_view_image, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  // delete button handler
  public void imageDelete(View v) {
    position = intent.getIntExtra(MainActivity.DB_EXTRA, 0);
    gallerySQLiteHelper.removeEntry(position);
    Intent result = new Intent();
    result.putExtra(DB_DELETE, true);
    setResult(REQUEST_DELETE, result);
    finish();

  }
}