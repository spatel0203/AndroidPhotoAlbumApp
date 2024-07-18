package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    /**
     * list of photos
     */
    private ArrayList<Photo> list = new ArrayList<>();
    /**
     * length of album
     */
    private int length=0;
    /**
     * whether album has been set or not
     */
    private boolean set = false;
    /**
     * name of album
     */
    private String name;
    /**
     * range of album
     */
    private String range;
    /**
     * constructor of album
     */
    public Album() {
        this.name = "Default";
    }

    /**
     * sets name constructor
     * @param s name
     */
    public Album(String s) {
        this.name = s;
        this.length = 0;
        this.list=new ArrayList<>();
        setRange();
    }


    /**
     * constructor
     * @param list sets list
     * @param name sets name
     */
    public Album(ArrayList<Photo> list, String name) {
        this.list = list;
        this.length = list.size();
        this.name = name;
        setRange();
    }


    /**
     * sets list with arraylist
     * @param list list
     */
    public void setList(ArrayList<Photo> list) {
        this.list = list;
        this.length = list.size();
        this.set = true;
    }

    /**
     * gets list
     * @return returns list
     */
    public ArrayList<Photo> getList() {
        return list;
    }

    /**
     * inserts photo into album
     * @param p photo
     */
    public void insertPhoto(Photo p) {
        list.add(p);
        System.out.println(list.size());
//        this.getList().add(p);
        setList(list);
        length++;
        setRange();
    }

    /**
     * gets set
     * @return set
     */
    public boolean getSet() {
        return this.set;
    }

    /**
     * sets name of album
     * @param name album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets name of album
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * deletes photo
     * @param p photo
     * @return error
     */
    public Exception deletePhoto(Photo p) {
        try {
            list.remove(p);
        } catch (Exception e) {
            return e;
        }
        length--;
        setRange();
        this.setList(list);
        return null;
    }

    /**
     * adds caption
     * @param p photo
     * @param caption caption
     * @return error
     */
    public Exception addCaption(Photo p, String caption) {
        try {
            p.insertCaption(caption);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    /**
     * gets length of album
     * @return length
     */
    public int getLength(){
        return this.length;
    }

    /**
     * sets range of album
     */
    public void setRange(){
        if (list.size() == 0) {
            this.range = "No pictures!";
            return;
        }
        Photo max = list.get(0);
        Photo min = list.get(0);
        for(int i=0;i<list.size();i++){
            if(list.get(i).getTime().compareTo(max.getTime())>0){
                max=list.get(i);
            }
            if(list.get(i).getTime().compareTo(min.getTime())<0){
                min=list.get(i);
            }
        }
        String ret = min.printTime() + " to " + max.printTime();
        this.range = ret;

    }

    /**
     * gets range of album
     * @return range
     */
    public String getRange(){
        return range;
    }

    /**
     * toString of function
     * @return name
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
