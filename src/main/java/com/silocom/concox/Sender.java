package com.silocom.concox;

import java.util.Arrays;

/**
 *
 * @author Sergio 2
 */
public class Sender {

    Receiver rec;
    private int retry;
    String where = "787810800A000000007768657265230000DB3A0D0A";
    String status = "787811800B000000007374617475732300003BFA0D0A";

    public Sender(Receiver rec, int retry) {
        this.rec = rec;
        this.retry = retry;
    }

    public ConcoxReport getReport() {
        ConcoxReport answer = new ConcoxReport();

        ConcoxReport report = getGPS();

        if (report != null) {
            answer.setLatitude(report.getLatitude());
            answer.setLongitude(report.getLongitude());
            answer.setSatInUse(report.getSatInUse());
            answer.setSpeed(report.getSpeed());
            answer.setDate(report.getDate());
            System.out.println("gps nulo");
            report = getBattery();
        }
        if (report != null) {
            answer.setVoltajeLevel(report.getVoltajeLevel());
            answer.setVoltagePercent(report.getVoltagePercent());
            answer.setDate(report.getDate());
            System.out.println("bateria nulo");
        }

        return answer;
    }

    public ConcoxReport getGPS() {

        //String gpsRequest = "where#";
        byte[] getGPS = Utils.stringToHex(where);
        //byte[] messageToSend = commandToSend(gpsRequest);
        ConcoxReport report = rec.sendMessage(getGPS, 1);

        for (int i = 0; report == null && i < retry; i++) {
            report = rec.sendMessage(getGPS, 1);
        }
        return report;

    }

    public ConcoxReport getBattery() {
        //String batRequest = "status#";
        byte[] getBattery = Utils.stringToHex(status);
        // byte[] messageToSend = commandToSend(batRequest);
        ConcoxReport report = rec.sendMessage(getBattery, 2);

        for (int i = 0; report == null && i < retry; i++) {
            report = rec.sendMessage(getBattery, 2);
        }
        return report;

    }

    public static byte[] commandToSend(String comm) {
        ConcoxReport get = new ConcoxReport();
        int infoSerialNumber = get.getInfoSerialNumber();
        int index = 0;
        byte[] command = comm.getBytes();
        byte[] messageToSend = new byte[15 + command.length];

        for (int i = 0; i < 2; i++) {
            messageToSend[i] = 0x78;
        }

        messageToSend[2] = (byte) (10 + command.length); // Protocol Number + Information Content + Information Serial Number + Error Check
        messageToSend[3] = (byte) 0x80;
        messageToSend[4] = (byte) (4 + command.length);
        //messageToSend[8] = (byte) 0x01;

        for (int i = 9, j = 0; j < command.length; j++, i++) {
            messageToSend[i] = command[j];
            index = i + 1;
        }

        messageToSend[index] = (byte) 0x00;  //Serial number
        index++;
        messageToSend[index] = (byte) 0x00;  //Serial number
        index++;
        byte[] crc = CRC16.getCRCITU(Arrays.copyOfRange(messageToSend, 2, messageToSend.length - 4));
        messageToSend[index] = (byte) crc[0];//CRC0
        index++;
        messageToSend[index] = (byte) crc[1]; //CRC1  16D6
        index++;
        messageToSend[index] = (byte) 0x0D; //Stop bit 
        index++;
        messageToSend[index] = (byte) 0x0A; //Stop bit 

        return messageToSend;
    }

}
