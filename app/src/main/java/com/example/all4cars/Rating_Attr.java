package com.example.all4cars;

/**
 * Created by Hanzalah on 3/2/2019.
 */

public class Rating_Attr {
    private String Id ;
    private String ServiceId;
    private String UserId;
    private Float Total;
    private String Comment;


    public Rating_Attr() {
    }

    public Rating_Attr(String id, String serviceId, String userId, Float total, String comment) {
        Id = id;
        ServiceId = serviceId;
        UserId = userId;
        Total = total;
        Comment = comment;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        Total = total;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
