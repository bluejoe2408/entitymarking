package com.example.bluejoe.myapplication;

/**
 * Created by Yunzhe on 2017/11/26.
 *
 */

class TextList {

    private String name;
    private String filename;

    TextList(String name, String filename) {
        this.name = name;
        this.filename = filename;
    }

    String getName() {
        return name;
    }

    String getFilename() {
        return filename;
    }
}
