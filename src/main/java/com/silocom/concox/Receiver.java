/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silocom.concox;

import com.silocom.m2m.layer.physical.Connection;
import com.silocom.m2m.layer.physical.MessageListener;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Sergio 2
 */
public class Receiver implements MessageListener {

    Connection con;
    private byte[] imeiExpected;

    private byte[] start = new byte[]{0x78, 0x78};
    private byte[] stop = new byte[]{0x0D, 0x0A};
    private byte[] isPacketLen1byte = new byte[]{0x78};
    private byte[] isPacketLen2byte = new byte[]{0x79};

    private long timeout;
    private static final int login = 0x01;
    private static final int lbsAlarm = 0x19;
    private static final int onlineCommandResponse = 0x21;
    private static final int gpsPositioningData = 0x22;
    private static final int heartBeat = 0x23;
    private static final int alarmDataSingleFence = 0x26;
    private static final int multipleBasesExtensionInfo = 0x28;
    private static final int informationTransmitionPackt = 0x94;

    private ConcoxListener listener;
    private int expectedMessage = -1;
    private final Object SYNC = new Object();

    private ConcoxReport answer;

    public Receiver(Connection con, byte[] imeiExpected, long timeout) {
        this.con = con;
        this.imeiExpected = imeiExpected;
        this.timeout = timeout;
    }

    public void setListener(ConcoxListener listener) {
        this.listener = listener;
    }

