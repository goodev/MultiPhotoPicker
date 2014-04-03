package org.goodev.picker;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  @Override
  public boolean hasStableIds() {
    return super.hasStableIds();
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
  }

}
