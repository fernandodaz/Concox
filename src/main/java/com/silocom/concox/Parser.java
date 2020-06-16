package com.silocom.concox;

import java.util.Date;

public class Parser {

    public static ConcoxReport gpsDataParser(byte[] message) {

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
        Date date = Utils.dateTime(dateTime);
        System.out.println("Date " + date.toString());

        int satInUse = message[6] & 0x0F;

        byte[] latitude = new byte[4];
        for (int i = 0, j = 7; (i < latitude.length); i++, j++) {
            latitude[i] = message[j];
        }
        double lat = Utils.latitude(latitude);

        byte[] longitude = new byte[4];
        for (int i = 0, j = 11; (i < latitude.length); i++, j++) {
            longitude[i] = message[j];
        }
        double lon = Utils.longitude(longitude);

        int speed = message[15] & 0xFF;
        boolean isWestLong = (message[16] & 0x08) > 0;
        boolean isSouthLat = (message[16] & 0x04) == 0;

        int course = ((message[16] & 0x03) << 8) | (message[17] & 0xFF);

        if (isSouthLat) {
            lat = lat * -1;
        }
        if (isWestLong) {
            lon = lon * -1;
        }
        System.out.println("course " + course);
        System.out.println("lon " + lon / 1800000);
        System.out.println("lat " + lat / 1800000);

        ConcoxReport report = new ConcoxReport();

        report.setDate(date);
        report.setLatitude(lat / 18);
        report.setLongitude(lon / 18);
        report.setSatInUse(satInUse);
        report.setSpeed(speed);
        report.setCourse(course);

        return report;
    }

    public static ConcoxReport alarmDataParser(byte[] message) {
        ConcoxReport report = gpsDataParser(message);
        int TIC = message[message.length - 5] & 0xFF; //Terminal information content
        int voltageLevel = message[message.length - 4] & 0xFF;
        int gsmSignalStrength = message[message.length - 3] & 0xFF;
        int alarm = ((message[message.length - 2] & 0xFF));

        System.out.println("alarm " + alarm);
        System.out.println("voltageLevel " + voltageLevel);
        System.out.println("gsmSignalStrength " + gsmSignalStrength);
        report.setTIC(TIC);
        report.setVoltajeLevel(voltageLevel);
        report.setGsmSignalStrength(gsmSignalStrength);
        report.setAlarm_Languaje(alarm);

        return report;
    }
    
    
    public static ConcoxReport lbsAlarmParser(byte[] message){
    ConcoxReport report = gpsDataParser(message);
    
    
    
    return report;
    }

    public static ConcoxReport onlineCommandResponse(byte[] commandData) {
        ConcoxReport answer = new ConcoxReport();

        //Necesito hacer pruebas
        return answer;
    }

}
