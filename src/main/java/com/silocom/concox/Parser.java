package com.silocom.concox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Parser {

    public static ConcoxReport heartBeatParser(byte[] message) {
        ConcoxReport report = new ConcoxReport();
        int index = 0;

        int TIC = message[index] & 0xFF;
        index++;
        int voltajeLevelInteger = ((message[index] & 0xFF) << 8) | (message[index + 1] & 0xFF);
        
        int voltagePercent = Utils.voltagePercentual(voltajeLevelInteger);
        index++;
        int gsmSignalStrength = message[index + 1] & 0xFF;
        report.setTIC(TIC);
        report.setVoltajeLevelInteger(voltajeLevelInteger);
        report.setGsmSignalStrength(gsmSignalStrength);
        report.setVoltagePercent(voltagePercent);
        return report;
    }

    public static ConcoxReport gpsDataParser(byte[] message) {

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

    public static ConcoxReport lbsAlarmParser(byte[] message) {
        ConcoxReport report = gpsDataParser(message);

        return report;
    }

    public static ConcoxReport onlineCommandResponse(byte[] commandData) {
        ConcoxReport answer = new ConcoxReport();

        //String test = "Current Position!Lat: N22.57705,Lon: E113.91664,Course: 355,Speed: 2Km/h,DateTime: 2020-04-23 11: 00: 48";
        String toDecode = new String(commandData);
        String lat = "";
        String lon = "";

        String dateTime = "";
        String replaced = toDecode.replaceAll("(Current Position!|Km/h)", "");
        String[] parts = replaced.split(",");

        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        String part4 = parts[3];
        String part5 = parts[4];

        if (part1.charAt(2) == 't') { //Latitude
            lat = part1.replaceAll("Lat: ", "");

            if (lat.charAt(0) == 'S') {  //is South?
                answer.setLatitude(Float.parseFloat(lat.replaceAll("[aA-zZ]", "")) * -1);  //set negative latitude
            } else {
                answer.setLatitude(Float.parseFloat(lat.replaceAll("[aA-zZ]", "")));    //set positive latitude
            }
        }

        if (part2.charAt(2) == 'n') { //Longitude
            lon = part2.replaceAll("Lon: ", "");

            if (lon.charAt(0) == 'W') {  //is West?
                answer.setLongitude(Float.parseFloat(lon.replaceAll("[aA-zZ]", "")) * -1);  //set negative longitude
            } else {
                answer.setLatitude(Float.parseFloat(lon.replaceAll("[aA-zZ]", "")));    //set positive longitude
            }
        }

        if (part3.charAt(2) == 'u') { //Course
            answer.setCourse(Integer.parseInt(part3.replaceAll("Course: ", "")));
        }

        if (part4.charAt(2) == 'e') { //Speed
            answer.setSpeed(Integer.parseInt(part4.replaceAll("Speed: ", "")));
        }

        if (part5.charAt(2) == 't') { //DateTime
            dateTime = part5.replaceAll("DateTime: ", "");
        }

        try {
            if (!dateTime.isEmpty()) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = format.parse(dateTime);
                answer.setDate(date);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

}
