package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Search extends AppCompatActivity {

    private Button search;
    private ListView photos;
    private TextView textView;
    private EditText tags;

    private ArrayList<ImageView> imagePhotos;

    private ArrayList<String> possibleSelections;

    private ArrayList<Photo> resPhotos = new ArrayList<>();

    private ListView resultStrings;

    private Button click;

    private CheckBox location;

    private CheckBox person;

    private String selection = "";

    private boolean whichone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);
        search = (Button) findViewById(R.id.searchByTag);
        photos = (ListView) findViewById(R.id.resultPhotos);
        textView = (TextView) findViewById(R.id.textView4);
        tags = (EditText) findViewById(R.id.tagInsert);
        resultStrings = (ListView) findViewById(R.id.resultOptions);
        click = (Button) findViewById(R.id.chooseSelection);
        location = (CheckBox) findViewById(R.id.location);
        person = (CheckBox) findViewById(R.id.person);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resPhotos != null)
                    resPhotos.clear();
                search();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location.isSelected())
                    location.setSelected(false);
                else
                    location.setSelected(true);
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (person.isSelected())
                    person.setSelected(false);
                else
                    person.setSelected(true);
            }
        });
        resultStrings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selection = possibleSelections.get(position);
            }
        });
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                makeToast("The photo was selected!");
            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
//                possibleSelections.clear();
//                selection="";
//                ArrayAdapter<String> apapter = new ArrayAdapter<>(Search.this, android.R.layout.simple_list_item_1,possibleSelections);
//                resultStrings.setAdapter(apapter);
            }
        });
    }
    private void makeToast(String text) {
        Toast toast = new Toast(this);
        toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return;
    }
    private void search() {
        String text = tags.getText().toString();
        if (text == null) {
            makeToast("Please enter a tag to search by!");
            return;
        }
        if (!location.isSelected() && !person.isSelected()) {
            makeToast("Please select either location or person!");
            return;
        }
        if (location.isSelected() && person.isSelected()) {
            makeToast("Only select ONE of person and location");
            return;
        }
        String yo = "";
        if (location.isSelected()) {
            yo = "location";
            whichone = true;
        }
        else {
            yo = "person";
            whichone = false;
        }
        HashMap<String, String> possibleVals = new HashMap<>();
        possibleSelections = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < MainActivity.listOfAlbums.size(); i++) {
            Album a = MainActivity.listOfAlbums.get(i);
            ArrayList<Photo> ps = a.getList();
            for (int j = 0; j < ps.size(); j++) {
                ArrayList<String> values = ps.get(j).getTags().get(yo);
                for (int z = 0; z < values.size(); z++) {
                    if (values.get(z).substring(0, length).equalsIgnoreCase(text)) {
                        if (!possibleVals.containsKey(values.get(z))) {
                            possibleVals.put(values.get(z), "");
                            possibleSelections.add(values.get(z));
                        }
                        resPhotos.add(ps.get(j));
                    }
                }
            }
        }
        for (int i = 0; i < resPhotos.size(); i++) {
            ImageView image = new ImageView(this);
            image.setImageURI(resPhotos.get(i).getUri());
        }
        MyAdapter adapter = new MyAdapter(this, R.layout.imgview, resPhotos);
        photos.setAdapter(adapter);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, possibleSelections);
        resultStrings.setAdapter(adapt);
    }
    private void select() {
        if (selection == "") {
            makeToast("Nothing selected! Please select from the listView to further search.");
            return;
        }
        String yoyo = "";
        if (whichone)
            yoyo = "location";
        else
            yoyo = "person";
        ArrayList<Photo> filtered = new ArrayList<>();
        for (int i = 0; i < possibleSelections.size(); i++) {
            if (selection.equals(possibleSelections.get(i))) {
                for (int j = 0; j < resPhotos.size(); j++) {
                    ArrayList<String> vals = resPhotos.get(j).getTags().get(yoyo);
                    for (int z = 0; z < vals.size(); z++) {
                        if (vals.get(z).equals(selection))
                            filtered.add(resPhotos.get(j));
                    }
                }
                break;
            }
        }
        selection = "";
        MyAdapter adapter = new MyAdapter(this, R.layout.imgview, filtered);
        photos.setAdapter(adapter);
    }
}
