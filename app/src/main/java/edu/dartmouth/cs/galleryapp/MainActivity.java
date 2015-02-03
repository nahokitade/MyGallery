package edu.dartmouth.cs.galleryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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
  private static final int REQUEST_IMAGE = 1;
  private static final int REQUEST_DELETE = 2;

  ArrayList<Object> images = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GridView grid = (GridView) findViewById(R.id.photo_grid);

    // populate gridview
    grid.setAdapter(new ImagesAdapter(this, images));
    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getImage(position);
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

  // picture button handler
  public void takePic(View v) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


    startActivityForResult(intent, REQUEST_CAMERA);
  }

  // clicked picture handler
  public void getImage(int pos) {

  }

  @Override
  protected void onActivityResult(int request, int result, Intent data) {
    // handle camera return
    if (request == REQUEST_CAMERA && result == RESULT_OK) {
      // add image to database

      // update gridview

    }

    // handle image selected
    if (request == REQUEST_IMAGE && result == RESULT_OK) {
      // display image page
      Intent intent = new Intent(this, ViewImageActivity.class);

      startActivityForResult(intent, REQUEST_DELETE);
    }

    // handle image deleted in ViewImageActivity
    if (request == REQUEST_DELETE && result == RESULT_OK) {
      // remove image from database

      // update gridview

    }
  }


  public class ImagesAdapter extends ArrayAdapter<Object> {
    private Context mContext;

    // what kind of base adapter??
    public ImagesAdapter(Context context, ArrayList<Object> objects) {
      super(context, 0, objects);
      mContext = context;
    }

    // display each image in GridView
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
      ImageView imageView;
      if (convertView == null) {
        imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
      }
      else {
        imageView = (ImageView) convertView;
      }

      // imageView.setImageResource(images[index]]);

      return imageView;
    }
  }
}