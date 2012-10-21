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

import ru.phsystems.irisx.database.SQL;
import ru.phsystems.irisx.devices.Device;
import ru.phsystems.irisx.devices.DeviceService;
import ru.phsystems.irisx.shedule.SheduleService;
import ru.phsystems.irisx.utils.Config;
import ru.phsystems.irisx.video.CaptureService;
import ru.phsystems.irisx.voice.Synthesizer;
import ru.phsystems.irisx.voice.VoiceService;
import ru.phsystems.irisx.web.WebService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static boolean shutdownCams = false;
    public static ArrayList<Device> devicesArray = new ArrayList<Device>();
    public static SQL sql;
    public static HashMap<String, String> config;

    private static Logger log = Logger.getLogger(Iris.class.getName());

    private static boolean connectZWave() {
        try {
            Socket echoSocket = new Socket("127.0.0.1", 6004);
            zwaveSocketOut = new PrintWriter(echoSocket.getOutputStream(), true);
            zwaveSocketIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        try {

            startTime = System.currentTimeMillis();

            sql = new SQL();
            Config cfg = new Config();
            config = cfg.getConfig();

            log.info("[iris] System starting");
            log.info("[iris] Version: " + config.get("version"));

            // Запускам поток с веб-интерфейсом
            DeviceService devices = new DeviceService();
            devicesThread = devices.getThread();

            Thread.sleep(3000);

            // Запускам поток с Z-Wave
            VoiceService voice = new VoiceService();

            Thread.sleep(3000);

            // дем готовности Z-Wave
            while (!connectZWave()) {
                Thread.sleep(1000);
                log.info("Waiting for Z-Wave...");
            }

            // Получаем список устройств в сети Z-Wave
            Iris.zwaveSocketOut.println("ALIST");

            // Они отделяются друг от друга разделителем #
            String lined = Iris.zwaveSocketIn.readLine();
            String[] device = lined.split("#");

            for (String sDevice : device) {
                Device tmp = new Device(sDevice);
                devicesArray.add(tmp);
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

            ExecutorService exs = Executors.newFixedThreadPool(10);
            Synthesizer Voice = new Synthesizer(exs);
            Voice.setAnswer("Система запущена");
            exs.submit(Voice).get();

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

}
