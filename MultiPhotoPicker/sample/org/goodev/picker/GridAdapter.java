package org.goodev.picker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GridAdapter extends BaseAdapter {

  private List<Parcelable> mItems = new ArrayList<Parcelable>();
  private Context          mContext;

  public GridAdapter(Context context) {
    mContext = context;
  }

  public void setData(Parcelable[] data) {
    mItems.clear();
    mItems.addAll(Arrays.asList(data));
    notifyDataSetChanged();
  }

  public void setData(Parcelable data) {
    mItems.clear();
    mItems.add(data);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return mItems.size();
  }

  @Override
  public Object getItem(int position) {
    return mItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext).inflate(R.layout.photo_item, null);
    }
    final ImageView iv = (ImageView) convertView;
    final Context context = mContext;
    final Uri uri = (Uri) getItem(position);
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
    return convertView;
  }

}
