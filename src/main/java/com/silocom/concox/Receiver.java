/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silocom.concox;

import com.silocom.m2m.layer.physical.Connection;
import com.silocom.m2m.layer.physical.MessageListener;
import java.util.Arrays;

/**
 *
 * @author Sergio 2
 */
public class Receiver implements MessageListener {

    Connection con;
    private byte[] imeiExpected;
    private int timeout;
    private static final int login = 0x01;
    private static final int locationData = 0x12;
    private static final int statusInformation = 0x13;
    private static final int stringInformation = 0x15;
    private static final int alarmData = 0x16;

    public Receiver(Connection con, byte[] imeiExpected, int timeout) {
        this.con = con;
        this.imeiExpected = imeiExpected;
        this.timeout = timeout;
    }

    @Override
    public void receiveMessage(byte[] message) {

        //int mType = message[4] & 0xFF; // fourth byte corresponds to message type
        int mType = 0x01;
        switch (mType) {

            case login:

                // byte[] imeiReceived = Arrays.copyOfRange(message, 4, message.length - 6);
                byte[] imeiReceived = new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89,
                    (byte) 0x01, (byte) 0x23, (byte) 0x45};
                if (Arrays.equals(imeiReceived, imeiExpected)) {
                    //prepare message to accept
                    byte[] loginAccept = new byte[10];

                    loginAccept[0] = 0x78;  //start bit
                    loginAccept[1] = 0x78;  //start bit
                    loginAccept[2] = 0x05;  //message length
                    loginAccept[3] = message[4];  // protocol number   
                    loginAccept[4] = message[12]; //information serial number
                    loginAccept[5] = message[13]; //information serial number
                    loginAccept[3] = 0x01;  // protocol number   
                    loginAccept[4] = 0x00; //information serial number
                    loginAccept[5] = 0x01;
                    byte[] crc = CRC16.getCRCITU(Arrays.copyOfRange(loginAccept, 2, loginAccept.length - 4)); //from message length to information serial number
                    loginAccept[6] = crc[2];
                    loginAccept[7] = crc[3];
                    loginAccept[8] = 0x0D;  //stop bit
                    loginAccept[9] = 0x0A;  //stop bit
                    

                } else {

                    //if the IMEI aren't the same, do nothing
                }

                break;

            case locationData:

                break;

            case statusInformation:

                break;

            case stringInformation:

                break;

            case alarmData:

                break;

        }

    }

    @Override
    public void receiveMessage(byte[] message, Connection con) {

    }

}
