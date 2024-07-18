package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class AlbumView extends AppCompatActivity {

    public static Album currentAlbum;

    private Bitmap b;

    private Button insertPhoto;
    private Button deletePhoto;
    private Button displayPhoto; // display and slideshow are the same
    private Button openSlideshow;

    private Button movePhoto;

    private TextView textView;

    private EditText resultAlbum;

    private ListView photos;

    public static ArrayList<ImageView> allPhotos = new ArrayList<>();

    public static ArrayList<Photo> tempPhotos = new ArrayList<>();

    private ActivityResultLauncher<String> takePhoto;

    private int currentPhoto = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumview);
        insertPhoto = (Button) findViewById(R.id.insertPhoto);
        deletePhoto = (Button) findViewById(R.id.deletePhoto);
        openSlideshow = (Button) findViewById(R.id.openSlideshow);
        photos = (ListView) findViewById(R.id.photos);
        movePhoto = (Button) findViewById(R.id.movePhoto);
        textView = (TextView) findViewById(R.id.textV);
        resultAlbum = (EditText) findViewById(R.id.resultAlbum);
        MyAdapter adapter = new MyAdapter(this,R.layout.imgview,currentAlbum.getList());
        tempPhotos=currentAlbum.getList();
        photos.setAdapter(adapter);
//        ActivityResultLauncher<Intent> arc = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == RESULT_OK) {
//                    Intent data = result.getData();
//                    Uri uri = data.getData();
//                    try {
//                        b = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        ImageView img = new ImageView(AlbumView.this);
//                        img.setImageBitmap(b);
//                        Photo p = new Photo(uri);
//                        allPhotos.add(img);
//                        tempPhotos.add(p);
//                        MyAdapter m = new MyAdapter(AlbumView.this, R.layout.imgview, tempPhotos);
//                        photos.setAdapter(m);
//                    } catch (IOException e) {
//                        makeToast("There was an error adding your photo. Please try again.");
//                        return;
//                    }
//                }
//            }
//        });

        insertPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 42);
//                arc.launch(intent);
            }
        });
        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
                PhotoData temp = new PhotoData(AlbumView.this);
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        openSlideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideshow();
            }
        });
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentPhoto = position;
                makeToast("The photo was selected!");
            }
        });

        movePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move();
                PhotoData temp = new PhotoData(AlbumView.this);
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

//    private void insert() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityIfNeeded(intent, 3);
//        makeToast("hey");
//        takePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                makeToast("zay");
//                ImageView image = new ImageView(AlbumView.this);
//                image.setImageURI(result);
//                Photo p = new Photo(result);
//                tempPhotos.add(p);
//                allPhotos.add(image);
//                currentAlbum.getList().add(p);
//            }
//        });
//        takePhoto.launch("image/*");
////        ArrayAdapter<ImageView> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allPhotos);
//        MyAdapter adapter = new MyAdapter(this, R.layout.imgview, allPhotos);
//
//        photos.setAdapter(adapter);
//    }
    private void delete() {
        if (currentPhoto == -1) {
            makeToast("No Photo selected!");
            return;
        }
        tempPhotos.remove(currentPhoto);
//        ArrayAdapter<ImageView> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allPhotos);
        MyAdapter adapter = new MyAdapter(this, R.layout.imgview, tempPhotos);
        photos.setAdapter(adapter);
        currentPhoto = - 1;
    }
    private void slideshow() {
        if (currentAlbum.getList().size() == 0) {
            makeToast("There are no photos in the album");
            return;
        }
        if (currentPhoto == -1) {
            makeToast("There is no selection!");
            return;
        }
        Slideshow.currentPos = currentPhoto;
        Slideshow.setCurrentAlbum(currentAlbum);
        currentPhoto = -1;
        Intent changePage = new Intent(AlbumView.this, Slideshow.class);
        startActivity(changePage);
    }
    private void move() {
        if (currentPhoto == -1) {
            makeToast("No Photo selected!");
            return;
        }
        String text = resultAlbum.getText().toString();
        if (text == null) {
            makeToast("No result Album inputted!");
            return;
        }
        Album a = null;
        for (int i = 0; i < MainActivity.listOfAlbums.size(); i++) {
            if (MainActivity.listOfAlbums.get(i).getName().equalsIgnoreCase(text)) {
                a = MainActivity.listOfAlbums.get(i);
                break;
            }
        }
        if (a == null) {
            makeToast("No album with that name to move to!");
            return;
        }
        a.getList().add(tempPhotos.get(currentPhoto));
        currentAlbum.getList().remove(currentPhoto);
//        allPhotos.remove(currentPhoto);
        MyAdapter adapter = new MyAdapter(this, R.layout.imgview, tempPhotos);
        photos.setAdapter(adapter);
    }
    private void makeToast(String text) {
        Toast toast = new Toast(this);
        toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selected_image = data.getData();
            for (int i = 0; i < currentAlbum.getList().size(); i++) {
                if (selected_image.equals(currentAlbum.getList().get(i).getUri())) {
                    makeToast("Photo already exists in the album!");
                    return;
                }
            }
            ImageView img = new ImageView(this);
            img.setImageURI(selected_image);
            Photo p = new Photo(selected_image);
            tempPhotos.add(p);
            MyAdapter adapter = new MyAdapter(this, R.layout.imgview, tempPhotos);
            photos.setAdapter(adapter);
            PhotoData temp = new PhotoData(AlbumView.this);
            temp.setAlbums(MainActivity.listOfAlbums);
            try {
                PhotoData.writeApp(temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected void onStop() {
        super.onStop();
        PhotoData temp = new PhotoData(AlbumView.this);
        temp.setAlbums(MainActivity.listOfAlbums);
        try {
            PhotoData.writeApp(temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        PhotoData temp = new PhotoData(AlbumView.this);
        temp.setAlbums(MainActivity.listOfAlbums);
        try {
            PhotoData.writeApp(temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
