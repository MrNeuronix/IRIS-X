package ru.phsystems.irisx.devices;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.09.12
 * Time: 18:36
 * License: GPL v3
 */

import ru.phsystems.irisx.Iris;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

// Класс отвечает за выставление значений для устройств Z-Wave

public class DeviceValuesHandler extends HttpServlet {

    public Properties prop = null;
    private static Logger log = Logger.getLogger(DeviceValuesHandler.class.getName());

    public DeviceValuesHandler() throws IOException {

        prop = new Properties();
        InputStream is = new FileInputStream("./conf/main.property");
        prop.load(is);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        Iris.zwaveSocketOut.println("VALUES");
        log.info("[zwave] Get values of devices");
        String line = Iris.zwaveSocketIn.readLine();
        String[] values = line.split("#");

        String out = "[";

        for (String sDevice : values) {
            String[] device = sDevice.split("~");
            out += "{\"Node\": \"" + device[1] + "\",\"Value\": \"" + device[2] + "\"},";
        }

        out = out.substring(0, out.length() - 1);
        out += "]";

        response.getWriter().println(out);
    }
}