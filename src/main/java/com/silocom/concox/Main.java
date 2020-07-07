/*
 * @Author Fernando Gonzalez.
 */
package com.silocom.concox;

import com.silocom.m2m.layer.physical.PhysicalLayer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silocom01
 */
public class Main {

    public static void main(String[] args) {
        byte[] mobileID = new byte[15]; //replace with device's IMEI
        int timeout = 5000;
        byte[] imeiExpected = new byte[]{(byte)0x03,(byte) 0x53,(byte) 0x54 ,(byte)0x90,(byte) 0x91 ,(byte)0x33 ,(byte)0x25 ,(byte)0x01};
        com.silocom.m2m.layer.physical.Connection con = PhysicalLayer.addConnection(PhysicalLayer.MASTERTCP, 23000, "192.168.210.1");  
       // com.silocom.m2m.layer.physical.Connection con = PhysicalLayer.addConnection(PhysicalLayer.MASTERTCP, 23000, "*");  
       Receiver rec = new Receiver(con, imeiExpected, timeout);
        con.addListener(rec);
       /* byte[] comm = new byte[]{(byte)0x78,(byte)0x78,(byte)0x10,(byte)0x80,(byte)0x0A,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x77,(byte)0x68,(byte)0x65,(byte)0x72,(byte)0x65,(byte)0x23,(byte)0x00,
            (byte)0x00,(byte)0xDB,(byte)0x3A,(byte)0x0D,(byte)0x0A};*/
       
       byte[] comm = new byte[]{(byte) 0x78,(byte) 0x78,(byte) 0x11,(byte) 0x80,(byte) 0x0B,(byte) 0x00,(byte) 0x00
               ,(byte) 0x00,(byte) 0x00,(byte) 0x73,(byte) 0x74,(byte) 0x61,(byte) 0x74,(byte) 0x75
               ,(byte) 0x73,(byte) 0x23,(byte) 0x00,(byte) 0x00,(byte) 0xCB,(byte) 0x5B,(byte) 0x0D,(byte) 0x0A};

        while (!con.isConnected()) {
    
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
        Sender sender = new Sender(rec, 3);
        while (true) {
            //con.sendMessage(comm);
                try{
           System.out.println("Lectura " +sender.getReport().toString());
                       
                }catch(Exception e){
       e.printStackTrace();
       }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
