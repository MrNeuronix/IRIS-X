package ru.phsystems.irisx.video;

import java.io.IOException;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.10.12
 * Time: 11:05
 */

public class CaptureService extends Thread {

    private Thread t = null;

    public CaptureService() throws IOException {
        t = new Thread(this);
        t.start();
    }

    public Thread getThread() {
        return t;
    }

    @Override
    public synchronized void run() {

        try {
            CaptureThread n1 = new CaptureThread(20);
            CaptureThread n2 = new CaptureThread(26);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
