package com.tiaonr.ws.software;

/**
 * Created by echyong on 12/9/15.
 */
public class SoftwareVersion {


    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    String deviceType;
    int versionCode;
    String versionName;
    String description;
    String location;

    public SoftwareVersion() {

    }

    public SoftwareVersion(String device) {
        this.deviceType = device;
        this.versionCode = 0;
        this.versionName = "0.0.0";
        this.description = "N/A";
        this.location = "";
    }
}
