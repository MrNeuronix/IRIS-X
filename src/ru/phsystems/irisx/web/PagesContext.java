package ru.phsystems.irisx.web;

import ru.phsystems.irisx.Iris;
import ru.phsystems.irisx.utils.Base64Coder;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

    public Properties prop = null;

    public PagesContext() throws IOException {

        prop = new Properties();
        InputStream is = new FileInputStream("./conf/main.property");
        prop.load(is);
    }

    // Тут вроде должны обрабатываться данные для страниц
    public HashMap getContext(String url) throws IOException, FileNotFoundException {
        HashMap<Object, Object> map = new HashMap<Object, Object>();

        // Загоняем содержимое property в шаблонизатор
        Iterator<Map.Entry<Object, Object>> iter = prop.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Object, Object> entry = iter.next();
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }

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
        }

        // Камеры
        else if (url.equals("cams")) {
            String authorization = String.valueOf(Base64Coder.encode((prop.getProperty("httpUser") + ":" + prop.getProperty("httpPassword")).getBytes("8859_1")));
            map.put("auth", authorization);
        }

        // Устройства
        else if (url.equals("devices")) {
            Socket echoSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {

                echoSocket = new Socket("127.0.0.1", 6004);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            } catch (IOException e) {
                System.err.println("[zwave] Couldn't get I/O for the connection to z-wave server");
            }

            // Получаем список устройств в сети Z-Wave
            out.println("ALIST");

            // Они отделяются друг от друга разделителем #
            String devices = in.readLine();
            String[] device = devices.split("#");
            String[][] dev = new String[250][5];

            for (int i = 0; i < device.length; i++) {
                String[] tmp = device[i].split("~");

                for (int c = 0; c < tmp.length; c++) {
                    if (tmp[c].equals("")) {
                        dev[i][c] = "none";
                    } else {
                        dev[i][c] = tmp[c];
                    }
                }
            }

            out.close();
            in.close();
            echoSocket.close();

            map.put("devicesList", dev);
            map.put("iter", device.length - 1);

        }

        // Возвращаем значения
        return map;
    }

}
