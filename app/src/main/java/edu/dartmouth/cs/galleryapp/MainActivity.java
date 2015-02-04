package edu.dartmouth.cs.galleryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends Activity {

  private static final int REQUEST_CAMERA = 0;
  private static final int REQUEST_DELETE = 1;
  protected static final String DB_EXTRA = "db_entry";

  ArrayList<PictureEntry> images = new ArrayList<>();
  GallerySQLiteHelper gallerySQLiteHelper;
  ImagesAdapter adapter;

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gallerySQLiteHelper = new GallerySQLiteHelper(this);
    images = gallerySQLiteHelper.fetchEntries();
    GridView grid = (GridView) findViewById(R.id.photo_grid);

    // populate gridview
    adapter = new ImagesAdapter(this, images);
    grid.setAdapter(adapter);
    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Log.d(TAG, "position = "+position);
        viewImage(position);

      }
    });


  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onResume() {
        super.onResume();
        images.clear();
        images.addAll(gallerySQLiteHelper.fetchEntries());
        adapter.notifyDataSetChanged();
        Log.d(TAG, "data set refreshed");
    }

  // picture button handler
  public void takePic(View v) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, REQUEST_CAMERA);
  }

  // clicked picture handler
  public void viewImage(int pos) {
      // open ViewImageActivity screen and wait for delete
      Intent intent = new Intent(this, ViewImageActivity.class);
      intent.putExtra(DB_EXTRA, pos);
      startActivityForResult(intent, REQUEST_DELETE);
  }

  @Override
  protected void onActivityResult(int request, int result, Intent data) {
    // handle camera return
    if (request == REQUEST_CAMERA && result == RESULT_OK) {
      // add image to database
      PictureEntry newPic = new PictureEntry();
      Bundle extras = data.getExtras();
      Bitmap photo = extras.getParcelable("data");
      Location location = getPictureLocation();
      if (location != null) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        newPic.setmLongitude(longitude);
        newPic.setmLatitude(latitude);
      }
      newPic.setBitmapPicture(photo);
      gallerySQLiteHelper.insertEntry(newPic);

      // update gridview
      onResume();

    }

    // handle image deleted in ViewImageActivity
    if (request == REQUEST_DELETE && result == RESULT_OK) {
      boolean delete = data.getBooleanExtra(ViewImageActivity.DB_DELETE, false);

      // update gridview
      if (delete) {
          onResume();
      }

    }
  }

  public Location getPictureLocation() {
    LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    String provider = manager.getBestProvider(criteria, true);
    if (provider != null) return manager.getLastKnownLocation(provider);
    return null;
  }

  public class ImagesAdapter extends ArrayAdapter<PictureEntry> {
    private Context mContext;

    // what kind of base adapter??
    public ImagesAdapter(Context context, ArrayList<PictureEntry> objects) {
      super(context, 0, objects);
      mContext = context;
    }

    // display each image in GridView
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
      ImageView imageView;
      if (convertView == null) {
        imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
      }
      else {
        imageView = (ImageView) convertView;
      }
      long rowId = images.get(pos).getId();

      imageView.setImageBitmap(gallerySQLiteHelper.fetchEntryByIndex(rowId).getBitmapPicture());

      return imageView;
    }
  }
}