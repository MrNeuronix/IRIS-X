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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

// Класс отвечает за выставление значений для устройств Z-Wave

public class DeviceHandler extends HttpServlet {

    public Properties prop = null;

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

        Socket echoSocket = null;
        PrintWriter out = null;

        try {

            echoSocket = new Socket("127.0.0.1", 6004);
            out = new PrintWriter(echoSocket.getOutputStream(), true);

        } catch (IOException e) {
            System.err.println("[zwave] Couldn't get I/O for the connection to z-wave server");
        }

        out.print("DEVICE~" + num + "~" + value + "~" + type);

        System.err.println("[zwave] Device: " + num + " Set value: " + value);
        response.getWriter().println("done");
    }
}