package com.example.bluejoe.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChooseText extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 1;
    private static final String TAG = "ChooseText";
    private List<TextList> textList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_text);
        initTextList();
        writeDefaultFile();
        Button button_choose = (Button) findViewById(R.id.button_choose);
        TextListAdapter adapter = new TextListAdapter(ChooseText.this,
                R.layout.text_list_item, textList);
        ListView listView = (ListView) findViewById(R.id.text_list);
        listView.setAdapter(adapter);
        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Choose a txt file
                if (ContextCompat.checkSelfPermission(ChooseText.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChooseText.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openFileManager();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextList textItem = textList.get(position);
                InputStream inputStream = null;
                try {
                    inputStream = getAssets().open(textItem.getFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String string = getString(inputStream);
                Intent intent = new Intent(ChooseText.this, MarkText.class);
                intent.putExtra("string", string);
                startActivity(intent);
            }
        });
    }

    private void openFileManager() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult( intent, FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path;
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    assert uri != null;
                    path = getPath(ChooseText.this, uri);
                    // Read string from path
                    assert path != null;
                    File file = new File(path);
                    String fName = file.getName();
                    // Get extension
                    String end = fName.substring(fName.lastIndexOf("."),
                            fName.length()).toLowerCase();
                    Log.d(TAG, "onActivityResult: " + end);
                    if (end.equals(".txt")) {
                        String string = "";
                        try {
                            FileInputStream inputStream = new FileInputStream(file);
                            string = getString(inputStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(ChooseText.this, MarkText.class);
                        intent.putExtra("string", string);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "请选择文本类型的文件（*.txt）",  Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                Toast.makeText(this, "请选择一个文件",  Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
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
                final String[] selectionArgs = new String[] {
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
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
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
                    openFileManager();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void initTextList() {
        TextList sample1 = new TextList("Sample Text 1", "t0.txt");
        textList.add(sample1);
        TextList sample2 = new TextList("Sample Text 2", "t1.txt");
        textList.add(sample2);
        TextList sample3 = new TextList("Sample Text 3", "t2.txt");
        textList.add(sample3);
        TextList sample4 = new TextList("Sample Text 4", "t3.txt");
        textList.add(sample4);
        TextList sample5 = new TextList("Sample Text 5", "t4.txt");
        textList.add(sample5);
    }

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

    public void writeDefaultFile() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "test.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String info = "参考消息网11月2日报道 外媒称，在即将开始的亚洲之行期间，美国总统特朗普不会访问朝鲜与韩国之间的非军事区。";
            fos.write(info.getBytes());
            fos.close();
            Log.d(TAG, "writeDefaultFile: Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
