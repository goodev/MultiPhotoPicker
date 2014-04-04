MultiPhotoPicker
================

Pick multiple photo from android gallery .

  **MultiPhotoPicker** : HONEYCOMB (Api 11) version.
  
  **MultiPhotoPickerAppCompat** : AppCompat (Api 7) version.
  
  
  ![](https://raw.githubusercontent.com/goodev/MultiPhotoPicker/master/screencast/1.png)
  ![](https://raw.githubusercontent.com/goodev/MultiPhotoPicker/master/screencast/2.png)
  
How to use
--------

    IntentAction.ACTION_PICK Action for pick single photo.
    IntentAction.ACTION_MULTIPLE_PICK for pick multiple photos.

```java
      Intent intent = new Intent();
      intent.setAction(IntentAction.ACTION_PICK);
      startActivityForResult(intent, CODE);
```
```java
      Intent intent = new Intent();
      intent.setAction(IntentAction.ACTION_MULTIPLE_PICK);
      startActivityForResult(intent, CODE);
```

* you can modify **org.goodev.picker.GalleryPickerActivity.onActionItemClicked(ActionMode, MenuItem)** method to change the return data type, such as: return photo path, uri, id ...*

```java
  //return Selected photos ids and file path
  @Override
  public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
    if (item.getItemId() == R.id.menu_done) {
      SparseBooleanArray position = isGingerbread() ? mAdapter.getCheckedItemPositions() : mGridView.getCheckedItemPositions();
      final int count = position.size();
      final ArrayList<String> pathList = new ArrayList<String>();

      for (int i = 0; i < count; i++) {
        if(position.valueAt(i)) {
          Cursor cursor = (Cursor) mAdapter.getItem(position.keyAt(i));
          pathList.add(cursor.getString(DATA_INDEX));
        }
      }

      String[] paths = new String[pathList.size()];
      paths = pathList.toArray(paths);
      long[] ids = isGingerbread() ? mAdapter.getCheckedItemIds() : mGridView.getCheckedItemIds();
      
      Intent data = new Intent();
      data.putExtra(IntentAction.EXTRA_DATA, ids);
      data.putExtra(IntentAction.EXTRA_PATH, paths);
      setResult(RESULT_OK, data);
      finish();
      return true;
    }
    return false;
  }
```

Android 系统选择多个图片 .

  **MultiPhotoPicker** : HONEYCOMB (Api 11， 3.0)以上版本。
  
  **MultiPhotoPickerAppCompat** : AppCompat (Api 7， 2.3) 版本，需要 AppCompat 库。
  
如何使用
--------

    IntentAction.ACTION_PICK  这个 Intent Action 用来选择一个图片
    IntentAction.ACTION_MULTIPLE_PICK 这个 Intent Action 用来选择多个图片
