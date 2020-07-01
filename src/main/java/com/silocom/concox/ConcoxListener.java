
package com.silocom.concox;



/**
 *
 * @author Sergio 2
 */
public interface ConcoxListener {
    public void onData(ConcoxReport reports);
    public void onHeartBeatData(ConcoxReport reports);
}


