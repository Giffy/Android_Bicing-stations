package com.mideann.giffy.estacionsbicis;

import android.app.Application;

import java.util.ArrayList;

public class MyApp extends Application {
    private ArrayList<Estacio> llistaEstacions;

    public ArrayList<Estacio> getEstacions() {
        return llistaEstacions;
    }

    public void setEstacions(ArrayList<Estacio> llistaEstacions) {
        this.llistaEstacions = llistaEstacions;
    }
}