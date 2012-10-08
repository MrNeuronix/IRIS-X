package ru.phsystems.irisx;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.09.12
 * Time: 15:24
 * License: GPL v3
 */

import ru.phsystems.irisx.devices.DeviceService;
import ru.phsystems.irisx.shedule.SheduleService;
import ru.phsystems.irisx.video.CaptureService;
import ru.phsystems.irisx.voice.Synthesizer;
import ru.phsystems.irisx.voice.VoiceService;
import ru.phsystems.irisx.web.WebService;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Iris {

    public static Thread wwwThread = null;
    public static Thread sheduleThread = null;
    public static Thread devicesThread = null;
    public static long startTime = 0;
    public static PrintWriter zwaveSocketOut = null;
    public static BufferedReader zwaveSocketIn = null;

    private static Logger log = Logger.getLogger(Iris.class.getName());

    public static void main(String[] args) {

        try {

            startTime = System.currentTimeMillis();

            Properties prop = new Properties();
            InputStream is = new FileInputStream("./conf/main.property");
            prop.load(is);

            ExecutorService exs = Executors.newFixedThreadPool(10);
            Synthesizer outVoice = new Synthesizer(exs);

            log.info("[iris] System starting");
            log.info("[iris] Version: " + prop.getProperty("version"));

            // Запускам поток с веб-интерфейсом
            DeviceService devices = new DeviceService();
            devicesThread = devices.getThread();

            Thread.sleep(3000);

            // Запускам поток с записью звука
            VoiceService voice = new VoiceService();

            Thread.sleep(8000);

            try {

                Socket echoSocket = new Socket("127.0.0.1", 6004);
                zwaveSocketOut = new PrintWriter(echoSocket.getOutputStream(), true);
                zwaveSocketIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            } catch (IOException e) {
                log.info("[zwave] Couldn't get I/O for the connection to z-wave server");
            }

            // Запускам поток с планировщиком
            SheduleService shedule = new SheduleService();
            sheduleThread = shedule.getThread();

            // Запускам поток с захватом видео
            CaptureService imageCapture = new CaptureService();

            // Запускам поток с веб-интерфейсом
            WebService www = new WebService();
            wwwThread = www.getThread();

            log.info("[iris] Done!");

            outVoice.setAnswer("Система запущена");
            exs.submit(outVoice).get();

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

}
