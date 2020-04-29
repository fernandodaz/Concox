package com.silocom.concox;

import java.util.Date;

public class Parser {

    public static ConcoxReport locationDataParser(byte[] message) {

        /*
                calcular tiempo 0-5 6 bytes
                num de sat 6 1 byte
                lat 7 - 10 4 bytes
                lon 11 -14 4 bytes
                velocidad 15 1 byte
                curso, status 16-17 2 bytes
         */
        byte[] dateTime = new byte[6];
        System.arraycopy(message, 0, dateTime, 0, dateTime.length);
        Date date = Utils.timeCalc(dateTime);

        int satInUse = message[6] & 0x0F;

        byte[] latitude = new byte[4];
        for (int i = 0, j = 7; (i < latitude.length) && (j < latitude.length); i++, j++) {
            latitude[i] = message[j];
        }
        double lat = Utils.latitude(latitude);

        byte[] longitude = new byte[4];
        for (int i = 0, j = 11; (i < latitude.length) && (j < latitude.length); i++, j++) {
            longitude[i] = message[j];
        }
        double lon = Utils.longitude(longitude);

        int speed = message[15] & 0xFF;

        int course = ((message[16] & 0xFF) << 8) | (message[17] & 0xFF);

        ConcoxReport report = new ConcoxReport();

        report.setDate(date);
        report.setLatitude(lat);
        report.setLongitude(lon);
        report.setSatInUse(satInUse);
        report.setSpeed(speed);
        report.setCourse(course);

        return report;
    }

    public static ConcoxReport alarmDataParser(byte[] message) {
        ConcoxReport report = locationDataParser(message);
        int TIC = message[26] & 0xFF; //Terminal information content
        int voltageLevel = message[27] & 0xFF;
        int gsmSignalStrength = message[28] & 0xFF;
        int alarm_Languaje = ((message[29] & 0xFF) << 8) | (message[30] & 0xFF);
        
        report.setTIC(TIC);
        report.setVoltajeLevel(voltageLevel);
        report.setGsmSignalStrength(gsmSignalStrength);
        report.setAlarm_Languaje(alarm_Languaje);

        return report;
    }

}
