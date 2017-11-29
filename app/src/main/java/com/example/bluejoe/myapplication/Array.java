package com.example.bluejoe.myapplication;

import android.telephony.gsm.GsmCellLocation;

import com.google.gson.Gson;

/**
 * Created by bluejoe on 2017/11/30.
 */

public class Array {
    class User{
        int age;
        User(int age,int age2){this.age = age;this.Yunzhe = new Yunzhe(age2);}
        Yunzhe Yunzhe;
    }
    class Yunzhe{
        int age;
        Yunzhe(int age){this.age = age;};
    }
    User gay = new User(1,2);
}
