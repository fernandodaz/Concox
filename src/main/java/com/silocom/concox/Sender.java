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

    public ConcoxReport requestGPS() {
        //  44 57 58 58 23
        ConcoxReport get = new ConcoxReport();
        int infoSerialNumber = get.getInfoSerialNumber();
        
        byte[] messageToSend = new byte[20];
        messageToSend[0] = (byte) 0x78;
        messageToSend[1] = (byte) 0x78;
        messageToSend[2] = (byte) (messageToSend.length - 1);
        messageToSend[3] = (byte) 0x80;
        messageToSend[4] = (byte) 0x00; // Server flag bit
        messageToSend[5] = (byte) 0x00; // Server flag bit
        messageToSend[6] = (byte) 0x00; // Server flag bit
        messageToSend[7] = (byte) 0x01; // Server flag bit
        messageToSend[8] = (byte) 0x57;  //Command 
        messageToSend[9] = (byte) 0x48;//Command 
        messageToSend[10] = (byte) 0x45;//Command 
        messageToSend[11] = (byte) 0x52;//Command 
        messageToSend[12] = (byte) 0x45;//Command  57 48 45 52 45 23
        messageToSend[13] = (byte) 0x23;
        messageToSend[14] = (byte)(infoSerialNumber >> 8); //Information serial number
        messageToSend[15] = (byte)(infoSerialNumber); //Information serial number
        byte[] crc = CRC16.getCRCITU(Arrays.copyOfRange(messageToSend, 2, messageToSend.length - 4));
        messageToSend[16] = crc[0];
        messageToSend[17] = crc[1];
        messageToSend[18] = 0x0D;
        messageToSend[19] = 0x0A;
        
        
        ConcoxReport report = rec.sendMessage(messageToSend,1);

        for (int i = 0; report == null && i < retry; i++) {
            report = rec.sendMessage(messageToSend,1);
        }
        return report;

      
    }

}