    @Override
    public void receiveMessage(byte[] message) {

        // System.out.println("mensaje " + Utils.hexToString(message));
        if (isPacketLen2byte[0] == 0x79) {

            int mType = message[4] & 0xFF;

            switch (mType) {

                case informationTransmitionPackt:
                    //System.out.println("informationTransmitionPackt ");
                    break;
            
            case onlineCommandResponse:
                    //System.out.println("onlineCommandResponse");
                    byte[] commandData = Arrays.copyOfRange(message, 4, message.length - 6);
                    if (expectedMessage == 1) {
                        synchronized (SYNC) {
                            answer = Parser.onlineCommandResponse(commandData);
                            SYNC.notifyAll();
                        }
                    }
                    break;
            
            }
        }

        if (isPacketLen1byte[0] == 0x78) {
            int mType = message[3] & 0xFF; // fourth byte corresponds to message type

            switch (mType) {

                case login:
                    //         System.out.println("login");
                    ConcoxReport report = new ConcoxReport();
                    byte[] imeiReceived = Arrays.copyOfRange(message, 4, message.length - 10);
                    int infoSerialNumber = 0;
                    /*
                TODO: procesar información que trae el paquete de login
                     */

                    if (Arrays.equals(imeiReceived, imeiExpected)) {

                        byte[] loginAccept = new byte[10];

                        loginAccept[0] = start[0];  //start bit
                        loginAccept[1] = start[1];  //start bit
                        loginAccept[2] = (byte) (loginAccept.length - 5);  //message length
                        loginAccept[3] = login;  // protocol number   
                        loginAccept[4] = message[12]; //information serial number
                        loginAccept[5] = message[13]; //information serial number
                        byte[] crc = CRC16.getCRCITU(Arrays.copyOfRange(loginAccept, 2, loginAccept.length - 4)); //from message length to information serial number
                        loginAccept[6] = crc[2];  //error check
                        loginAccept[7] = crc[3];  //error check
                        loginAccept[8] = stop[0];  //stop bit
                        loginAccept[9] = stop[1];  //stop bit

                        infoSerialNumber = ((message[12] & 0xFF) << 8) | (message[13] & 0xFF);
                        report.setInfoSerialNumber(infoSerialNumber);
                        con.sendMessage(loginAccept);

                        //           System.out.println("login respondido: " + Utils.hexToString(loginAccept));
                    } else {
                        //         System.out.println("El IMEI no es el esperado");
                        //if the IMEI aren't the same, do nothing
                    }

                    break;

                case heartBeat: {
                    //   System.out.println("heartbeat");
                    byte[] heartBeatData = Arrays.copyOfRange(message, 4, message.length - 8); //from date to GSM signal strength          
                    ConcoxReport reports = Parser.heartBeatParser(heartBeatData);
                    System.out.println("nivel de voltaje " + reports.getVoltajeLevelInteger());
                    
                    byte[] heartbeat = new byte[10];

                    heartbeat[0] = start[0];  //start bit
                    heartbeat[1] = start[1];  //start bit
                    heartbeat[2] = (byte) (heartbeat.length - 5);  //message length
                    heartbeat[3] = heartBeat;  // protocol number   
                    heartbeat[4] = message[10]; //information serial number
                    heartbeat[5] = message[11]; //information serial number
                    byte[] crc = CRC16.getCRCITU(Arrays.copyOfRange(heartbeat, 2, heartbeat.length - 4)); //from message length to information serial number
                    heartbeat[6] = crc[2];  //error check
                    heartbeat[7] = crc[3];  //error check
                    heartbeat[8] = stop[0];  //stop bit
                    heartbeat[9] = stop[1];

                    con.sendMessage(heartbeat);

                    if (listener != null) {
                        listener.onHeartBeatData(reports);
                    }
                }
                
                break;

                case gpsPositioningData: {

                    // System.out.println("gpsPositioningData");
                    byte[] gpsData = Arrays.copyOfRange(message, 4, message.length - 6); //from date to cell ID           
                    ConcoxReport reports = Parser.gpsDataParser(gpsData);

                    if (listener != null) {
                        listener.onData(reports);
                    }
                }
                break;

                case alarmDataSingleFence: {
                    //System.out.println("alarmData");
                    byte[] alarmData = Arrays.copyOfRange(message, 4, message.length - 6);
                    ConcoxReport reports = Parser.alarmDataParser(alarmData);
                    // ConcoxReport reports = Parser.alarmDataParser(message);

                    byte[] alarmDataResponse = new byte[10];

                    alarmDataResponse[0] = start[0];
                    alarmDataResponse[1] = start[1];
                    alarmDataResponse[2] = (byte) (alarmDataResponse.length - 5);
                    alarmDataResponse[3] = 0x26; //alarm data response protocol number
                    alarmDataResponse[4] = message[36];  //Serial number
                    alarmDataResponse[5] = message[37];
                    byte[] crc16 = CRC16.getCRCITU(Arrays.copyOfRange(alarmDataResponse, 2, alarmDataResponse.length - 4)); //from message length to information serial number
                    alarmDataResponse[6] = crc16[0];
                    alarmDataResponse[7] = crc16[1];
                    alarmDataResponse[8] = stop[0];
                    alarmDataResponse[9] = stop[1];

                    con.sendMessage(alarmDataResponse);
                    // System.out.println("Alarm data response " + Utils.hexToString(alarmDataResponse));

                    if (listener != null) {
                        listener.onData(reports);
                    }
                }
                break;

                case onlineCommandResponse:
                    //System.out.println("onlineCommandResponse");
                    byte[] commandData = Arrays.copyOfRange(message, 4, message.length - 6);
                    if (expectedMessage == 1) {
                        synchronized (SYNC) {
                            answer = Parser.onlineCommandResponse(commandData);
                            SYNC.notifyAll();
                        }
                    }
                    break;

                case multipleBasesExtensionInfo:
                    //System.out.println("multipleBasesExtensionInfo");
                    break;

                case lbsAlarm:
                    // System.out.println("lbsAlarm");
                    //byte[] lbsAlarm = Arrays.copyOfRange(message, 4, message.length - 6);
                    ConcoxReport reports = Parser.alarmDataParser(message);

                    break;

            }

        }

    }

    public ConcoxReport sendMessage(byte[] toSend, int expected) {
        answer = null;
        expectedMessage = expected;
        con.sendMessage(toSend);

        if (answer == null) {
            synchronized (SYNC) {
                try {
                    SYNC.wait(timeout);
                } catch (InterruptedException ex) {
                }
            }
        }
        expectedMessage = -1;
        return answer;

    }

    @Override
    public void receiveMessage(byte[] message, Connection con) {

    }

}
