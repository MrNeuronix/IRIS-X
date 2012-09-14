package ru.phsystems.irisx.web;

import ru.phsystems.irisx.Iris;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public HashMap getContext(String url) {
        HashMap<String, String> map = new HashMap<String, String>();

        // Загоняем содержимое property в шаблонизатор
        Iterator<Map.Entry<Object, Object>> iter = prop.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Object, Object> entry = iter.next();
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }

        System.err.println("PAGE: " + url);

        // Главная страница
        if (url.equals("index")) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy ");
            Date date = new Date();

            map.put("processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
            map.put("memoryTotal", String.valueOf((Runtime.getRuntime().totalMemory()) / (1024 * 1024)));
            map.put("memoryFree", String.valueOf(((Runtime.getRuntime().totalMemory()) / (1024 * 1024)) - (Runtime.getRuntime().freeMemory()) / (1024 * 1024)));
            map.put("date", dateFormat.format(date));

            map.put("wwwState", String.valueOf(Iris.wwwThread.isAlive()));
        }

        // Камеры
        else if (url.equals("cams")) {
            map.put("page", "index");
        }

        // Возвращаем значения
        return map;
    }

}
