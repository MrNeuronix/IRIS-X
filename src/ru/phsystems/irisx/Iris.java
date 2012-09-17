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
import ru.phsystems.irisx.voice.Synthesizer;
import ru.phsystems.irisx.voice.VoiceService;
import ru.phsystems.irisx.web.WebService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Iris {

    public static Thread wwwThread = null;
    public static Thread devicesThread = null;
    public static long startTime = 0;

    public static void main(String[] args) {

        try {

            startTime = System.currentTimeMillis();

            Properties prop = new Properties();
            InputStream is = new FileInputStream("./conf/main.property");
            prop.load(is);

            ExecutorService exs = Executors.newFixedThreadPool(10);
            Synthesizer outVoice = new Synthesizer(exs);

            System.out.println("[iris] System starting" + "\n[iris] Version: " + prop.getProperty("version"));

            // Запускам поток с веб-интерфейсом
            DeviceService devices = new DeviceService();
            devicesThread = devices.getThread();

            Thread.sleep(3000);

            // Запускам поток с записью звука
            VoiceService voice = new VoiceService();

            // Запускам поток с веб-интерфейсом
            WebService www = new WebService();
            wwwThread = www.getThread();

            System.out.println("[iris] Done!");

            outVoice.setAnswer("Система запущена");
            exs.submit(outVoice).get();

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

}
