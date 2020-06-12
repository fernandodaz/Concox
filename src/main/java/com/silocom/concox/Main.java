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
        byte[] imeiExpected = new byte[]{(byte)0x03,(byte) 0x53,(byte) 0x54 ,(byte)0x90,(byte) 0x91 ,(byte)0x33 ,(byte)0x25 ,(byte)0x01};
        com.silocom.m2m.layer.physical.Connection con = PhysicalLayer.addConnection(PhysicalLayer.MASTERTCP, 23000, "192.168.210.1");  
        Receiver rec = new Receiver(con, imeiExpected, timeout);
        con.addListener(rec);

    }
}
