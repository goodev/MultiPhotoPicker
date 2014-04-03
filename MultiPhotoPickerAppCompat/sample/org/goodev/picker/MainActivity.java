package org.goodev.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class MainActivity extends ActionBarActivity implements OnClickListener {
  final static int    CODE         = 1;
  final static String KEY_MULTIPLE = "KEY_MULTIPLE";
  GridView            mGridView;
  GridAdapter         mAdapter;
  boolean             mIsMultiple;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo);
    GridView view = (GridView) findViewById(R.id.grid);
    mAdapter = new GridAdapter(this);
    view.setAdapter(mAdapter);
    mGridView = view;
    if (savedInstanceState != null) {
      mIsMultiple = savedInstanceState.getBoolean(KEY_MULTIPLE);
    }
    findViewById(R.id.single).setOnClickListener(this);
    findViewById(R.id.multiple).setOnClickListener(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == CODE) {
      if (mIsMultiple) {
        Parcelable[] uris = data.getParcelableArrayExtra(IntentAction.EXTRA_DATA);
        mAdapter.setData(uris);
      } else {
        Uri uri = data.getParcelableExtra(IntentAction.EXTRA_DATA);
        mAdapter.setData(uri);
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(KEY_MULTIPLE, mIsMultiple);
  }

  @Override
  public void onClick(View v) {
    Intent intent = new Intent();
    switch (v.getId()) {
    case R.id.single:
      mIsMultiple = false;
      intent.setAction(IntentAction.ACTION_PICK);
      break;
    case R.id.multiple:
      mIsMultiple = true;

      intent.setAction(IntentAction.ACTION_MULTIPLE_PICK);
      break;

    }

    startActivityForResult(intent, CODE);
  }

}
