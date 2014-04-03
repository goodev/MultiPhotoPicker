package org.goodev.picker;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Checkable;
import android.widget.GridView;
import static org.goodev.picker.IntentAction.*;

public class GalleryPickerActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, OnItemClickListener,   MultiChoiceModeListener {
  final static String[]    PROJECTION = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
  final static String      SORTORDER  = MediaStore.Images.Media._ID +" DESC";
  final static Uri URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
  public static final int  DATA_INDEX = 0;
  public static final int  ID_INDEX   = 1;
  private static final int LOADER_ID  = 0;
  private GridView                 mGridView;
  private GalleryAdapter           mAdapter;
  private boolean mIsMultiple;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);
    mIsMultiple = ACTION_MULTIPLE_PICK.equals(getIntent().getAction());
    final GridView view = (GridView) findViewById(R.id.grid);
    mAdapter = new GalleryAdapter(this);
    view.setAdapter(mAdapter);
    view.setOnItemClickListener(this);
    view.setMultiChoiceModeListener(this);
    mGridView = view;
    getSupportLoaderManager().initLoader(LOADER_ID, null, this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, URI, PROJECTION, null, null, SORTORDER);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.swapCursor(null);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if(mIsMultiple) {
      if(view instanceof Checkable) {
        Checkable c = (Checkable) view;
        boolean checked = !c.isChecked();
        mGridView.setItemChecked(position, checked);
      }
    }else {
      Uri uri = ContentUris.withAppendedId(URI, id);
      Intent data = new Intent();
      data.putExtra(IntentAction.EXTRA_DATA, uri);
      setResult(RESULT_OK, data );
      finish();
    }
  }

  @Override
  public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    MenuInflater inflater = mode.getMenuInflater();
    inflater.inflate(R.menu.gallery, menu);
    int selectCount = mGridView.getCheckedItemCount();
    String title = getResources().getQuantityString(R.plurals.number_of_items_selected, selectCount, selectCount);
    mode.setTitle(title);
    return true;
  }

  @Override
  public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    return false;
  }

  @Override
  public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    if(item.getItemId() == R.id.menu_done) {
      System.out.println(item.getTitle() +" "+ item.getItemId());
      long[] ids = mGridView.getCheckedItemIds();
      Uri[] uris = new Uri[ids.length];
      for (int i = 0; i < ids.length; i++) {
        Uri uri = ContentUris.withAppendedId(URI, ids[i]);
        uris[i] = uri;
      }
      
      Intent data = new Intent();
      data.putExtra(IntentAction.EXTRA_DATA, uris);
      setResult(RESULT_OK, data );
      finish();
      return true;
    }
    return false;
  }

  @Override
  public void onDestroyActionMode(ActionMode mode) {
  }

  @Override
  public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
    int selectCount = mGridView.getCheckedItemCount();
    String title = getResources().getQuantityString(R.plurals.number_of_items_selected, selectCount, selectCount);
    mode.setTitle(title);
  }
}
