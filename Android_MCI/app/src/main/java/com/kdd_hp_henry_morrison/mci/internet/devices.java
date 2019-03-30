package com.kdd_hp_henry_morrison.mci.internet;

public class devices {
    String id = "";
    String pk = "";
    boolean upload = false;
    String sex = "";
    String name = "";
    String age = "";
    String perfusion = "";
    String respirations = "";
    int status = 1;
    public String getPerfusion() {
        return perfusion;
    }

    public void setPerfusion(String perfusion) {
        this.perfusion = perfusion;
    }



    public String getRespirations() {
        return respirations;
    }

    public void setRespirations(String respirations) {
        this.respirations = respirations;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }



    public String getSex() {
        return sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSex(String sex) {
        this.sex = sex;

    }

    public void setId(String id) {

        this.id = id;
    }






    public String getPk() {
        return pk;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
