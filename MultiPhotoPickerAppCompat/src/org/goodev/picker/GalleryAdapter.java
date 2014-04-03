package org.goodev.picker;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.support.v4.util.LongSparseArray;
import android.support.v4.widget.CursorAdapter;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Checkable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GalleryAdapter extends CursorAdapter {

  final LayoutInflater mInflater;

  public GalleryAdapter(Context context) {
    super(context, null, false);
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return mInflater.inflate(R.layout.photo_item, parent, false);
  }

  @Override
  public void bindView(View view, final Context context, Cursor cursor) {
    final ImageView iv = (ImageView) view;
    // file path
    final String path = cursor.getString(GalleryPickerActivity.DATA_INDEX);
    final long id = cursor.getLong(GalleryPickerActivity.ID_INDEX);
    final Uri uri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id);
    final int width = iv.getWidth();
    if (width <= 0) {
      iv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

        @SuppressWarnings("deprecation")
        @Override
        public void onGlobalLayout() {
          iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          Picasso.with(context).load(uri).placeholder(R.drawable.picker_photo_holder)
              .resize(iv.getWidth(), iv.getHeight()).centerCrop().into(iv);
        }
      });
    } else {
      Picasso.with(context).load(uri).placeholder(R.drawable.picker_photo_holder).resize(iv.getWidth(), iv.getHeight())
          .centerCrop().into(iv);
    }

    if (view instanceof Checkable) {
      Checkable c = (Checkable) view;
      boolean checked = mCheckedIdStates.get(id, Boolean.FALSE);
      c.setChecked(checked);
    }
  }

  private SparseBooleanArray       mCheckStates     = new SparseBooleanArray();
  private LongSparseArray<Boolean> mCheckedIdStates = new LongSparseArray<Boolean>();

  public void setItemChecked(int position, boolean value) {
    mCheckStates.put(position, value);
    if (mCheckedIdStates != null) {
      if (value) {
        mCheckedIdStates.put(getItemId(position), Boolean.TRUE);
      } else {
        mCheckedIdStates.delete(getItemId(position));
      }
    }
  }

  public long[] getCheckedItemIds() {

    final LongSparseArray<Boolean> idStates = mCheckedIdStates;
    final int count = idStates.size();
    final long[] ids = new long[count];

    for (int i = 0; i < count; i++) {
      ids[i] = idStates.keyAt(i);
    }

    return ids;
  }

  public SparseBooleanArray getCheckedItemPositions() {
    return mCheckStates;
  }

  public boolean isItemChecked(int position) {
    if (mCheckStates != null) {
      return mCheckStates.get(position);
    }

    return false;
  }

  public void performItemClick(View view, int position, long id) {
    boolean newValue = !mCheckStates.get(position, false);
    mCheckStates.put(position, newValue);
    if (mCheckedIdStates != null && hasStableIds()) {
      if (newValue) {
        mCheckedIdStates.put(getItemId(position), Boolean.TRUE);
      } else {
        mCheckedIdStates.delete(getItemId(position));
      }
    }

    if (view instanceof Checkable) {
      Checkable checkable = (Checkable) view;
      checkable.setChecked(newValue);
    }
  }

  public void clearChoices() {
    if (mCheckStates != null) {
      mCheckStates.clear();
    }
    if (mCheckedIdStates != null) {
      mCheckedIdStates.clear();
    }
    notifyDataSetChanged();
  }
}
