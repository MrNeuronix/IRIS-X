package ru.phsystems.irisx.devices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 10.09.12
 * Time: 13:26
 */
public class DeviceService implements Runnable {

    Thread t = null;

    public DeviceService() {
        t = new Thread(this);
        t.start();
    }

    public Thread getThread() {
        return t;
    }

    @Override
    public synchronized void run() {

        Process pb = null;
        try {
            pb = new ProcessBuilder("./zwave/zwave_tcp").start();

            InputStreamReader isr = new InputStreamReader(pb.getInputStream());
            BufferedReader input = new BufferedReader(isr);

            while (input.readLine() != null) {
            }

            pb.waitFor();
            input.close();
            isr.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}