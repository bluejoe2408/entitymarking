package com.example.bluejoe.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends Activity {

    private static final int FILE_SELECT_CODE = 1;

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Mention mention = new Mention(0, "catdoglovecatdoglovecatdoglove");
//        mention.addEntity("cat", "animal", 0);
//        mention.addEntity("dog", "animal", 3);
//        mention.addRelation(0, 3, "love");
//        mention.addEntity("cat", "animal", 10);
//        mention.addEntity("dog", "animal", 13);
//        mention.addRelation(10, 13, "love");
//        mention.addEntity("cat", "animal", 20);
//        mention.addEntity("dog", "animal", 23);
//        mention.addRelation(20, 23, "love");
//        mention.removeRelation(0, 3);
//        mention.removeEntity(10);
//        if (MarkText.saveJSON(mention)) {
//            String articleId = mention.getArticleId();
//            String sentenceText = mention.getSentenceText();
//            if (MarkText.checkJSON(sentenceText) > 0) {
//                Log.d(TAG, "onCreate: checkJSON success");
//                File file = new File(Environment.getExternalStorageDirectory(), "entity_marking/" + articleId + ".json");
//                Mention newMention = MarkText.loadJSON(file);
//                Log.d(TAG, "onCreate: " + Environment.getExternalStorageDirectory());
//                Log.d(TAG, "onCreate: get mention md5 " + newMention.getArticleId());
//            }
//        }

        TextView title_entity = findViewById(R.id.title_entity);
        TextView title_marking = findViewById(R.id.title_marking);
        final TextView title_version = findViewById(R.id.title_version);
        final Button button_start = findViewById(R.id.button_start);
        final Button button_choose = findViewById(R.id.button_choose);
        final Button button_default = findViewById(R.id.button_default);
        final Typeface ventouse = Typeface.createFromAsset(getAssets(), "fonts/ventouse.ttf");
        final Animation move_btn_start = AnimationUtils.loadAnimation(this, R.anim.move_btn_start);
        final Animation move_btn_choose_text = AnimationUtils.loadAnimation(this, R.anim.move_btn_choose_text);
        final Animation move_title_left = AnimationUtils.loadAnimation(this, R.anim.move_title_left);
        final Animation move_title_right = AnimationUtils.loadAnimation(this, R.anim.move_title_right);
        final Animation move_title_up = AnimationUtils.loadAnimation(this, R.anim.move_title_up);
        final Animation show_btn_start = AnimationUtils.loadAnimation(this, R.anim.show_btn_start);

        title_entity.setTypeface(ventouse);
        title_marking.setTypeface(ventouse);
        title_version.setTypeface(ventouse);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    button_start.startAnimation(move_btn_start);
                }
            }
        });
        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Choose a txt file
                openFileManager();
            }
        });
        button_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChooseText.class);
                startActivity(intent);
            }
        });

        move_btn_start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button_start.setVisibility(View.GONE);
                button_choose.setVisibility(View.VISIBLE);
                button_default.setVisibility(View.VISIBLE);
                button_choose.setAnimation(move_btn_choose_text);
                button_default.setAnimation(move_btn_choose_text);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        title_entity.setAnimation(move_title_left);
        title_marking.setAnimation(move_title_right);

        move_title_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                title_version.setVisibility(View.VISIBLE);
                title_version.setAnimation(move_title_up);
                button_start.setVisibility(View.VISIBLE);
                button_start.setAnimation(show_btn_start);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void openFileManager() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path;
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    assert uri != null;
                    path = getPath(MainActivity.this, uri);
                    // Read string from path
                    assert path != null;
                    File file = new File(path);
                    String fName = file.getName();
                    // Get extension
                    String end = fName.substring(fName.lastIndexOf("."),
                            fName.length()).toLowerCase();
                    switch (end) {
                        case ".txt": {
                            try {
                                InputStream inputStream = new FileInputStream(file);
                                ArrayList<CharSequence> string = ChooseText.splitText(inputStream);
                                Intent intent = new Intent(MainActivity.this, MarkText.class);
                                intent.putExtra("type", "text");
                                intent.putExtra("string", string);
                                startActivity(intent);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "读取文件失败", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case ".json": {
//                            List<Mention> mentionList = new ArrayList<>();
                            String articleId = fName.substring(0, 31);
                            Intent intent = new Intent(MainActivity.this, MarkText.class);
                            int i = 0;
                            File jsonFile = new File(path.substring(0, path.length() - fName.length()) + articleId + "_" + i + ".json");
                            while (jsonFile.exists()) {
                                Mention mention = MarkText.loadJSON(jsonFile);
                                intent.putExtra("mention" + i, mention);
//                                mentionList.add(MarkText.loadJSON(jsonFile));
                                i++;
                                jsonFile = new File(path.substring(0, path.length() - fName.length()) + articleId + "_" + i + ".json");
                            }
                            intent.putExtra("type", "json");
                            intent.putExtra("num", i);
                            startActivity(intent);
                            break;
                        }
                        default:
                            Toast.makeText(this, "请选择文本或JSON格式的文件（*.txt|*.json）", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;
            default:
                Toast.makeText(this, "请选择一个文件", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @Nullable
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    @Nullable
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final Button button_start = findViewById(R.id.button_start);
                    final Animation move_btn_start = AnimationUtils.loadAnimation(this, R.anim.move_btn_start);
                    button_start.startAnimation(move_btn_start);
                } else {
                    Toast.makeText(this, "请允许App获得权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @NonNull
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        assert inputStreamReader != null;
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}