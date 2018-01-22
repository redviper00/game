package com.example.dell_1.myapp3.ImageViewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.dell_1.myapp3.R;

import java.io.File;
import java.util.ArrayList;

import static com.example.dell_1.myapp3.ImageViewer.ImageGallery.al_images;

public class PhotosActivity extends AppCompatActivity {
    int int_position;
    private GridView gridView;
    private static final String  TAG = " com.example.dell_1.myapp3.ImageViewer";
    GridViewAdapter adapter;
    ArrayList<Model_images> al_menu = new ArrayList<>();
    private ArrayList<Integer> mSelected = new ArrayList<>();
    boolean boolean_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        final ImageButton buttoncut = (ImageButton) findViewById(R.id.button1);
        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        final ImageButton button5 = (ImageButton) findViewById(R.id.button5);
        final ImageButton buttonpaste = (ImageButton) findViewById(R.id.buttonpaste);
        buttoncut.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        button5.setVisibility(View.GONE);
        buttonpaste.setVisibility(View.GONE);


        gridView = (GridView) findViewById(android.R.id.list);

        int_position = getIntent().getIntExtra("value", 0);
        adapter = new GridViewAdapter(this, al_images, int_position);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String abc = "file://" + al_images.get(int_position).getAl_imagepath().get(position);

                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", position);
                i.putExtra("folderPosition", int_position);
                i.putExtra("abc", abc);
                startActivity(i);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if (mSelected.contains(position)) {
                    mSelected.remove(position);
                    view.setBackgroundColor(Color.TRANSPARENT);// remove item from list
                    // update view (v) state here
                    // eg: remove highlight
                } else {
                    mSelected.add(position);
                    view.setBackgroundColor(Color.LTGRAY);// add item to list
                    // update view (v) state here
                    // eg: add highlight
                }

                buttoncut.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);
                buttoncut.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                buttoncut.setVisibility(View.GONE);
                                button2.setVisibility(View.GONE);
                                button3.setVisibility(View.GONE);
                                button4.setVisibility(View.GONE);
                                button5.setVisibility(View.GONE);
                                Intent moveIntent = new Intent(PhotosActivity.this, ImageGallery.class);
                                moveIntent.putExtra("selected_images", getImagePaths(mSelected));
                                moveIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(moveIntent);
                                finish();
                            }
                        });

                button2.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                buttoncut.setVisibility(View.GONE);
                                button2.setVisibility(View.GONE);
                                button3.setVisibility(View.GONE);
                                button4.setVisibility(View.GONE);
                                button5.setVisibility(View.GONE);

                            }

                        });
                button3.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PhotosActivity.this);
                                builder1.setMessage("Are you sure you want to delete it ?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                adapter.updateUpdater(mSelected);
                                                for (int position = 0; position < mSelected.size(); position++) {
                                                    al_images.get(int_position).getAl_imagepath().remove(position);
                                                }
                                                finish();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        });

                button4.setOnClickListener(
                        new View.OnClickListener(){
                            public void onClick(View view){
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PhotosActivity.this);
                                builder2.setMessage("Rename File");
                                final EditText input = new EditText(PhotosActivity.this);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                input.setLayoutParams(lp);
                                builder2.setView(input);
                                builder2.setPositiveButton(
                                        "Rename",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                File oldName =new File(al_images.get(int_position).getAl_imagepath().get(position));
                                                String string = input.getText().toString();
                                                File newFile = new File(string);
                                                if(!newFile.exists()){
                                                    boolean success = oldName.renameTo(newFile);
                                                    if(!success){
                                                        Log.v(TAG,"not renamed");
                                                    }
                                                }else{
                                                    Log.e(TAG, "file is already exist");
                                                }
                                                }
                                            });


                                builder2.setNegativeButton(
                                        "Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert12 = builder2.create();
                                alert12.show();

                            }
                        }
                );

                return true;
            }


        });
    }

    public ArrayList<Model_images> fn_imagespath() {
        al_menu.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplication().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_menu.size(); i++) {
                if (al_menu.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_menu.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);
                al_menu.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(absolutePathOfImage);
                obj_model.setDirectoryPath(new File(absolutePathOfImage).getParent());
                obj_model.setAl_imagepath(al_path);

                al_menu.add(obj_model);
            }
        }

        for (int i = 0; i < al_menu.size(); i++) {
            Log.e("FOLDER", al_menu.get(i).getStr_folder());
            for (int j = 0; j < al_menu.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_menu.get(i).getAl_imagepath().get(j));
            }
        }
        adapter = new GridViewAdapter(this, al_menu, int_position);
        gridView.setAdapter(adapter);
        return al_menu;
    }

    private ArrayList<String> getImagePaths(ArrayList<Integer> selectedIndexList) {
        ArrayList<String> listOfImages = new ArrayList<>();
        for(Integer index : selectedIndexList) {
            listOfImages.add(al_images.get(int_position).getAl_imagepath().get(index));
        }

        return listOfImages;


    }
}
