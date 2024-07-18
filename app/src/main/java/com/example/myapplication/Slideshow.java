package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class Slideshow extends AppCompatActivity {

    private static Album currentAlbum;

    public static Album getCurrentAlbum() {
        return currentAlbum;
    }

    public static void setCurrentAlbum(Album currentAlbum) {
        Slideshow.currentAlbum = currentAlbum;
    }

    private Button next;
    private Button prev;
    private ImageView image;
    private ListView tagList;
    private TextView textView;
    private EditText tagArea;
    private Button addTag;
    private Button deleteTag;

    private ArrayList<String> tags;

    public static int currentPos;

    private String currentTagSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Populate t to an ImageView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshowview);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.previous);
        tagList = (ListView) findViewById(R.id.listView);
        image = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.textView3);
        tagArea = (EditText) findViewById(R.id.tagArea);
        addTag = (Button) findViewById(R.id.addTag);
        deleteTag = (Button) findViewById(R.id.deleteTag);
        image.setImageURI(currentAlbum.getList().get(currentPos).getUri());
        tags=currentAlbum.getList().get(currentPos).populateArrayTags();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,currentAlbum.getList().get(currentPos).populateArrayTags());
        tagList.setAdapter(adapter);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPos == (currentAlbum.getList().size() - 1)) {
                    makeToast("You are at the end of the album");
                    return;
                }
                currentPos++;
                image.setImageURI(currentAlbum.getList().get(currentPos).getUri());
                ArrayAdapter<String> tags = new ArrayAdapter<>(Slideshow.this, android.R.layout.simple_list_item_1, currentAlbum.getList().get(currentPos).populateArrayTags());
                tagList.setAdapter(tags);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPos == 0) {
                    makeToast("You are at the beginning of the album!");
                    return;
                }
                currentPos--;
                image.setImageURI(currentAlbum.getList().get(currentPos).getUri());
                ArrayAdapter<String> tags = new ArrayAdapter<>(Slideshow.this, android.R.layout.simple_list_item_1, currentAlbum.getList().get(currentPos).populateArrayTags());
                tagList.setAdapter(tags);
            }
        });
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagAdd();
                PhotoData temp = new PhotoData();
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagDelete();
                PhotoData temp = new PhotoData();
                temp.setAlbums(MainActivity.listOfAlbums);
                try {
                    PhotoData.writeApp(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentTagSelected = tags.get(position);
                makeToast(currentTagSelected + " was selected!");
            }
        });
    }
    private void tagAdd() {
        String text = tagArea.getText().toString();
        String[] split = text.split("=");
        if (split.length != 2) {
            makeToast("Invalid format entered for the tag. Note: You can only add one key-value pair at a time.");
            return;
        }
        if (!split[0].equalsIgnoreCase("person") && !split[0].equalsIgnoreCase("location")) {
            makeToast("The only acceptable tags are person and location!");
            return;
        }
        System.out.println(currentPos);
        Photo p = currentAlbum.getList().get(currentPos);
        System.out.println(p);
        ArrayList<String> vals = new ArrayList<>();
        vals.add(split[1].toLowerCase());
        ArrayList<String> curVals = p.getTags().get(split[0]);
        if (curVals == null) {
            ArrayList<String> ts = new ArrayList<>();
            ts.add(split[1].toLowerCase());
            p.getTags().put(split[0], ts);
            tags = p.populateArrayTags();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tags);
            tagList.setAdapter(adapter);
            return;
        }
        for (int i = 0; i < curVals.size(); i++) {
            if (curVals.get(i).equalsIgnoreCase(split[1])) {
                makeToast("Key-Value pair already exists!");
                return;
            }
        }
        vals.addAll(curVals);
        p.getTags().put(split[0], vals);
        tags = p.populateArrayTags();
        System.out.println(tags.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tags);
        tagList.setAdapter(adapter);
    }
    private void tagDelete() {
        if (currentTagSelected == null || currentTagSelected=="") {
            makeToast("Nothing was selected!");
            return;
        }
        System.out.println(currentTagSelected);
        String[] split = currentTagSelected.split("=");
        ArrayList<String> vals = currentAlbum.getList().get(currentPos).getTags().get(split[0]);
        if (vals.size() > 1) {
            for (int i = 0; i < vals.size(); i++) {
                if (vals.get(i).equalsIgnoreCase(split[1])) {
                    vals.remove(i);
                    currentAlbum.getList().get(currentPos).getTags().replace(split[0], vals);
                    break;
                }
            }
        }
        else {
            currentAlbum.getList().get(currentPos).getTags().remove(split[0]);
        }
        tags = currentAlbum.getList().get(currentPos).populateArrayTags();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tags);
        tagList.setAdapter(adapter);
        makeToast("The tag was deleted!");
        currentTagSelected = "";
    }

    private void makeToast(String text) {
        Toast toast = new Toast(this);
        toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return;
    }
    protected void onStop() {
        super.onStop();
        PhotoData temp = new PhotoData(Slideshow.this);
        temp.setAlbums(MainActivity.listOfAlbums);
        try {
            PhotoData.writeApp(temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
