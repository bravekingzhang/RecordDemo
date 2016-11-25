package com.example.hoollyzhang.recorddemo;

/**
 * Created by hoollyzhang on 16/11/22.
 */

public class SimpleObject {



    public SimpleObject(String text) {
        this.text = text;
    }

    public String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
