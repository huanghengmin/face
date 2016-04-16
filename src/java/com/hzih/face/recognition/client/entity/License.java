package com.hzih.face.recognition.client.entity;

/**
 * Created by Administrator on 15-8-2.
 */
public class License {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<LICENSE>\r\n" +
                "<USERNAME>"+username+"</USERNAME>\r\n" +
                "<PASSWORD>"+password+"</PASSWORD>\r\n" +
                "</LICENSE>";
    }
}
