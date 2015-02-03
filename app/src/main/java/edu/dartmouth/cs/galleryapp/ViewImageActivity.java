package edu.dartmouth.cs.galleryapp;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;


public class ViewImageActivity extends Activity {

  private static final int REQUEST_DELETE = 2;
  private static final String DB_DELETE = "db_delete";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);
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
    Intent intent = getIntent();
    //intent.putExtra(DB_DELETE, null);
    setResult(REQUEST_DELETE, intent);
    finish();

  }
}