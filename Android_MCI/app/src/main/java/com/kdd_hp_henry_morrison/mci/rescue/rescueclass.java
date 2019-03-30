package com.kdd_hp_henry_morrison.mci.rescue;
import com.google.android.gms.maps.model.LatLng;
public class rescueclass {
    String name;
    String commander;
    LatLng latLng;
    String timestart;
    String timestop;
    int pk;

    public String getCommander() {
        return commander;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getTimestop() {
        return timestop;
    }

    public void setTimestop(String timestop) {
        this.timestop = timestop;
    }

    public LatLng getLatLng() {
        return latLng;

    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setCommander(String commander) {

        this.commander = commander;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
