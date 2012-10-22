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

import lombok.extern.slf4j.Slf4j;
import ru.phsystems.irisx.Iris;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j

// Класс отвечает за выставление значений для устройств Z-Wave

public class DeviceValuesHandler extends HttpServlet {
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
