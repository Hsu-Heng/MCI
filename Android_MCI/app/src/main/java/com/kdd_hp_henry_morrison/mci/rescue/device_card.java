package com.kdd_hp_henry_morrison.mci.rescue;

public class device_card {
     String DeviceId;
     int pk;
     int status;

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPk() {

        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }
}
