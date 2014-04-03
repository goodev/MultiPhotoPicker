package org.goodev.picker;

import static org.goodev.picker.IntentAction.ACTION_MULTIPLE_PICK;

import org.goodev.picker.MultiSelectionUtil.MultiChoiceModeListener;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GalleryPickerActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>, OnItemClickListener,
    MultiChoiceModeListener {
  final static String[]                 PROJECTION = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
  final static String                   SORTORDER  = MediaStore.Images.Media._ID + " DESC";
  final static Uri                      URI        = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
  public static final int               DATA_INDEX = 0;
  public static final int               ID_INDEX   = 1;
  private static final int              LOADER_ID  = 0;
  private GridView                      mGridView;
  private GalleryAdapter                mAdapter;
  private boolean                       mIsMultiple;
  private MultiSelectionUtil.Controller mMultiSelectionController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    mIsMultiple = ACTION_MULTIPLE_PICK.equals(getIntent().getAction());
    final GridView view = (GridView) findViewById(R.id.grid);
    mAdapter = new GalleryAdapter(this);
    view.setAdapter(mAdapter);
    view.setOnItemClickListener(this);
    mMultiSelectionController = MultiSelectionUtil.attachMultiSelectionController(view, this, this);
    mMultiSelectionController.tryRestoreInstanceState(savedInstanceState);
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
    mMultiSelectionController.tryRestoreInstanceState();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mMultiSelectionController != null) {
      mMultiSelectionController.saveInstanceState(outState);
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.swapCursor(null);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (mIsMultiple) {
      if (isGingerbread()) {
        mAdapter.performItemClick(view, position, id);
      }
      mMultiSelectionController.onItemLongClick(parent, view, position, id);
      // if (view instanceof Checkable) {
      // Checkable c = (Checkable) view;
      // boolean checked = !c.isChecked();
      // mGridView.setItemChecked(position, checked);
      // }
    } else {
      Uri uri = ContentUris.withAppendedId(URI, id);
      Intent data = new Intent();
      data.putExtra(IntentAction.EXTRA_DATA, uri);
      setResult(RESULT_OK, data);
      finish();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mMultiSelectionController != null) {
      mMultiSelectionController.finish();
    }
    mMultiSelectionController = null;
  }

  @Override
  public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
    if (item.getItemId() == R.id.menu_done) {
      long[] ids = isGingerbread() ? mAdapter.getCheckedItemIds() : mGridView.getCheckedItemIds();
      Uri[] uris = new Uri[ids.length];
      for (int i = 0; i < ids.length; i++) {
        Uri uri = ContentUris.withAppendedId(URI, ids[i]);
        uris[i] = uri;
      }

      Intent data = new Intent();
      data.putExtra(IntentAction.EXTRA_DATA, uris);
      setResult(RESULT_OK, data);
      finish();
      return true;
    }
    return false;
  }

  @Override
  public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
    MenuInflater inflater = mode.getMenuInflater();
    inflater.inflate(R.menu.gallery, menu);
    int selectCount = getSelectCount();
    String title = getResources().getQuantityString(R.plurals.number_of_items_selected, selectCount, selectCount);
    mode.setTitle(title);
    return true;
  }

  @Override
  public void onDestroyActionMode(android.support.v7.view.ActionMode arg0) {

  }

  @Override
  public boolean onPrepareActionMode(android.support.v7.view.ActionMode arg0, Menu arg1) {
    return false;
  }

  @Override
  public void onItemCheckedStateChanged(android.support.v7.view.ActionMode mode, int position, long id, boolean checked) {
    int selectCount = getSelectCount();
    if (isGingerbread()) {
      mAdapter.setItemChecked(position, checked);
    }
    String title = getResources().getQuantityString(R.plurals.number_of_items_selected, selectCount, selectCount);
    mode.setTitle(title);
  }

  int getSelectCount() {
    if (isGingerbread()) {
      return mAdapter.getCheckedItemIds().length;
    } else {
      return mGridView.getCheckedItemIds().length;
    }
  }

  public static boolean isGingerbread() {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
  }
}
