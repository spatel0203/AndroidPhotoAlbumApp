package com.example.myapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;

public class PhotoData implements Serializable {
    public static final long serialVersionUID = 1L;
    private static Context c;
    /**
     * directory to store object
     */
    public static final String storeDir = "/Users/shaanpatel/Documents/Android08/app/src/main/java/com/example/myapplication";
    /**
     * fileString to store object
     */
    public static final String storeFile = "AdminView.dat";

    /**
     * hashmap to store users
     */
    public PhotoData() {
//        for (int i = 0; i < users.size(); i++) {
//            User u = users.get(i);
//            HashMap<Album, ArrayList<Photo>> temp = new HashMap<>();
//            for (int j = 0; j < u.getAlbums().size(); j++) {
//                Album a = u.getAlbums().get(j);
//                ArrayList<Photo> photos = new ArrayList<Photo>();
//                for (int z = 0; z < a.getList().size(); z++) {
//                    photos.add(a.getList().get(z));
//                }
//                temp.put(a, photos);
//            }
//            hm.put(u, temp);
//        }
    }
    public PhotoData(Context c){
        this.c=c;
    }

    /**
     * constructor with arraylist users
     * @param users users
     */

    /**
     * writeApp function
     * @param av photoData
     * @throws IOException error
     */
    public static void writeApp(PhotoData av) throws IOException {
        File file = new File(c.getFilesDir(), storeFile);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(av);
        oos.flush();
        oos.close();
    }

    /**
     * readApp function
     * @return photoData
     * @throws IOException error
     * @throws ClassNotFoundException error
     */
    public static PhotoData readApp() throws IOException, ClassNotFoundException {
        File file = new File(c.getFilesDir(), storeFile);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        PhotoData p = (PhotoData) ois.readObject();
        ois.close();
        return p;
    }
//    public void setUsers(ObservableList<User> Users) {
//
//    }

    /**
     * gets users
     * @return user arraylist
     */
    private ArrayList<Album> albums = new ArrayList<>();

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<String> getAlbumString(ArrayList<Album> albums) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < albums.size(); i++) {
            res.add(albums.get(i).getName());
        }
        return res;
    }
    //    public ArrayList<User> getUsers()
//    {
//        Set<User> s = hm.keySet();
//        Iterator<User> it = s.iterator();
//        ArrayList<User> res = new ArrayList<>();
//        while (it.hasNext())
//            res.add(it.next());
//        return res;
//    }
}
