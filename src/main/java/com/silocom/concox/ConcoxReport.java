/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silocom.concox;

import java.util.Date;

/**
 *
 * @author Sergio 2
 */
public class ConcoxReport {

    private Date date;
    private double latitude;
    private double longitude;
    private int satInUse;
    private int speed;
    private int course;
    private Integer TIC = null;
    private int voltajeLevel;
    private Integer voltajeLevelInteger  = null;
    private int gsmSignalStrength;
    private int alarm_Languaje;
    private int infoSerialNumber;
    private int voltagePercent;
    

    public ConcoxReport() {
    }

    public ConcoxReport(Date date, int satInUse, double latitude, double longitude, int speed, int course,
            int TIC, int voltajeLevel, int gsmSignalStrength, int alarm_Languaje, int infoSerialNumber, int voltajeLevelInteger,
            int voltagePercent) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.satInUse = satInUse;
        this.speed = speed;
        this.course = course;
        this.TIC = TIC;
        this.voltajeLevel = voltajeLevel;
        this.gsmSignalStrength = gsmSignalStrength;
        this.alarm_Languaje = alarm_Languaje;
        this.infoSerialNumber = infoSerialNumber;
        this.voltajeLevelInteger = voltajeLevelInteger;
        this.voltagePercent = voltagePercent;   
    }

    public int getVoltagePercent() {
        return voltagePercent;
    }

    
    public Integer getVoltajeLevelInteger() {
        return voltajeLevelInteger;
    }
 
    public int getInfoSerialNumber() {
        return infoSerialNumber;
    }

    public Date getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getSatInUse() {
        return satInUse;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCourse() {
        return course;
    }

    public Integer getTIC() {
        return TIC;
    }

    public int getVoltajeLevel() {
        return voltajeLevel;
    }

    public int getGsmSignalStrength() {
        return gsmSignalStrength;
    }

    public int getAlarm_Languaje() {
        return alarm_Languaje;
    }

    public void setTIC(int TIC) {
        this.TIC = TIC;
    }

    public void setVoltajeLevel(int voltajeLevel) {
        this.voltajeLevel = voltajeLevel;
    }

    public void setGsmSignalStrength(int gsmSignalStrength) {
        this.gsmSignalStrength = gsmSignalStrength;
    }

    public void setAlarm_Languaje(int alarm_Languaje) {
        this.alarm_Languaje = alarm_Languaje;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSatInUse(int satInUse) {
        this.satInUse = satInUse;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public void setInfoSerialNumber(int infoSerialNumber) {
        this.infoSerialNumber = infoSerialNumber;
    }

    public void setVoltajeLevelInteger(int voltajeLevelInteger) {
        this.voltajeLevelInteger = voltajeLevelInteger;
    }

    public void setVoltagePercent(int voltagePercent) {
        this.voltagePercent = voltagePercent;
    }
    
    

}
