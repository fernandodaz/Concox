/*
 * @Author Fernando Gonzalez.
 */
package com.silocom.concox;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silocom01
 */
public class Utils {

    public static String byteToString(byte[] message) {
        long value = 0;

        for (byte msg : message) {
            value = (value << 8) | (msg & 0xFF);
        }
        return Long.toString(value);
    }

    public static String hexToString(byte[] message) {
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < message.length; i++) {

            if ((message[i] & 0xff) < 0x10) {
                answer.append("0");
            }

            answer.append(Integer.toHexString(message[i] & 0xFF));

        }
        return answer.toString();
    }

    public static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] stringToHex(String message) {

        if (message.length() % 2 == 0) {
            byte[] msg = new byte[message.length() / 2];
            for (int i = 0; i < message.length() / 2; i++) {
                msg[i] = (byte) (Integer.parseInt(message.substring(i * 2, (i * 2) + 2), 16));
            }
            return msg;
        }
        return null;
    }

    public static byte[] concatByteArray(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static Date dateTime(byte[] dateTime) {
        Calendar cal = Calendar.getInstance();

        int year = (dateTime[0] & 0xFF) + 2000;
        int month = (dateTime[1] & 0xFF) - 1; //Jan = 0, feb = 1, and so on
        int day = dateTime[2] & 0xFF;
        int hour = dateTime[3] & 0xFF;
        int min = dateTime[4] & 0xFF;
        int sec = dateTime[5] & 0xFF;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        //cal.set(Calendar.HOUR, hour); //formato 12 hrs
        cal.set(Calendar.HOUR_OF_DAY, hour); //formato 24Hrs
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

    public static double longitude(byte[] lon) {

        //  return (int) Long.parseLong(Utils.bytesToHex(lon), 16);
        return ByteBuffer.wrap(lon).getInt();
    }

    public static double latitude(byte[] lat) {

        //  return (int) Long.parseLong(Utils.bytesToHex(lat), 16);
        return ByteBuffer.wrap(lat).getInt();
    }

    public static int voltagePercentual(int voltageLevelInteger) {
        int voltagePercent = 0;
        float voltageLevelFloat = (float) (voltageLevelInteger / 100.00);

        if (voltageLevelFloat >= 4.13) {
            voltagePercent = 100;
        } else if (voltageLevelFloat >= 4.08) {
            voltagePercent = 90;
        } else if (voltageLevelFloat >= 4) {
            voltagePercent = 80;
        } else if (voltageLevelFloat >= 3.91) {
            voltagePercent = 70;
        } else if (voltageLevelFloat >= 3.87) {
            voltagePercent = 60;
        } else if (voltageLevelFloat >= 3.81) {
            voltagePercent = 50;
        } else if (voltageLevelFloat >= 3.78) {
            voltagePercent = 40;
        } else if (voltageLevelFloat >= 3.75) {
            voltagePercent = 30;
        } else if (voltageLevelFloat >= 3.73) {
            voltagePercent = 20;
        } else if (voltageLevelFloat >= 3.7) {
            voltagePercent = 10;
        } else if (voltageLevelFloat >= 3.60) {
            voltagePercent = 5;
        } else{
            voltagePercent = 0;
        }
        return voltagePercent;
    }

    public static int indexOf(byte[] data, byte[] pattern) {
        int[] failure = computeFailure(pattern);

        int j = 0;

        for (int i = 0; i < data.length; i++) {
            while (j > 0 && pattern[j] != data[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == data[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }

}
