package com.silocom.concox;

import java.util.Arrays;

/**
 *
 * @author Sergio 2
 */
public class Sender {

    Receiver rec;
    private int retry;

    public Sender(Receiver rec, int retry) {
        this.rec = rec;
        this.retry = retry;
    }

    public ConcoxReport getReport() {
        
        String gpsRequest = "WHERE#";

        byte [] messageToSend = commandToSend(gpsRequest);
        ConcoxReport report = rec.sendMessage(messageToSend, 1);

        for (int i = 0; report == null && i < retry; i++) {
            report = rec.sendMessage(messageToSend, 1);
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
        messageToSend[8] = (byte) 0x01;

        for (int i = 9, j = 0; j < command.length; j++, i++) {
            messageToSend[i] = command[j];
            index = i + 1;
        }

        messageToSend[index] = (byte) (infoSerialNumber >> 8);  //Serial number
        index++;
        messageToSend[index] = (byte) (infoSerialNumber);  //Serial number
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
