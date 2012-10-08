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

public class DeviceHandler extends HttpServlet {

    public Properties prop = null;
    private static Logger log = Logger.getLogger(DeviceHandler.class.getName());

    public DeviceHandler() throws IOException {

        prop = new Properties();
        InputStream is = new FileInputStream("./conf/main.property");
        prop.load(is);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int num = Integer.valueOf(request.getParameter("num"));
        int value = Integer.valueOf(request.getParameter("value"));
        String type = request.getParameter("type");

        Iris.zwaveSocketOut.println("DEVICE~" + num + "~" + value + "~" + type);
        log.info("[zwave] Set value: " + value + " to device: " + num + " Type: " + type);
        log.info("[zwave] Answer: " + Iris.zwaveSocketIn.readLine());
        response.getWriter().println("done");
    }
}