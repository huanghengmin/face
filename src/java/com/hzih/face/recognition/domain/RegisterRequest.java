package com.hzih.face.recognition.domain;


import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.Configuration;

/**
 * Created by Administrator on 15-7-24.
 */
public class RegisterRequest extends SipXml {

    private String province;
    private String city;
    private String organization;
    private String institution;
    private String place;
    private String address;
    private String ip;
    private int port;
    private double latitude;
    private double longitude;
    private String manager;
    private String mobile;
    private String telephone;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String toString(){
        return "<?xml version=\"1.0\"?>\r\n\r\n" +
                "<Register>\r\n" +
                "<DeviceType>" + deviceType + "</DeviceType>\r\n" +
                "<CmdType>" + cmdType + "</CmdType>\r\n" +
                "<DeviceID>" + deviceId + "</DeviceID>\r\n" +
                "<Province>" + province + "</Province>\r\n" +
                "<City>" + city + "</City>\r\n" +
                "<Organization>" + organization + "</Organization>\r\n" +
                "<Institution>" + institution + "</Institution>\r\n" +
                "<Place>" + place + "</Place>\r\n" +
                "<Address>" + address + "</Address>\r\n" +
                "<Latitude>" + latitude + "</Latitude>\r\n" +
                "<Longitude>" + longitude + "</Longitude>\r\n" +
                "<IP>" + ip + "</IP>\r\n" +
                "<Port>" + port + "</Port>\r\n" +
                "<Manager>" + manager + "</Manager>\r\n" +
                "<Mobile>" + manager + "</Mobile>\r\n" +
                "<Telephone>" + manager + "</Telephone>\r\n" +
                "</Register>";

    }

    public RegisterRequest xmlToBean(byte[] buff){
        Configuration config = new Configuration(buff);
        return config.getRegisterRequest();
    }
}
