/*
 * @Author Fernando Gonzalez.
 */
package com.silocom.concox;


import java.util.Date;


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


    public static Date timeCalc(byte[] timeStamp) {

        return Utils.convert(Utils.bytesToHex(timeStamp));
    }

    public static Date convert(String inHexString) {

        Date dateResult = new Date(Long.parseLong(inHexString, 16));

        return dateResult;
    }   //usar calendar

    public static double longitude(byte[] lon) {

        return (int) Long.parseLong(Utils.bytesToHex(lon), 16);

    }

    public static double latitude(byte[] lat) {

        return (int) Long.parseLong(Utils.bytesToHex(lat), 16);
    }

}
