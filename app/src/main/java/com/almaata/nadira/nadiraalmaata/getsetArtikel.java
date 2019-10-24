package com.almaata.nadira.nadiraalmaata;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class getsetArtikel {
    private String judul, sumber, isi;

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

}