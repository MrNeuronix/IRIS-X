package ru.phsystems.irisx.web;

import ru.phsystems.irisx.Iris;
import ru.phsystems.irisx.devices.Device;
import ru.phsystems.irisx.utils.Base64Coder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 14.09.12
 * Time: 12:19
 */
public class PagesContext {

    // Тут вроде должны обрабатываться данные для страниц
    public HashMap getContext(String url) throws IOException, FileNotFoundException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>) Iris.config.clone();

        // Главная страница
        if (url.equals("index")) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy ");
            Date date = new Date();
            long uptime = System.currentTimeMillis() - Iris.startTime;

            DateFormat formatter = new SimpleDateFormat("dd дней hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            map.put("irisUptime", String.format("%d мин, %d сек", TimeUnit.MILLISECONDS.toMinutes(uptime), TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime))));
            map.put("serverUptime", String.valueOf(formatter.format(calendar.getTime())));
            map.put("memoryTotal", String.valueOf((Runtime.getRuntime().totalMemory()) / (1024 * 1024)));
            map.put("memoryFree", String.valueOf(((Runtime.getRuntime().totalMemory()) / (1024 * 1024)) - (Runtime.getRuntime().freeMemory()) / (1024 * 1024)));
            map.put("date", dateFormat.format(date));

            map.put("wwwState", String.valueOf(Iris.wwwThread.isAlive()));
            map.put("zwaveState", String.valueOf(Iris.devicesThread.isAlive()));
            map.put("sheduleState", String.valueOf(Iris.sheduleThread.isAlive()));
            map.put("captureState", String.valueOf(Iris.captureThread.isAlive()));
        }

        // Камеры
        else if (url.equals("cams")) {
            String authorization = String.valueOf(Base64Coder.encode((Iris.config.get("httpUser") + ":" + Iris.config.get("httpPassword")).getBytes("8859_1")));
            map.put("auth", authorization);
        }

        // Устройства
        else if (url.equals("devices")) {

            // Получаем список устройств в сети Z-Wave
            Iris.zwaveSocketOut.println("ALIST");

            // Они отделяются друг от друга разделителем #
            String line = Iris.zwaveSocketIn.readLine();
            String[] device = line.split("#");
            ArrayList<Device> devicesOut = new ArrayList<Device>();

            for (String sDevice : device) {
                Device tmp = new Device(sDevice);
                devicesOut.add(tmp);
            }

            map.put("devicesList", devicesOut);
        }

        // Планировщик
        else if (url.equals("schedule")) {


            map.put("devicesList", "");
        }

        // Возвращаем значения
        return map;
    }

}
