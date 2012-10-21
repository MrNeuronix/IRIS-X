package ru.phsystems.irisx.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.10.12
 * Time: 14:57
 */
public class CaptureThread extends Thread {

    private int cam;
    private Thread t = null;
    private static Logger log = LoggerFactory.getLogger(CaptureThread.class.getName());

    public CaptureThread(int cam) throws IOException {
        this.cam = cam;
        t = new Thread(this);
        t.start();
    }

    public Thread getThread() {
        return t;
    }

    @Override
    public synchronized void run() {

        try {
            log.info("[cam " + cam + "] Start capture thread");

            // URL = http://host/control/video?cam=<number>
            URL camURL = new URL("http://192.168.10." + cam + "/image/jpeg.cgi");

            while (true) {

                try {

                    BufferedImage image = ImageIO.read(camURL);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", baos);

                    // Переводим изображение в byte array
                    byte[] bytesOut = baos.toByteArray();

                    // Тут будет распознавание лиц

                    // Берем паузу
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}
