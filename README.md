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

Android 系统选择多个图片 .

  **MultiPhotoPicker** : HONEYCOMB (Api 11， 3.0)以上版本。
  
  **MultiPhotoPickerAppCompat** : AppCompat (Api 7， 2.3) 版本，需要 AppCompat 库。
  
如何使用
--------

    IntentAction.ACTION_PICK  这个 Intent Action 用来选择一个图片
    IntentAction.ACTION_MULTIPLE_PICK 这个 Intent Action 用来选择多个图片
