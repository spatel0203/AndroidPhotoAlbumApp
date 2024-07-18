package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button searchButton;
    private Button openAlbum;
    private Button renameAlbum;
    private Button deleteAlbum;

    private Button createAlbum;

    private ListView albums;

    public static ArrayList<String> allAlbums=new ArrayList<>();

    public static ArrayList<Album> listOfAlbums=new ArrayList<>();

    private String currentAlbumSelected = "";

    private String albumName;

    private EditText albumChange;

    Context c = this;
    PhotoData p = new PhotoData(MainActivity.this);

    private boolean loader=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //File f = new File("AdminView.dat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = (Button) findViewById(R.id.searchButton);
        deleteAlbum = (Button) findViewById(R.id.deleteAlbum);
        openAlbum = (Button) findViewById(R.id.openAlbum);
        renameAlbum = (Button) findViewById(R.id.renameAlbum);
        createAlbum = (Button) findViewById(R.id.createAlbum);
        albums = (ListView) findViewById(R.id.albums);
        albumChange = (EditText) findViewById(R.id.albumName);
        if(loader){
            File t = new File("/data/user/0/com.example.myapplication/files/AdminView.dat");
            if (t.exists()) {
                try {
                    p = PhotoData.readApp();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                ArrayList<Album> ps = p.getAlbums();
                listOfAlbums=ps;
                allAlbums=p.getAlbumString(listOfAlbums);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAlbums);
                albums.setAdapter(adapter);
            }
            else {
                PhotoData temp = new PhotoData(MainActivity.this);
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentAlbumSelected = allAlbums.get(position);
                makeToast("selected = " + currentAlbumSelected);
            }
        });

        deleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hey");
                delete();
                PhotoData temp = new PhotoData(MainActivity.this);
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });
        createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
                PhotoData temp = new PhotoData(MainActivity.this);
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        renameAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rename();
                PhotoData temp = new PhotoData(MainActivity.this);
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    /**
     * create function goes into album view to make album
     * @throws IOException error
     */
    private void create() {
        // TODO: Go to separate screen that allows the users to import photos to add to the album
        // TODO: Allow them add a name to the album
        System.out.println("hey");
        albumName = albumChange.getText().toString();
        if (albumName.equals("")) {
            makeToast("Please enter a name before selecting create!");
            return;
        }
        for (int i = 0; i < allAlbums.size(); i++) {
            if (allAlbums.get(i).equals(albumName)) {
                makeToast("There already is an album with this name!");
                return;
            }
        }
        Album a = new Album(albumName);
        listOfAlbums.add(a);
        allAlbums.add(a.getName());
        p.setAlbums(listOfAlbums);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAlbums);
        albums.setAdapter(adapter);
    }

    /**
     * delete album
     * @throws IOException error
     */
    private void delete(){
        Album a = null;
//        System.out.println(listOfAlbums.size());
        int pos = 0;
        for (int i = 0; i < allAlbums.size(); i++) {
            if (currentAlbumSelected.equals(allAlbums.get(i))) {
                a = listOfAlbums.get(i);
                pos = i;
            }
        }
        ArrayList<String> list = allAlbums;
        if (a == null) {
            noItemSelected();
            return;
        }
        list.remove(a.getName());
        allAlbums = list;
        listOfAlbums.remove(pos);
        p.setAlbums(listOfAlbums);
        makeToast(currentAlbumSelected + " was deleted!");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAlbums);
        albums.setAdapter(adapter);
        return;
    }

    /**
     * rename album
     * @throws IOException error
     */
    private void rename() {
        albumName = albumChange.getText().toString();
        if (albumName.equals("")) {
            makeToast("Please enter a name before selecting rename!");
            return;
        }
        Album a = null;
        for (int i = 0; i < listOfAlbums.size(); i++) {
            if (currentAlbumSelected.equals(allAlbums.get(i))) {
                a = listOfAlbums.get(i);
            }
        }
        if (a == null) {
            noItemSelected();
            return;
        }
        for (int i = 0; i < allAlbums.size(); i++) {
            if (allAlbums.get(i).equals(albumName)) {
                makeToast("There already is an album with this name!");
                return;
            }
        }
        a.setName(albumName);
        p.setAlbums(listOfAlbums);
        for(int i=0;i<allAlbums.size();i++){
            if(allAlbums.get(i).equals(currentAlbumSelected)){
                allAlbums.set(i,albumName);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAlbums);
        albums.setAdapter(adapter);
    }


    /**
     * open function
     * @throws IOException error
     */
    private void open() {
        Album a = null;
        for (int i = 0; i < listOfAlbums.size(); i++) {
            if (currentAlbumSelected.equals(allAlbums.get(i))) {
                a = listOfAlbums.get(i);
            }
        }
        if (a == null) {
            noItemSelected();
            return;
        }
        AlbumView.currentAlbum = a;
        Intent changePage = new Intent(MainActivity.this, AlbumView.class);
        startActivity(changePage);
        return;
    }

    /**
     * search function
     * @throws IOException error
     */
    private void search() {
        Intent changePage = new Intent(this, Search.class);
        startActivity(changePage);
        return;
    }

    /**
     * show function
     * Displays the details of the album that is selected
     */
//    @FXML
//    private void show() {
//        Album a = albums.getSelectionModel().getSelectedItem();
//        if (a == null)
//            return;
//        String s = "";
//        s += "Name: " + a.getName() + "\n" + "Number Of Photos: " + a.getLength() + "\n";
//        s += "Range of dates: " + a.getRange();
//        display.setText(s);
//    }

    private void noItemSelected() {
        Toast toast = new Toast(this);
        toast.makeText(this, "No Album was selected!", Toast.LENGTH_LONG).show();
        return;
    }
    private void makeToast(String text) {
        Toast toast = new Toast(this);
        toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return;
    }
    protected void onStop() {
        super.onStop();
        PhotoData temp = new PhotoData(MainActivity.this);
        temp.setAlbums(MainActivity.listOfAlbums);
        try {
            PhotoData.writeApp(temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
