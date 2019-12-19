package com.example.all4cars;

/**
 * Created by Hanzalah on 2/20/2019.
 */

public class AddServiceAttr {
    private String id;
    private String Image_url;
    private String CompanyName;
    private String Location;
    private String CloseTime;
    private String OpenTime;
    private String UserID;
    private String Service;
    private String Phone;
    private String Latitude;
    private String Longitude;

    public AddServiceAttr() {
    }

    public AddServiceAttr(String id, String image_url, String companyName, String location, String closeTime, String openTime, String userID, String service, String phone, String latitude, String longitude) {
        this.id = id;
        Image_url = image_url;
        CompanyName = companyName;
        Location = location;
        CloseTime = closeTime;
        OpenTime = openTime;
        UserID = userID;
        Service = service;
        Phone = phone;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return Image_url;
    }

    public void setImage_url(String image_url) {
        Image_url = image_url;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(String closeTime) {
        CloseTime = closeTime;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String openTime) {
        OpenTime = openTime;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
