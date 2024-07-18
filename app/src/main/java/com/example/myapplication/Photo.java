package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;

public class Photo implements Serializable {
    private LocalDateTime time;
    /**
     * image
     */
    private ImageView image;
    /**
     * tags of photo
     */
    private HashMap<String, ArrayList<String>> tags = new HashMap<>();
    /**
     * caption of photo
     */
    private String caption;

    private Bitmap bitmap;
    /**
     * photo file
     */
    private File f;
    // TODO: Add something that stores the photo location so it can load
    // private url location;

    private String uri;
    private int resource;

    public Photo(Uri i) {
        this.uri = i.toString();
        this.tags = new HashMap<>();
    }
//    public Photo(Uri i, int r) {
//        this.location = i;
//        this.resource=r;
//    }
//    public Photo(int r) {
//        this.resource = r;
//    }

    /**
     * photo constructor
     * @param f file
     * @param tags tags
     */
//    public Photo(File f, HashMap<String, ArrayList<String>> tags) {
//        long t = f.lastModified();
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//        time= LocalDateTime.parse(sdf.format(t));
//        this.tags = tags;
//    }
//    public Photo(Uri uri, Bitmap b) {
//        this.location = uri;
//        this.bitmap = b;
//    }

    /**
     * sets time
     * @param time time
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Bitmap getBitMap() {
        return this.bitmap;
    }

    /**
     * sets tags
     * @param tags tags
     */
    public void setTags(HashMap<String, ArrayList<String>> tags) {
        this.tags = tags;
    }

    /**
     * gets tags
     * @return tags
     */
    public HashMap<String, ArrayList<String>> getTags() {
        return this.tags;
    }

    /**
     * gets time
     * @return time
     */
    public LocalDateTime getTime() {
        return this.time;
    }

    /**
     * inserts tag
     * @param key keys
     * @param values values
     */
    public void insertTag(String key, ArrayList<String> values) {
        if (tags.containsKey(key)) {
            ArrayList<String> vals = tags.get(key);
            for(int i=0;i<vals.size();i++){
                if(vals.get(i).equals(values.get(0))){
                    return;
                }
            }
            vals.addAll(values);
            tags.put(key, vals);
        }
        else {
            tags.put(key, values);
        }
    }

    /**
     * inserts caption
     * @param caption caption
     */
    public void insertCaption(String caption) {
        this.caption = caption;
    }

    /**
     * tags string
     * @return tags
     */
    public String tagsString() {
        String s = "";
        Set<String> keys = this.getTags().keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            String s1 = iterator.next();
            for(int i=0;i<this.getTags().get(s1).size();i++){
                s+= s1 + "=" + this.getTags().get(s1).get(i) + " \n";
            }

        }
        return s;
    }

    /**
     * gets caption
     * @return caption
     */
    public String getCaption(){
        return caption;
    }

    /**
     * get Image function
     * @return image
     * @throws FileNotFoundException error
     */
    public ImageView getImage() throws FileNotFoundException {
        FileInputStream fi= new FileInputStream(f);
//        Image img = new Image(fi);
        return image;
    }

    /**
     * gets file
     * @return file
     */
    public File getFile() {
        return this.f;
    }

    /**
     * tostring
     * @return string
     */
    public String toString() { return caption; }

    /**
     * prints time
     * @return time
     */
    public String printTime(){
        String ret = time.getMonthValue() + "-" + time.getDayOfMonth() + "-" + time.getYear();
        return ret;
    }
    public ArrayList<String> populateArrayTags() {
        Set<String> keys = this.getTags().keySet();
        ArrayList<String> tags = new ArrayList<>();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            String s1 = iterator.next();
            for(int i=0;i<this.getTags().get(s1).size();i++){
                tags.add(s1 + "=" + this.getTags().get(s1).get(i));
            }
        }
        return tags;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }
    public int getResource() {
        return resource;
    }
}
