package com.example.myapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MyAdapter extends ArrayAdapter<Photo> {

    private Context c;
    private int resource;
    private ArrayList<Photo> objects = new ArrayList<>();

    public MyAdapter(Context context, int resource, ArrayList<Photo> objects) {
        super(context, resource, objects);
        this.c=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View temp;
        if (convertView == null) {
            temp = new View(c);
            temp = li.inflate(R.layout.imgview, null);
            ImageView img = (ImageView) temp.findViewById(R.id.selected_item);
            img.setImageURI(objects.get(position).getUri());
            return temp;
        } else {
            return convertView;
        }
//
//        convertView = li.inflate(resource, parent, false);
//        ImageView image = convertView.findViewById(R.id.selected_item);
//        System.out.print(R.id.selected_item + " gash");
//        Photo i = getItem(position);
//        image.setImageBitmap(i.getBitMap());
//        return convertView;
    }
}
