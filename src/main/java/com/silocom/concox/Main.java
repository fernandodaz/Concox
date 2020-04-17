/*
 * @Author Fernando Gonzalez.
 */
package com.silocom.concox;

import com.silocom.m2m.layer.physical.PhysicalLayer;
import java.util.Arrays;

/**
 *
 * @author silocom01
 */
public class Main {

    public static void main(String[] args) {
        byte[] mobileID = new byte[15]; //replace with device's IMEI
        int timeout = 5000;
        byte[] imeiExpected = new byte[]{(byte)0x01,(byte) 0x23,(byte) 0x45 ,(byte)0x67,(byte) 0x89 ,(byte)0x01 ,(byte)0x23 ,(byte)0x45};
        com.silocom.m2m.layer.physical.Connection con = PhysicalLayer.addConnection(3, 17501, "192.168.210.1");  
        Receiver rec = new Receiver(con, imeiExpected, timeout);
        con.addListener(rec);

    }
}
