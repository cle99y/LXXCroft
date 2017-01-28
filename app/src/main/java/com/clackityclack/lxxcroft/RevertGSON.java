package com.clackityclack.lxxcroft;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by PClegg on 27/01/2017.
 */

public class RevertGSON {

    String json;
    ArrayList<ParadeDetail> arraylist;
    Gson gson = new Gson();

    public void revertGSON(String param) {
        json = param;
        arraylist = gson.fromJson(json, ArrayList.class);
    }

    public ParadeDetail getParadeObject(int i) {

        return arraylist.get(i);
    }



}