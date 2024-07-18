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

import java.io.IOException;
import java.util.ArrayList;

public class UserView extends AppCompatActivity {
    private Button searchButton;
    private Button openAlbum;
    private Button renameAlbum;
    private Button deleteAlbum;

    private Button createAlbum;

    private ListView albums;

    public static ArrayList<String> allAlbums = new ArrayList<>();

    public static ArrayList<Album> listOfAlbums = new ArrayList<>();

    private String albumName;

    private EditText albumChange;

    Context c = this;

    private String currentAlbumSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = (Button) findViewById(R.id.searchButton);
        deleteAlbum = (Button) findViewById(R.id.deleteAlbum);
        openAlbum = (Button) findViewById(R.id.openAlbum);
        renameAlbum = (Button) findViewById(R.id.renameAlbum);
        createAlbum = (Button) findViewById(R.id.createAlbum);
        albums = (ListView) findViewById(R.id.albums);
        albumChange = (EditText) findViewById(R.id.albumName);
        albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentAlbumSelected = allAlbums.get(position);
                return;
            }
        });
        deleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Remove the selected Album from the listView
                delete();
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
            }
        });

    }

    /**
     * action event handler
     * @param e action event
     * @throws IOException error
     */
//    private void actEvent(ActionEvent e) throws IOException {
//        ArrayList<Album> p = new ArrayList<Album>(albums.getItems());
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setHeaderText("Please Confirm your selection.");
//        alert.showAndWait();
//        ButtonType b1 = alert.getResult();
//        if (!b1.getText().equals("OK")) {
//            alert.close();
//            return;
//        }
//        alert.close();
//        Button b = (Button) e.getSource();
//        if (b == createAlbum)
//            create();
//        if (b == deleteAlbum)
//            delete();
//        if (b == renameAlbum)
//            rename();
//        if (b == logOut)
//            logOff();
//        if (b == openAlbum)
//            open();
//        if (b == searchPhoto)
//            search();
//        PhotoData.writeApp(new PhotoData(AdminView.getUsers()));
//    }

    /**
     * create function goes into album view to make album
     * @throws IOException error
     */
    private void create() {
        // TODO: Go to separate screen that allows the users to import photos to add to the album
        // TODO: Allow them add a name to the album
        albumName = albumChange.getText().toString();
        if (albumName == "") {
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAlbums);
        albums.setAdapter(adapter);
    }

    /**
     * delete album
     * @throws IOException error
     */
    private void delete(){
        Album a = null;
        for (int i = 0; i < listOfAlbums.size(); i++) {
            if (currentAlbumSelected.equals(listOfAlbums.get(i))) {
                a = listOfAlbums.get(i);
            }
        }
        ArrayList<String> list = allAlbums;
        if (a == null) {
            noItemSelected();
            return;
        }
        list.remove(a);
        allAlbums = list;
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
        if (albumName == "") {
            makeToast("Please enter a name before selecting create!");
            return;
        }
        Album a = null;
        for (int i = 0; i < listOfAlbums.size(); i++) {
            if (currentAlbumSelected.equals(listOfAlbums.get(i))) {
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
            if (currentAlbumSelected.equals(listOfAlbums.get(i))) {
                a = listOfAlbums.get(i);
            }
        }
        if (a == null) {
            noItemSelected();
            return;
        }
        AlbumView.currentAlbum = a;
        Intent changePage = new Intent(this, AlbumView.class);
        startActivity(changePage);
        return;
    }

    /**
     * search function
     * @throws IOException error
     */
    private void search() {
        Intent changePage = new Intent(UserView.this, Search.class);
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
        toast.makeText(this, text, Toast.LENGTH_LONG).show();
        return;
    }
}
